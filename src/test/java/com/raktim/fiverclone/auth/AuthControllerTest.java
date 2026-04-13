package com.raktim.fiverclone.auth;

import com.raktim.fiverclone.auth.controller.AuthController;
import com.raktim.fiverclone.auth.service.AuthService;
import com.raktim.fiverclone.common.DTO.auth.SignInRequestDto;
import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.user.DTO.UserDTO;
import com.raktim.fiverclone.user.service.CustomUserDetailService;
import com.raktim.fiverclone.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("When endpoint /signIn is hit, then it should call the proper service")
    public void testSignIn() throws Exception {
        SignInRequestDto signInRequestDto =
                new SignInRequestDto("Raktim", "1234");

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto)))
                .andExpect(status().isOk());

        verify(authService).signIn(signInRequestDto);
        verify(userService, never()).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("When endpoint /signUp is hit")
    public void testSignUp() throws Exception {
        UserDTO userDTO = UserTestDataFactory.validUserDto().build();

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());

        verify(userService).createUser(userDTO);
        verify(authService, never()).signIn(any(SignInRequestDto.class));
    }
}
