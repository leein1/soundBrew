package com.soundbrew.soundbrew.service.file;

import com.soundbrew.soundbrew.config.FileProperties;
import com.soundbrew.soundbrew.repository.MusicFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.core.io.Resource;

@Service
@Log4j2
@RequiredArgsConstructor
public class FileService {

    /*
    DB update는 생략 나중에 추가해야 함
     */

    private final FileProperties fileProperties;
    private final MusicFileRepository repository;

    //  파일 경로 새로 지정해주어야 함 - 현재 테스트용으로 되는대로 작성함
    private final Path uploadDir = Paths.get(System.getProperty("user.dir"), "upload");
    private final Path profileImageDir = uploadDir.resolve("profile-images");




    //  업로드 (mp3,wav 확장자만 지원)
    public String uploadFile(MultipartFile file,String title) throws IOException {

        //  확장자 검증
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf("."));


        //  null이거나 , !(mp3 or wav) 인 경우
        if(filename == null || !(extension.equals(".mp3") || extension.equals(".wav"))){
            throw new IllegalArgumentException(
                    "지원하지 않는 파일 형식." + filename.substring((filename.lastIndexOf(".")))
            );
        }

        //  디렉토리 없으면 생성
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String uniqueFilename = UUID.randomUUID().toString() + extension;

        //  저장
        Path destinationPath = uploadDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), destinationPath);

        return uniqueFilename;

//        String uploadDir = fileProperties.getUploadDir();
//        Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);
//
//        //  경로 없으면 생성
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//
//        String originalFilename = file.getOriginalFilename();
//        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String uniqueFilename = UUID.randomUUID().toString() + extension;
//
//        //  저장
//        Path destinationPath = uploadPath.resolve(uniqueFilename);
//        Files.copy(file.getInputStream(), destinationPath);
//
//        return uniqueFilename;
    }

    //  파일 다운로드
    public Resource downloadFile(String filename) throws IOException { // 토큰 인증 해야 함!!!!

    /*
    토큰 인증 필요
     */

        Path filePath = uploadDir.resolve(filename);

        return new ByteArrayResource(Files.readAllBytes(filePath));

//        String uploadDir = fileProperties.getUploadDir();
//
//        return Paths.get(System.getProperty("user.dir"), uploadDir, filename);
    }



    public Resource streamFile(String filename, String rangeHeader) throws IOException {    // 토큰 인증 해야 함!!!!
        /*
        토큰 인증 필요
         */

        Path filePath = uploadDir.resolve(filename);

        if (rangeHeader != null) {

            String[] ranges = rangeHeader.replace("bytes=", "").split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : Files.size(filePath) - 1;

            if (end >= Files.size(filePath)) end = Files.size(filePath) - 1;

            byte[] data = readPartialFile(filePath, start, end);
            return new ByteArrayResource(data);
        }

        return new ByteArrayResource(Files.readAllBytes(filePath));
    }


    public byte[] readPartialFile(Path filePath, long start, long end) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
            file.seek(start);
            byte[] data = new byte[(int) (end - start + 1)];
            file.readFully(data);
            return data;
        }
    }

    // 프로필 이미지 업로드
    public String uploadProfileImage(MultipartFile file, String userId) throws IOException {
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();

        if (filename == null || !(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png"))) {
            throw new IllegalArgumentException("지원하지 않는 이미지 형식: " + extension);
        }

        if (!Files.exists(profileImageDir)) {
            Files.createDirectories(profileImageDir);
        }

        String uniqueFilename = userId + extension;
        Path destinationPath = profileImageDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), destinationPath);

        return uniqueFilename;
    }

    // 프로필 이미지 출력
    public Resource getProfileImage(String userId) throws IOException {
        Path imagePath = profileImageDir.resolve(userId + ".jpg");
        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("프로필 이미지가 존재하지 않습니다: " + userId);
        }
        return new ByteArrayResource(Files.readAllBytes(imagePath));
    }

    // 프로필 이미지 삭제
    public void deleteProfileImage(String userId) throws IOException {
        Path imagePath = profileImageDir.resolve(userId + ".jpg");
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
        }
    }

    public void validateToken(String token) {

        if (!token.equals("user")) {
            throw new SecurityException("유효하지 않은 토큰입니다.");
        }
    }

    public Path getFile(String filename) {
        return uploadDir.resolve(filename);
    }



}
