package com.soundbrew.soundbrew.contorller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soundbrew.soundbrew.controller.UserController;
import com.soundbrew.soundbrew.dto.ApiResponse;
import com.soundbrew.soundbrew.dto.UserDTO;
import com.soundbrew.soundbrew.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class) // JPA 관련 빈을 Mock으로 대체
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_ShouldReturnBadRequest_WhenPasswordInvalid() throws Exception {
        // given
        UserDTO userDTO = UserDTO.builder()
                .name("Test User")
                .nickname("tester")
                .password("123")
                .phonenumber("01012345678")
                .email("test@example.com")
                .build();

        ApiResponse mockResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "비밀번호 규격에 맞지 않습니다.");
        given(userService.registerUser(any(UserDTO.class))).willReturn(mockResponse);

        // when & then
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("비밀번호 규격에 맞지 않습니다."));
    }

    
}
