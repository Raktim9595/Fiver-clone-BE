package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileRequestDto;
import com.raktim.fiverclone.sellerApplication.service.sellerPersonalProfile.SellerPersonalProfileService;
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

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellerPersonalProfileController.class)
public class SellerPersonalProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SellerPersonalProfileService service;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("""
            When called POST on /api/seller-application/{id}/personal-profile,
            And there is no error,
            Then it should invoke correct methods and return proper response
            """)
    public void shouldInvokeCorrectMethods() throws Exception {
        UUID applicationId = UUID.randomUUID();
        SellerPersonalProfileRequestDto dto = SellerApplicationTestData
                .validSellerPersonalProfileRequestDto()
                .build();

        mockMvc.perform(post("/api/seller-application/{id}/personal-profile", applicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());

        verify(service, times(1)).create(applicationId, dto);
    }
}
