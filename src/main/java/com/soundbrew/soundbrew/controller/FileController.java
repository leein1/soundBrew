package com.soundbrew.soundbrew.controller;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
@Log4j2
@RequiredArgsConstructor
public class FileController {


    private final FileService fileService;

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file
            ,@RequestParam("title") String title) {

        try{

            String uniqueFilename = fileService.uploadFile(file,title);
            return ResponseEntity.ok("성공" + uniqueFilename);

        } catch (IOException e) {

            return ResponseEntity.status(500).body("실패");
        }


    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename){

        try {
            Path filePath = fileService.getFile(filename);

            //  파일 경로로 리소스 객체 생성
            //  파일 데이터를 HTTP 응답에 첨부 가능하게 해줌
            Resource resource = new UrlResource(filePath.toUri());

            //  파일 없는 경우
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }

    }



}
