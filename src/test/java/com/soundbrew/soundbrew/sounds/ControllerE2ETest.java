package com.soundbrew.soundbrew.sounds;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soundbrew.soundbrew.dto.sound.*;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.security.JWTUtil;
import org.springframework.security.core.GrantedAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("SoundController 통합 테스트")
public class ControllerE2ETest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTUtil jwtUtil;

    // 업데이트 상황 authentication
    @Test
    @DisplayName("PATCH /albums/{albumId} - 앨범 업데이트 통합 테스트")
    @WithUserDetails("dong@dong.com")
    public void testUpdateAlbum_BadAuth() throws Exception {
        int albumId = 10;
        AlbumDTO albumDTO=new AlbumDTO();
        albumDTO.setAlbumArtPath("test-image");
        albumDTO.setAlbumName("test-albumName");
        albumDTO.setDescription("test-albumDescription");

        mockMvc.perform(patch("/api/me/albums/{albumId}", albumId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDTO)))
                .andExpect(status().isUnauthorized());
    }

    // valid
    @Test
    @DisplayName("GET /tracks/{musicId} - 단일 사운드 조회 통합 테스트")
    public void testGetSoundOne_Valid() throws Exception {
        int musicId = -15;

        mockMvc.perform(get("/api/me/tracks/{musicId}", musicId))
                .andExpect(status().isUnauthorized());
    }

    // me
    @Test
    @DisplayName("POST /api/me/tracks/{musicId}/tags - 태그 업데이트 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testUpdateLinkTags() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int musicId = 15;
        // 요청에 사용할 TagsDTO (실제 필드에 맞게 설정)
        TagsDTO tagsDto = new TagsDTO();
        tagsDto.setMood(List.of("chill"));
        tagsDto.setGenre(List.of("disco"));
        tagsDto.setInstrument(List.of("piano"));

        // 실제 서비스 로직에 따라 결과가 달라지므로, 반환되는 JSON의 message 필드가 null이 아니거나 특정 값을 가지는지 확인
        mockMvc.perform(post("/api/me/tracks/{musicId}/tags", musicId)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    //me
    @Test
    @DisplayName("POST /sounds - 사운드 생성 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testCreateSound() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        // 요청에 사용할 DTO 객체 구성 (실제 DTO 구조에 맞게 수정)
        AlbumDTO albumDTO=new AlbumDTO();
        albumDTO.setAlbumArtPath("test-image");
        albumDTO.setAlbumName("test-albumName");
        albumDTO.setDescription("test-albumDescription");

        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setFilePath("test-music");
        musicDTO.setDescription("test-musicDescription");
        musicDTO.setTitle("test-title");

        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setInstrument(Arrays.asList("piano"));
        tagsDTO.setMood(Arrays.asList("chill"));
        tagsDTO.setGenre(Arrays.asList("pop"));

        SoundCreateDTO soundCreateDto = new SoundCreateDTO();
        soundCreateDto.setAlbumDTO(albumDTO);
        soundCreateDto.setMusicDTO(musicDTO);
        soundCreateDto.setTagsDTO(tagsDTO);

        mockMvc.perform(post("/api/me/sounds")
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soundCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    //me
    @Test
    @DisplayName("PATCH /albums/{albumId} - 앨범 업데이트 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testUpdateAlbum() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int albumId = 10;
        AlbumDTO albumDTO=new AlbumDTO();
        albumDTO.setAlbumArtPath("test-image");
        albumDTO.setAlbumName("test-albumName");
        albumDTO.setDescription("test-albumDescription");

        mockMvc.perform(patch("/api/me/albums/{albumId}", albumId)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    //me
    @Test
    @DisplayName("PATCH /tracks/{musicId} - 음악 업데이트 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testUpdateMusic() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int musicId = 15;
        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setFilePath("test-music");
        musicDTO.setDescription("test-musicDescription");
        musicDTO.setTitle("test-title");

        mockMvc.perform(patch("/api/me/tracks/{musicId}", musicId)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(musicDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    //me
    @Test
    @DisplayName("GET /tracks/{musicId} - 단일 사운드 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetSoundOne() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int musicId = 15;

        mockMvc.perform(get("/api/me/tracks/{musicId}", musicId).header("Authorization", "Bearer " + token) )// 🔥 JWT 추가)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dto").exists());
    }

    //me
    @Test
    @DisplayName("GET /albums/{albumId} - 단일 앨범 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetAlbumOne() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int albumId = 10;
        // RequestDTO 의 필요한 파라미터를 전달 (예: page, size 등)
        mockMvc.perform(get("/api/me/albums/{albumId}", albumId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dtoList").exists());
    }

    //me
    @Test
    @DisplayName("GET /tracks - 내 사운드 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetSoundMe() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        MvcResult result = mockMvc.perform(get("/api/me/tracks").header("Authorization", "Bearer " + token))
                .andDo(print()) // 요청 및 응답 내용을 콘솔에 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dtoList").exists())
                .andReturn();

        // 추가적으로 결과를 문자열로 가져와서 로그로 출력하거나 디버깅 변수로 사용할 수 있습니다.
        String responseContent = result.getResponse().getContentAsString();
        System.out.println("응답 내용: " + responseContent);
    }

    //me
    @Test
    @DisplayName("GET /albums - 내 앨범 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetAlbumMe() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        mockMvc.perform(get("/api/me/albums").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dtoList").exists());
    }

    //me
    @Test
    @DisplayName("GET /tags - 내 태그 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetTagsMe() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        mockMvc.perform(get("/api/me/tags").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dtoList").exists());
    }

    //me
    @Test
    @DisplayName("GET /api/me/albums/{albumId} - 단일 앨범 조회 유효성 검증 실패 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetAlbumOne_InvalidRequestParams() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int albumId = -10;
        // page, size 값이 1 미만이면 유효성 검증에 실패한다고 가정
        mockMvc.perform(get("/api/me/albums/{albumId}", albumId).header("Authorization", "Bearer " + token)
                        .param("page", "0")
                        .param("size", "-1"))
                .andExpect(status().isBadRequest());
    }

    //me
    @Test
    @DisplayName("GET /api/me/tracks - 내 사운드 조회 유효성 검증 실패 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetSoundMe_InvalidRequestParams() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        mockMvc.perform(get("/api/me/tracks").header("Authorization", "Bearer " + token)
                        .param("page", "-1")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    //me
    @Test
    @DisplayName("GET /api/me/albums - 내 앨범 조회 유효성 검증 실패 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetAlbumMe_InvalidRequestParams() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        mockMvc.perform(get("/api/me/albums").header("Authorization", "Bearer " + token)
                        .param("page", "0")
                        .param("size", "-5"))
                .andExpect(status().isBadRequest());
    }

    //me
    @Test
    @DisplayName("GET /api/me/tags - 내 태그 조회 유효성 검증 실패 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetTagsMe_InvalidRequestParams() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        mockMvc.perform(get("/api/me/tags").header("Authorization", "Bearer " + token)
                        .param("page", "0")
                        .param("size", "-1"))
                .andExpect(status().isBadRequest());
    }

    //admin

    @Test
    @DisplayName("POST /api/admin/tags - 태그 생성 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testCreateTag() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        TagsDTO tagsDto = new TagsDTO();
        tagsDto.setMood(List.of("happyadmintesttest"));
        tagsDto.setGenre(List.of("popadmintesttest"));
        tagsDto.setInstrument(List.of("guitaradmintesttest"));

        mockMvc.perform(post("/api/admin/tags")
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    // PATCH /tags/instruments/{beforeName}
    @Test
    @DisplayName("PATCH /api/admin/tags/instruments/{beforeName} - 악기 태그 스펠링 수정 (유효한 값)")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testUpdateInstrumentTagSpelling_Valid() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        String beforeName = "guitar";
        String afterName = "piano223"; // 모두 소문자, 길이 2~50, 정규식 충족
        TagsDTO afterNameDTO = new TagsDTO();
        afterNameDTO.setInstrument(List.of(afterName));

        mockMvc.perform(patch("/api/admin/tags/instruments/{beforeName}", beforeName)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(afterNameDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("PATCH /api/admin/tags/instruments/{beforeName} - 악기 태그 스펠링 수정 (유효하지 않은 값)")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testUpdateInstrumentTagSpelling_Invalid() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        String beforeName = "piano";      // 길이 1 → 최소 2 미만
        String afterName = "Pi@no";    // 대문자 및 특수문자 '@' 포함 → 정규식 불일치
        TagsDTO afterNameDTO = new TagsDTO();
        afterNameDTO.setInstrument(List.of(afterName));

        mockMvc.perform(patch("/api/admin/tags/instruments/{beforeName}", beforeName)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(afterNameDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /api/admin/tags/moods/{beforeName} - JWT 기반 인증 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testUpdateMoodTagSpelling_WithJWT() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        // 🔹 태그 업데이트 요청 데이터
        String beforeName = "happy";
        String afterName = "joyful223";
        TagsDTO afterNameDTO = new TagsDTO();
        afterNameDTO.setMood(List.of(afterName));

        // 🔹 JWT를 Authorization 헤더에 포함하여 요청 보내기
        mockMvc.perform(patch("/api/admin/tags/moods/{beforeName}", beforeName)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(afterNameDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }


    @Test
    @DisplayName("PATCH /api/admin/tags/moods/{beforeName} - 무드 태그 스펠링 수정 (유효하지 않은 값)")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testUpdateMoodTagSpelling_Invalid() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        String beforeName = "happy";       // 너무 짧음
        String afterName = "J0y!ful";    // 대문자, 특수문자 포함
        TagsDTO afterNameDTO = new TagsDTO();
        afterNameDTO.setMood(List.of(afterName));

        mockMvc.perform(patch("/api/admin/tags/moods/{beforeName}", beforeName)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(afterNameDTO)))
                .andExpect(status().isBadRequest());
    }

    // PATCH /tags/genres/{beforeName}
    @Test
    @DisplayName("PATCH /api/admin/tags/genres/{beforeName} - 장르 태그 스펠링 수정 (유효한 값)")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testUpdateGenreTagSpelling_Valid() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        String beforeName = "disco";
        String afterName = "metal223";
        TagsDTO afterNameDTO = new TagsDTO();
        afterNameDTO.setGenre(List.of(afterName));

        mockMvc.perform(patch("/api/admin/tags/genres/{beforeName}", beforeName)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(afterNameDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("PATCH /api/admin/tags/genres/{beforeName} - 장르 태그 스펠링 수정 (유효하지 않은 값)")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testUpdateGenreTagSpelling_Invalid() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        String beforeName = "edm";      // 길이 부족
        String afterName = "Metal@";   // 대문자, 특수문자 '@' 포함
        TagsDTO afterNameDTO = new TagsDTO();
        afterNameDTO.setGenre(List.of(afterName));

        mockMvc.perform(patch("/api/admin/tags/genres/{beforeName}", beforeName)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(afterNameDTO)))
                .andExpect(status().isBadRequest());
    }

    // POST /tags/{musicId} → 링크 태그 업데이트
    @Test
    @DisplayName("POST /api/admin/tags/{musicId} - 링크 태그 업데이트 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testAdminUpdateLinkTags() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int musicId = 15;
        TagsDTO tagsDto = new TagsDTO();
        tagsDto.setMood(List.of("chill"));
        tagsDto.setGenre(List.of("disco"));
        tagsDto.setInstrument(List.of("piano"));

        mockMvc.perform(post("/api/admin/tags/{musicId}", musicId)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    // ------------------- 사운드/앨범 관련 -------------------

    // DELETE /albums/{albumId} - 앨범 삭제
    @Test
    @DisplayName("DELETE /api/admin/albums/{albumId} - 앨범 삭제 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testDeleteAlbum() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int albumId = 10;

        mockMvc.perform(delete("/api/admin/albums/{albumId}", albumId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    // DELETE /tracks/{musicId} - 음악 삭제
    @Test
    @DisplayName("DELETE /api/admin/tracks/{musicId} - 음악 삭제 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testDeleteMusic() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int musicId = 15;

        mockMvc.perform(delete("/api/admin/tracks/{musicId}", musicId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    // PATCH /albums/{albumId} - 앨범 업데이트
    @Test
    @DisplayName("PATCH /api/admin/albums/{albumId} - 앨범 업데이트 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testAdminUpdateAlbum() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int albumId = 10;
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setAlbumArtPath("test-image");
        albumDTO.setAlbumName("test-albumName");
        albumDTO.setDescription("test-albumDescription");

        mockMvc.perform(patch("/api/admin/albums/{albumId}", albumId)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    // PATCH /albums/{albumId}/verify - 앨범 검증 업데이트
    @Test
    @DisplayName("PATCH /api/admin/albums/{albumId}/verify - 앨범 검증 업데이트 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testUpdateVerifyAlbum() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int albumId = 10;

        mockMvc.perform(patch("/api/admin/albums/{albumId}/verify", albumId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    // GET /albums/verify - 검증 대기 앨범 목록 조회
    @Test
    @DisplayName("GET /api/admin/albums/verify - 검증 대기 앨범 목록 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testReadVerifyAlbum() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        mockMvc.perform(get("/api/admin/albums/verify").header("Authorization", "Bearer " + token)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dtoList").exists());
    }

    // GET /albums/{userId}/title/{albumId}/verify - 특정 앨범 검증 조회
    @Test
    @DisplayName("GET /api/admin/albums/{userId}/title/{albumId}/verify - 특정 앨범 검증 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testReadVerifyAlbumOne() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int userId = 2;
        int albumId = 10;

        mockMvc.perform(get("/api/admin/albums/{userId}/title/{albumId}/verify", userId, albumId).header("Authorization", "Bearer " + token)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dtoList").exists());
    }

    // PATCH /tracks/{musicId} - 음악 업데이트
    @Test
    @DisplayName("PATCH /api/admin/tracks/{musicId} - 음악 업데이트 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testAdminUpdateMusic() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int musicId = 15;
        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setFilePath("test-music");
        musicDTO.setDescription("test-musicDescription");
        musicDTO.setTitle("test-title");

        mockMvc.perform(patch("/api/admin/tracks/{musicId}", musicId)
                        .header("Authorization", "Bearer " + token) // 🔥 JWT 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(musicDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    // GET /tracks/{userId}/{musicId} - 단일 사운드 조회 (관리자용)
    @Test
    @DisplayName("GET /api/admin/tracks/{userId}/{musicId} - 단일 사운드 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testAdminGetSoundOne() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int userId = 2;
        int musicId = 15;

        mockMvc.perform(get("/api/admin/tracks/{userId}/{musicId}", userId, musicId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dto").exists());
    }

    // GET /albums/{userId}/{albumId} - 단일 앨범 조회 (관리자용)
    @Test
    @DisplayName("GET /api/admin/albums/{userId}/{albumId} - 단일 앨범 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testAdminGetAlbumOne() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        int userId = 2;
        int albumId = 10;

        mockMvc.perform(get("/api/admin/albums/{userId}/{albumId}", userId, albumId).header("Authorization", "Bearer " + token)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dtoList").exists());
    }

    // GET /tracks - 사운드 목록 조회 (관리자용)
    @Test
    @DisplayName("GET /api/admin/tracks - 사운드 목록 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetTracks() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        mockMvc.perform(get("/api/admin/tracks").header("Authorization", "Bearer " + token)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dtoList").exists());
    }

    // GET /albums - 앨범 목록 조회 (관리자용)
    @Test
    @DisplayName("GET /api/admin/albums - 앨범 목록 조회 통합 테스트")
    @WithUserDetails("ddjsjs12@naver.com")
    public void testGetAlbums() throws Exception {
        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

        // JWT 생성 (UserDetailsDTO에서 정보 추출)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());  // 이메일
        claims.put("userId", userDetails.getUserDTO().getUserId()); // 유저 ID
        claims.put("nickname", userDetails.getUserDTO().getNickname()); // 닉네임
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        int time = 2;
        String token = jwtUtil.generateToken(claims, time);

        mockMvc.perform(get("/api/admin/albums").header("Authorization", "Bearer " + token)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dtoList").exists());
    }
}
