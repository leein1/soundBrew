package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
@Log4j2
@RequiredArgsConstructor
public class FileController {
    // 토큰 유효성 확인
    // 범위 요청
    //


    private final FileService fileService;

    // 공용 메서드: 파일 업로드 처리
    private ResponseEntity<String> handleFileUpload(MultipartFile file, String identifier, String uploadType) {
        try {
            String uniqueFilename;

            switch (uploadType) {
                case "SOUND": uniqueFilename = fileService.uploadSoundFile(file, identifier);break;
                case "PROFILE": uniqueFilename = fileService.uploadProfileImage(file, identifier).getMessage();break;
                case "ALBUM": uniqueFilename = fileService.uploadAlbumImage(file, identifier);break;
                default: throw new IllegalArgumentException("지원하지 않는 업로드 타입입니다.");
            }
            return ResponseEntity.ok().body(uniqueFilename);

        } catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: 서버 오류"); }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(value = "/tracks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadMusic(@RequestParam("file") MultipartFile file, @RequestParam("title") String title) {
        return handleFileUpload(file, title, "SOUND");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(value = "/profiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfile(@RequestParam("file") MultipartFile file, @RequestParam("userId") int userId) {
        return handleFileUpload(file, Integer.toString(userId), "PROFILE");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(value = "/albums", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAlbumImage(@RequestParam("file") MultipartFile file, @RequestParam("title") String title) {
        return handleFileUpload(file, title, "ALBUM");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/media/sounds/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        String currentFile = "media/sounds/" + filename;
        log.info(filename);
        log.info(currentFile);
        try {
            Resource resource = fileService.downloadSoundFile(currentFile);
            // 파일 확장자에 따라 콘텐츠 타입 지정
            String contentType = "audio/mpeg"; // 기본값
            if (filename.toLowerCase().endsWith(".wav")) {
                contentType = "audio/wav";
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + URLEncoder.encode(filename, StandardCharsets.UTF_8))
                    .body(resource);
        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    //수정시에는 이전의 프로필 사진 삭제
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable int userId){

        return null;
    }




}
