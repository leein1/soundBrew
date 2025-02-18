package com.soundbrew.soundbrew.service.file;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.SoundStreamDTO;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
//
//@Service
//@Log4j2
//@RequiredArgsConstructor
//public class FileService {
//
//    private final AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    public String getFile(String filename) {
//        // S3 객체 URL 반환
//        return amazonS3Client.getUrl(bucket, filename).toString();
//    }
//
//    // 공용 메서드: 파일 업로드
//    private String uploadToS3(MultipartFile file, String pathPrefix, String filenamePrefix) throws IOException {
//        // 고유 파일 이름 생성
//        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
//        String uniqueFilename = pathPrefix + "/" + filenamePrefix.replaceAll("\\..*$", "-") + UUID.randomUUID() + extension;
//
//        // S3에 파일 업로드
//        try (InputStream inputStream = file.getInputStream()) {
//            amazonS3Client.putObject(bucket, uniqueFilename, inputStream, null);
//        }
//
//        return uniqueFilename;
//    }
//
//    // 음원 파일 업로드
//    public String uploadSoundFile(MultipartFile file, String title) throws IOException {
//        validateFileExtension(file.getOriginalFilename(), List.of(".mp3", ".wav"));
//        return uploadToS3(file, "sounds", title);
//    }
//
//    // 앨범 이미지 업로드
//    public String uploadAlbumImage(MultipartFile file, String title) throws IOException {
//        validateFileExtension(file.getOriginalFilename(), List.of(".jpg", ".jpeg", ".png"));
//        return uploadToS3(file, "album-images", title);
//    }
//
//    // 프로필 이미지 업로드
//    public String uploadProfileImage(MultipartFile file, String userId) throws IOException {
//        validateFileExtension(file.getOriginalFilename(), List.of(".jpg", ".jpeg", ".png"));
//        return uploadToS3(file, "profile-images", userId);
//    }
//
//    // 파일 다운로드
//    public Resource downloadSoundFile(String filename) throws IOException {
//        String fileKey = "sounds/" + filename;
//
//        try (S3Object s3Object = amazonS3Client.getObject(bucket, fileKey)) {
//            byte[] data = s3Object.getObjectContent().readAllBytes();
//            return new ByteArrayResource(data);
//        } catch (AmazonS3Exception e) {
//            throw new FileNotFoundException("S3에서 파일을 찾을 수 없습니다: " + fileKey);
//        }
//    }
//
//    // 프로필 이미지 가져오기
//    public Resource getProfileImage(String userId) throws IOException {
//        String fileKey = "profile-images/" + userId + ".jpg";
//
//        try (S3Object s3Object = amazonS3Client.getObject(bucket, fileKey)) {
//            byte[] data = s3Object.getObjectContent().readAllBytes();
//            return new ByteArrayResource(data);
//        } catch (AmazonS3Exception e) {
//            throw new FileNotFoundException("S3에서 프로필 이미지를 찾을 수 없습니다: " + fileKey);
//        }
//    }
//
//    // 파일 삭제
//    public void deleteProfileImage(String userId) {
//        String fileKey = "profile-images/" + userId + ".jpg";
//
//        try {
//            amazonS3Client.deleteObject(bucket, fileKey);
//        } catch (AmazonS3Exception e) {
//            log.error("S3 파일 삭제 실패: " + fileKey, e);
//            throw new IllegalStateException("S3 파일 삭제에 실패했습니다.");
//        }
//    }
//
//    // 공용 메서드: 확장자 검증
//    private void validateFileExtension(String filename, List<String> validExtensions) {
//        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
//        if (validExtensions.stream().noneMatch(extension::equals)) {
//            throw new IllegalArgumentException("지원하지 않는 파일 형식: " + extension);
//        }
//    }
//
//    public ResponseDTO<SoundStreamDTO> streamSound(HttpRange range, String fileName) throws IOException {
//        final long FIXED_RANGE_SIZE = 2 * 1024 * 1024; // 2 MB
//
//        // S3 객체의 메타데이터를 통해 파일 크기 확인
//        String objectKey =  "sounds/"+fileName;
//        try {
//            amazonS3Client.getObjectMetadata(bucket, objectKey);
//        } catch (AmazonS3Exception e) {
//            if (e.getStatusCode() == 404) {
//                throw new FileNotFoundException("S3에 해당 파일이 없습니다: " + objectKey);
//            }
//            throw e; // 다른 예외는 그대로 전달
//        }
//
//        long fileLength = amazonS3Client.getObjectMetadata(bucket, objectKey).getContentLength();
//        long start = range.getRangeStart(fileLength);
//        long end = Math.min(start + FIXED_RANGE_SIZE - 1, fileLength - 1);
//
//        if (start >= fileLength) {
//            throw new IllegalArgumentException("음원 재생 길이를 초과하는 요청입니다.");
//        }
//
//        // S3에서 지정된 범위의 데이터를 가져오기
//        GetObjectRequest rangeRequest = new GetObjectRequest(bucket, objectKey)
//                .withRange(start, end);
//
//        byte[] data;
//        try (S3Object s3Object = amazonS3Client.getObject(rangeRequest);
//             InputStream inputStream = s3Object.getObjectContent()) {
//            data = IOUtils.toByteArray(inputStream);
//        }
//
//        // 결과를 DTO에 담아 반환
//        return ResponseDTO.<SoundStreamDTO>withSingleData()
//                .dto(new SoundStreamDTO(data, start, end, fileLength))
//                .build();
//    }
//}



@Service
@Log4j2
@RequiredArgsConstructor
public class FileService {
    private final UserService userService;

    private final Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
    private final Path profileImageDir = uploadDir.resolve("profile-images");
    private final Path albumImageDir = uploadDir.resolve("album-images");
    private final Path soundDir = uploadDir.resolve("sounds");

    // 공용 메서드: 디렉토리 생성
    private void ensureDirectoryExists(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    // 공용 메서드: 파일 읽기
    private byte[] readFile(Path filePath) throws IOException {
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            throw new FileNotFoundException("파일을 읽을 수 없습니다: " + filePath);
        }
        return Files.readAllBytes(filePath);
    }

    // 공용 메서드: 부분 파일 읽기
    private byte[] readPartialFile(Path filePath, long start, long end) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
            file.seek(start);
            byte[] data = new byte[(int) (end - start + 1)];
            file.readFully(data);
            return data;
        }
    }

    // 공용 메서드: 확장자 검증
    private void validateFileExtension(String filename, List<String> validExtensions) {
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        if (validExtensions.stream().noneMatch(extension::equals)) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식: " + extension);
        }
    }

    // 공용 메서드: 파일 업로드
    private String uploadFile(MultipartFile file, Path uploadDir, List<String> validExtensions, String filenamePrefix) throws IOException {
        // 확장자 검증
        validateFileExtension(file.getOriginalFilename(), validExtensions);

        // 디렉토리 존재 확인 및 생성
        ensureDirectoryExists(uploadDir);

        // 고유 파일 이름 생성
        String filenameFixed = filenamePrefix.replaceAll("\\..*$", "-");
        System.err.println(filenameFixed);
        String uniqueFilename = filenameFixed + UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        Path destinationPath = uploadDir.resolve(uniqueFilename);

        // 파일 복사
        Files.copy(file.getInputStream(), destinationPath);

        return uniqueFilename;
    }

    // 음원 파일 업로드
    public String uploadSoundFile(MultipartFile file, String title) throws IOException {
        return uploadFile(file, soundDir, List.of(".mp3", ".wav"), title);
    }

    // 앨범 이미지 업로드
    public String uploadAlbumImage(MultipartFile file, String title) throws IOException {
        return uploadFile(file, albumImageDir, List.of(".jpg", ".jpeg", ".png"), title);
    }

    // 프로필 이미지 업로드
    public ResponseDTO<String> uploadProfileImage(MultipartFile file, String userId) throws IOException {
        String fileUploadName = uploadFile(file, profileImageDir, List.of(".jpg", ".jpeg", ".png"), userId);
        return userService.updateProfile(Integer.parseInt(userId),fileUploadName);
    }


    //  음원 파일 다운로드
    public Resource downloadSoundFile(String filename) throws IOException {
        Path filePath = soundDir.resolve(filename);
        return new ByteArrayResource(readFile(filePath));
    }

    // 파일 스트리밍
    public ResponseDTO<SoundStreamDTO> streamSound(HttpRange range, String fileName) throws IOException {
        final long FIXED_RANGE_SIZE = 2 * 1024 * 1024; // 2 MB
        Path filePath = soundDir.resolve(fileName);

        byte[] data;
        long fileLength = Files.size(filePath);
        long start = range.getRangeStart(fileLength);
        long end = Math.min(start + FIXED_RANGE_SIZE - 1, fileLength - 1);

        if (start >= fileLength) {
            throw new IllegalArgumentException("음원 재생 길이를 초과하는 요청입니다.");
        }

        data = readPartialFile(filePath, start, end);

        return ResponseDTO.<SoundStreamDTO>withSingleData()
                .dto(new SoundStreamDTO(data, start, end, fileLength))
                .build();
    }

    // 프로필 이미지 가져오기
    public Resource getProfileImage(String userId) throws IOException {
        Path imagePath = profileImageDir.resolve(userId + ".jpg");
        return new ByteArrayResource(readFile(imagePath));
    }

//    // 앨범 이미지 가져오기
//    public Resource getAlbumImage(){
//
//    }

    // 프로필 이미지 삭제
    public void deleteProfileImage(String userId) throws IOException {
        Path imagePath = profileImageDir.resolve(userId + ".jpg");
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
        }
    }

    // 인증 토큰 검증 (간단한 예시)
    public void validateToken(String token) {
        if (!"user".equals(token)) {
            throw new SecurityException("유효하지 않은 토큰입니다.");
        }
    }

    // 파일 경로 가져오기
    public Path getFile(String filename) {
        return uploadDir.resolve(filename);
    }
}
