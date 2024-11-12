package com.soundbrew.soundbrew.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/stream")
@AllArgsConstructor
public class SampleRestController {
    // ResourceLoader는 Spring의 리소스 로더로, classpath나 file 같은 다양한 리소스 경로에서 파일을 동적으로 로드하는 기능을 제공합니다.
    // 주요 기능: 리소스를 로드하여, Resource 타입 객체를 반환합니다.
    private final ResourceLoader resourceLoader;

    // 스트리밍 테스트를 위한 엔드포인트
    @GetMapping("/test-sound")
    public ResponseEntity<byte[]> streamSoundTest(@RequestHeader HttpHeaders headers) throws IOException {
        // 파일패스에 이제 로컬 패스를 코드에 노출하지않고 넣어야함
        // 듣을 음원을 눌렀을때 식별자를 통해 값을 넘기고 그게 있는지 확인하고 해당 파일의 저장 제목을 넣어야함.
        String filePath = "static/sound/TestSound-SpaceOddity.mp3";

        // 음원 파일을 로드
        // 주요 기능: 리소스에 대한 읽기 및 메타데이터 접근 기능을 제공.
        // Resource는 Spring의 추상화된 리소스 접근 인터페이스로, 파일 시스템, 클래스패스, URL 등 다양한 위치에서 리소스를 읽을 수 있도록 돕습니다
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        Path path = Paths.get(resource.getURI());

        // 파일 크기 확인
        long fileLength = Files.size(path);

        // Range 요청 확인
        // HttpRange는 파일의 일부만 로드할 수 있는 기능을 제공합니
        // HttpRange가 설정되면 요청된 범위만큼 파일의 조각을 가져와 스트리밍을 가능하게 하며, "불러올 양"을 조정해 필요한 부분만 전송하게 합니다.
        List<HttpRange> ranges = headers.getRange();
        HttpRange range = ranges.isEmpty() ? null : ranges.get(0);

        // Range 요청이 있을 경우 해당 범위의 바이트만 전송
        if (range != null) {
            long start = range.getRangeStart(fileLength);
            long end = range.getRangeEnd(fileLength);
            long rangeLength = end - start + 1;

            byte[] data = new byte[(int) rangeLength];
            // RandomAccessFile은 파일의 특정 위치에 접근하여 데이터를 읽거나 쓸 수 있게 해주는 Java 클래스입니다.
            try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
                // raf.seek(start);는 파일의 특정 바이트 위치로 이동한 뒤, raf.readFully(data);는 지정된 위치에서 요청된 크기만큼 데이터를 읽어 배열에 저장합니다.
                raf.seek(start);
                raf.readFully(data);
            }

            // 클라이언트가 Range 헤더를 포함하여 부분적 바이트 요청을 보낸 경우
            // HttpStatus.PARTIAL_CONTENT를 반환하여 부분 스트리밍을 제공
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")  // MIME 타입 지정
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength))// 요청된 바이트 범위 길이 지정
                    .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength)// 요청 범위 정보를 클라이언트에 전달
                    .body(data);
        } else {
            // 클라이언트가 전체 파일을 요청하거나 Range 헤더가 없는 경우
            // HttpStatus.OK 상태와 함께 전체 파일 데이터를 반환
            byte[] data = Files.readAllBytes(path);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength))
                    .body(data);
        }
    }
}
