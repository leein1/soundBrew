package com.soundbrew.soundbrew.service;

import com.soundbrew.soundbrew.config.FileProperties;
import com.soundbrew.soundbrew.repository.MusicFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class FileService {

    //  필드 이름 최대한 관례를 참고하였음

    private final FileProperties fileProperties;

    private final MusicFileRepository repository;


    //  업로드 (테스트 용이라 mp3,wav 확장자 지원)
    public String uploadFile(MultipartFile file,String title) throws IOException {

        String uploadDir = fileProperties.getUploadDir();
        Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);

        //  디렉토리 없으면 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }


        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        //  저장
        Path destinationPath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), destinationPath);

        return uniqueFilename;
    }

    //  파일 가져오기
    public Path getFile(String filename) {

        String uploadDir = fileProperties.getUploadDir();

        return Paths.get(System.getProperty("user.dir"), uploadDir, filename);
    }
}
