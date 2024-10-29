package com.soundbrew.soundbrew.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

//
@Service
public class FileUploadService {
    @Value("${upload.sound}") private String soundPath;
    @Value("${upload.profile}") private String profilePath;
    @Value("${upload.board}") private String boardPath;
    @Value("${upload.album}") private String albumPath;

    public String makeUploadLocation(String uploadType) {
        String directory =
        switch (uploadType) {
            case "sound" -> soundPath;
            case "profile" -> profilePath;
            case "board" -> boardPath;
            case "album" -> albumPath;
            default -> throw new IllegalArgumentException("유효하지 않은 업로드 타입입니다: " + uploadType);
        };
        return directory;
    }

    public File uploadFile(MultipartFile multipartFile, String directory) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일 목록이 비어있습니다.");
        }

        // 파일 경로 + 경로 구분자 + 업로드한 파일 오리지날 네임
        File file = new File(directory + File.separator + multipartFile.getOriginalFilename());
        // 실제로 저장되는 부분
        multipartFile.transferTo(file);
        return file;
    }
}
