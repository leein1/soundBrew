package com.soundbrew.soundbrew.service.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.SoundStreamDTO;
import com.soundbrew.soundbrew.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;

@Service
@Log4j2
@RequiredArgsConstructor
public class FileService {
    private final UserService userService;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String getFile(String filename) {
        // S3 객체 URL 반환
        return amazonS3Client.getUrl(bucket, filename).toString();
    }

    // 공용 메서드: 파일 업로드
    private String uploadToS3(MultipartFile file, String pathPrefix, String filenamePrefix) throws IOException {
        // 고유 파일 이름 생성
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
        String uniqueFilename = pathPrefix + "/" + filenamePrefix.replaceAll("\\..*$", "-") + UUID.randomUUID() + extension;

        // S3에 파일 업로드
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(bucket, uniqueFilename, inputStream, null);
        }

        return uniqueFilename;
    }

    // 음원 파일 업로드
    public String uploadSoundFile(MultipartFile file, String title) throws IOException {
        validateFileExtension(file.getOriginalFilename(), List.of(".mp3", ".wav"));
        return uploadToS3(file, "media/sounds", title);
    }

    // 앨범 이미지 업로드
    public String uploadAlbumImage(MultipartFile file, String title) throws IOException {
        validateFileExtension(file.getOriginalFilename(), List.of(".jpg", ".jpeg", ".png"));
        return uploadToS3(file, "media/album-images", title);
    }

    // 프로필 이미지 업로드
    public ResponseDTO<String> uploadProfileImage(MultipartFile file, String userId) throws IOException {
        validateFileExtension(file.getOriginalFilename(), List.of(".jpg", ".jpeg", ".png"));
        String fileName = uploadToS3(file, "media/profile-images", userId);
        return userService.updateProfile(Integer.parseInt(userId), fileName);
    }

    // 파일 다운로드
    public Resource downloadSoundFile(String filename) throws IOException {
        try {
            S3Object s3Object = amazonS3Client.getObject(bucket, filename);
            byte[] data = s3Object.getObjectContent().readAllBytes();

            System.out.println("다운로드한 파일 크기: " + data.length + " bytes"); // 로그 확인

            return new ByteArrayResource(data);
        } catch (AmazonS3Exception e) {
            throw new FileNotFoundException("S3에서 파일을 찾을 수 없습니다: " + filename);
        }
    }


    // 프로필 이미지 가져오기
    public Resource getProfileImage(String userId) throws IOException {
        String fileKey = "media/profile-images/" + userId + ".jpg";

        try (S3Object s3Object = amazonS3Client.getObject(bucket, fileKey)) {
            byte[] data = s3Object.getObjectContent().readAllBytes();
            return new ByteArrayResource(data);
        } catch (AmazonS3Exception e) {
            throw new FileNotFoundException("S3에서 프로필 이미지를 찾을 수 없습니다: " + fileKey);
        }
    }

    // 파일 삭제
    public void deleteProfileImage(String userId) {
        String fileKey = "media/profile-images/" + userId + ".jpg";

        try {
            amazonS3Client.deleteObject(bucket, fileKey);
        } catch (AmazonS3Exception e) {
            log.error("S3 파일 삭제 실패: " + fileKey, e);
            throw new IllegalStateException("S3 파일 삭제에 실패했습니다.");
        }
    }

    // 공용 메서드: 확장자 검증
    private void validateFileExtension(String filename, List<String> validExtensions) {
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        if (validExtensions.stream().noneMatch(extension::equals)) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식: " + extension);
        }
    }

    public ResponseDTO<SoundStreamDTO> streamSound(HttpRange range, String fileName) throws IOException {
        // S3 객체 키 설정
        String objectKey = "media/sounds/" + fileName;

        // 파일 존재 여부와 메타데이터 조회
        ObjectMetadata metadata;
        try {
            metadata = amazonS3Client.getObjectMetadata(bucket, objectKey);
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                throw new FileNotFoundException("S3에 해당 파일이 없습니다: " + objectKey);
            }
            throw e;
        }
        long fileLength = metadata.getContentLength();

        long start = range.getRangeStart(fileLength);
        // 2MB 단위로 범위를 제한, 파일의 남은 부분이 작으면 그만큼만 요청
        long end = Math.min(start + (2 * 1024 * 1024) - 1, fileLength - 1);

        if (start >= fileLength) {
            throw new IllegalArgumentException("음원 재생 길이를 초과하는 요청입니다.");
        }

        // S3에서 범위 지정 요청 생성
        GetObjectRequest rangeRequest = new GetObjectRequest(bucket, objectKey)
                .withRange(start, end);

        byte[] data;
        try (S3Object s3Object = amazonS3Client.getObject(rangeRequest);
             InputStream inputStream = s3Object.getObjectContent()) {
            data = IOUtils.toByteArray(inputStream);
        }

        return ResponseDTO.<SoundStreamDTO>withSingleData()
                .dto(new SoundStreamDTO(data, start, end, fileLength))
                .build();
    }

    // 추가: 파일의 전체 길이를 가져오는 메서드
    public long getFileLength(String fileName) throws IOException {
        String objectKey = "media/sounds/" + fileName;
        ObjectMetadata metadata = amazonS3Client.getObjectMetadata(bucket, objectKey);
        return metadata.getContentLength();
    }

    public String getContentType(String filename) {
        ObjectMetadata metadata = amazonS3Client.getObjectMetadata(bucket, filename);
        return metadata.getContentType();
    }
}
