//package com.soundbrew.soundbrew.file;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.PutObjectResult;
//import com.amazonaws.services.s3.model.S3Object;
//import com.soundbrew.soundbrew.service.file.FileService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.util.UUID;
//
//@ExtendWith(MockitoExtension.class)
//class FileServiceUnitTest {
//
//    @Mock
//    private AmazonS3Client amazonS3Client;
//
//    @InjectMocks
//    private FileService fileService;  // FileService 내부 @Value 주입은 별도 처리 필요할 수 있음
//
//    // 테스트 전 사전 설정
//    @BeforeEach
//    void setUp() throws Exception {
//        // 테스트용 버킷 이름를 강제 주입 (예: ReflectionTestUtils 사용)
//        // 또는 FileService 생성자를 통해 주입하는 방식으로 수정할 수도 있습니다.
//        // 여기서는 ReflectionTestUtils 예시:
//        org.springframework.test.util.ReflectionTestUtils.setField(fileService, "bucket", "test-bucket");
//    }
//
//    @Test
//    //FileService#getFile 메서드가 S3에 저장된 파일의 URL을 올바르게 생성하는지 확인합니다.
//    void testGetFileReturnsCorrectUrl() throws MalformedURLException {
//        // given
//        String filename = "sounds/sample.mp3";
//        String expectedUrl = "https://s3.ap-northeast-2.amazonaws.com/test-bucket/" + filename;
//        when(amazonS3Client.getUrl(anyString(), anyString()))
//                .thenReturn(UriComponentsBuilder.fromHttpUrl(expectedUrl).build().toUri().toURL());
//
//        // when
//        String url = fileService.getFile(filename);
//
//        // then
//        assertEquals(expectedUrl, url);
//    }
//
//    @Test
//    //음원 업로드 메서드인 uploadSoundFile가 지원하는 확장자(.mp3 또는 .wav)를 가진 파일에 대해 정상적으로 처리되고, 고유 파일 경로를 올바르게 생성하는지 확인합니다.
//    void testUploadSoundFileWithValidExtension() throws IOException {
//        // given
//        String title = "testSong";
//        byte[] content = "test content".getBytes();
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "song.mp3", "audio/mpeg", content);
//
//        // putObject가 호출될 때 dummy 결과(PutObjectResult)를 반환하도록 스텁을 설정합니다.
//        PutObjectResult putObjectResult = new PutObjectResult();
//        when(amazonS3Client.putObject(eq("test-bucket"), anyString(), any(InputStream.class), isNull()))
//                .thenReturn(putObjectResult);
//
//        // when
//        String uniqueFilename = fileService.uploadSoundFile(multipartFile, title);
//
//        // then
//        assertNotNull(uniqueFilename);
//        assertTrue(uniqueFilename.startsWith("sounds/"));
//        assertTrue(uniqueFilename.endsWith(".mp3"));
//    }
//
//
//    @Test
//    //지원하지 않는 파일 형식(예, .txt)을 업로드하려고 할 때, uploadSoundFile 메서드가 올바르게 예외를 발생시키는지 검증합니다.
//    void testUploadSoundFileWithInvalidExtension() {
//        // given
//        String title = "testSong";
//        byte[] content = "test content".getBytes();
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "song.txt", "text/plain", content);
//
//        // when & then
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            fileService.uploadSoundFile(multipartFile, title);
//        });
//        assertTrue(exception.getMessage().contains("지원하지 않는 파일 형식"));
//    }
//
//    @Test
//    //파일 다운로드 시, S3에 해당 파일이 존재하지 않을 경우 적절한 예외(FileNotFoundException을 포함한 IOException)가 발생하는지 확인합니다.
//    void testDownloadSoundFileNotFound() throws IOException {
//        // given
//        String filename = "nonexistent.mp3";
//        // S3Object를 가져올 때 AmazonS3Exception을 던지도록 설정
//        when(amazonS3Client.getObject(anyString(), anyString()))
//                .thenThrow(new com.amazonaws.services.s3.model.AmazonS3Exception("Not Found"));
//
//        // when & then
//        Exception exception = assertThrows(IOException.class, () -> {
//            fileService.downloadSoundFile(filename);
//        });
//        assertTrue(exception.getMessage().contains("S3에서 파일을 찾을 수 없습니다"));
//    }
//
//
//}
