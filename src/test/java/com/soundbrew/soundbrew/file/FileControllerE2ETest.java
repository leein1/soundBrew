//package com.soundbrew.soundbrew.file;
//
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.soundbrew.soundbrew.service.file.FileService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpStatus;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class FileControllerE2ETest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private AmazonS3Client amazonS3Client;
//
//    @MockBean
//    private FileService fileService;
//
//    private final String BASE_URL = "/api/files";
//
//    @Test
//    @WithUserDetails("ddjsjs12@naver.com")
//    void testUploadMusic_Success() throws Exception {
//        // given
//        byte[] content = "test content".getBytes();
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "song.mp3", "audio/mpeg", content);
//        String title = "testSong";
//        String expectedFilename = "sounds/testSong-uuid.mp3";
//
//        when(fileService.uploadSoundFile(any(MultipartFile.class), eq(title)))
//                .thenReturn(expectedFilename);
//
//        // when & then
//        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/tracks")
//                        .file(multipartFile)
//                        .param("title", title))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string(expectedFilename));
//    }
//
////    @Test
////    void testUploadMusic_InvalidExtension() throws Exception {
////        // given
////        byte[] content = "test content".getBytes();
////        MockMultipartFile multipartFile = new MockMultipartFile("file", "song.txt", "text/plain", content);
////        String title = "testSong";
////
////        when(fileService.uploadSoundFile(any(MultipartFile.class), eq(title)))
////                .thenThrow(new IllegalArgumentException("지원하지 않는 파일 형식"));
////
////        // when & then
////        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/tracks")
////                        .file(multipartFile)
////                        .param("title", title))
////                .andExpect(MockMvcResultMatchers.status().isBadRequest())
////                .andExpect(MockMvcResultMatchers.content().string(MockMvcResultMatchers.containsString("지원하지 않는 파일 형식")));
////    }
////
////    @Test
////    void testDownloadMusic_Success() throws Exception {
////        // given
////        String filename = "sample.mp3";
////        byte[] fileContent = "mock data".getBytes();
////        ByteArrayResource resource = new ByteArrayResource(fileContent);
////
////        when(fileService.downloadSoundFile(eq(filename))).thenReturn(resource);
////
////        // when & then
////        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/music/" + filename))
////                .andExpect(MockMvcResultMatchers.status().isOk());
////    }
////
////    @Test
////    void testDownloadMusic_NotFound() throws Exception {
////        // given
////        String filename = "nonexistent.mp3";
////
////        when(fileService.downloadSoundFile(eq(filename)))
////                .thenThrow(new FileNotFoundException("S3에서 파일을 찾을 수 없습니다: " + filename));
////
////        // when & then
////        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/music/" + filename))
////                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
////                .andExpect(MockMvcResultMatchers.content().string(MockMvcResultMatchers.("S3에서 파일을 찾을 수 없습니다")));
////    }
//}
