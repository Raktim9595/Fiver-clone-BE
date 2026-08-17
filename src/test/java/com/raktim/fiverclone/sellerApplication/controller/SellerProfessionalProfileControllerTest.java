package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.sellerApplication.dto.SellerProfessionalProfileRequestDto;
import com.raktim.fiverclone.sellerApplication.service.SellerProfessionalProfileService;
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

@WebMvcTest(SellerProfessionalProfileController.class)
public class SellerProfessionalProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @MockitoBean
    private SellerProfessionalProfileService service;

    @Test
    @DisplayName("""
            When called POST on /api/seller-application/{id}/professional-profile,
            And there is no error,
            Then it should invoke correct methods and return proper response
            """)
    public void shouldReturnProperResponse() throws Exception {
        UUID applicationId = UUID.randomUUID();
        SellerProfessionalProfileRequestDto dto = SellerApplicationTestData
                .validSellerProfessionalProfileRequestDto()
                .build();

        mockMvc.perform(post("/api/seller-application/{id}/professional-profile", applicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());

        verify(service, times(1)).createProfessionalProfile(applicationId, dto);
    }
}
