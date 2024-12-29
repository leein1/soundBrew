package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


    //
    @PostMapping(value = "/music",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(// 추후 토큰 사용
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title) {

        try{

            String uniqueFilename = fileService.uploadFile(file,title);
            return ResponseEntity.ok("성공" + uniqueFilename);

        } catch(IllegalArgumentException e){

            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {

            return ResponseEntity.status(500).body("실패");
        }
    }

//    @GetMapping("/download/{filename}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String filename){    // 추후 토큰 사용
//
//        /*
//        토큰 검증 과정 필요함!!!!
//
//
//         */
//
//
//        try {
//            Path filePath = fileService.downloadFile(filename);
//
//            //  파일 경로로 리소스 객체 생성
//            //  파일 데이터를 HTTP 응답에 첨부 가능하게 해줌
//            Resource resource = new UrlResource(filePath.toUri());
//
//            //  파일 없는 경우
//            if (!resource.exists()) {
//                return ResponseEntity.notFound().build();
//            }
//
//            return ResponseEntity.ok()
//                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
//                    .body(resource);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).build();
//        }
//
//    }

    @GetMapping("/music/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {// 추후 토큰 사용

        try {
            Resource resource = fileService.downloadFile(filename);
            Path filePath = fileService.getFile(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/stream/{filename}")
    public ResponseEntity<Resource> streamFile(
            @PathVariable String filename,
            @RequestParam(value = "token", required = false) String token) throws IOException {

        try {

            Path filePath = fileService.getFile(filename);
            long fileSize = Files.size(filePath);

            // 범위 ㅈㅔ한
            long maxAllowedRange = 5 * 1024 * 1024; // 5MB

            // 토큰이 없는 경우 시작부터 1분까지만 - 320kbps
            if (token == null) {

                long allowedEnd = 60 * 40 * 1024; // 1분 (320kbps)

                byte[] data = fileService.readPartialFile(filePath, 0, Math.min(fileSize - 1, allowedEnd));

                return ResponseEntity.status(206)
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                        .header(HttpHeaders.CONTENT_RANGE, "bytes 0-" + allowedEnd + "/" + fileSize)
                        .body(new ByteArrayResource(data));
            }

            // 토큰 검증 현재는 그냥 문자열로 "user"가 아니면 오류 반환
            fileService.validateToken(token);

            // 제한범위 내 전체 파일 전송
            byte[] data = fileService.readPartialFile(filePath, 0, Math.min(fileSize - 1, maxAllowedRange));

            return ResponseEntity.status(206)
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                    .header(HttpHeaders.CONTENT_RANGE, "bytes 0-" + (Math.min(fileSize - 1, maxAllowedRange)) + "/" + fileSize)
                    .body(new ByteArrayResource(data));

        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfile(@RequestParam MultipartFile file, @RequestParam int userId) throws IOException {

        fileService.uploadProfileImage(file, Integer.toString(userId));

        return null;
    }

    @GetMapping("/profile/{userId}")
    public Resource getProfile(@PathVariable String userId){
        return null;
    }

    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable String userId){
        return null;
    }


}
