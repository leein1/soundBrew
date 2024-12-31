package com.soundbrew.soundbrew.service.file;

import com.soundbrew.soundbrew.config.FileProperties;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.sound.SoundStreamDTO;
import com.soundbrew.soundbrew.repository.MusicFileRepository;
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

@Service
@Log4j2
@RequiredArgsConstructor
public class FileService {

    private final FileProperties fileProperties;
    private final MusicFileRepository repository;

    private final Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
    private final Path profileImageDir = uploadDir.resolve("profile-images");

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

    // 파일 업로드
    public String uploadFile(MultipartFile file, String title) throws IOException {
        validateFileExtension(file.getOriginalFilename(), List.of(".mp3", ".wav"));
        ensureDirectoryExists(uploadDir);

        String uniqueFilename = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        Path destinationPath = uploadDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), destinationPath);

        return uniqueFilename;
    }

    // 파일 다운로드
    public Resource downloadFile(String filename) throws IOException {
        Path filePath = uploadDir.resolve(filename);
        return new ByteArrayResource(readFile(filePath));
    }

    // 파일 스트리밍
    public ResponseDTO<SoundStreamDTO> streamSound(HttpRange range, String fileName) throws IOException {
        final long FIXED_RANGE_SIZE = 2 * 1024 * 1024; // 2 MB
        Path filePath = uploadDir.resolve(fileName);

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

    // 프로필 이미지 업로드
    public String uploadProfileImage(MultipartFile file, String userId) throws IOException {
        validateFileExtension(file.getOriginalFilename(), List.of(".jpg", ".jpeg", ".png"));
        ensureDirectoryExists(profileImageDir);

        String uniqueFilename = userId + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        Path destinationPath = profileImageDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), destinationPath);

        return uniqueFilename;
    }

    // 프로필 이미지 가져오기
    public Resource getProfileImage(String userId) throws IOException {
        Path imagePath = profileImageDir.resolve(userId + ".jpg");
        return new ByteArrayResource(readFile(imagePath));
    }

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
