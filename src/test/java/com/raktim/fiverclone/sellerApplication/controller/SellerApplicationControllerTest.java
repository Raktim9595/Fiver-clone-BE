package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.sellerApplication.dto.SellerCertificationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerEducationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPortfolioRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.sellerApplication.service.SellerCertificationService;
import com.raktim.fiverclone.sellerApplication.service.SellerEducationService;
import com.raktim.fiverclone.sellerApplication.service.SellerPortfolioService;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellerApplicationController.class)
public class SellerApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SellerApplicationService sellerApplicationService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @MockitoBean
    private SellerEducationService sellerEducationService;

    @MockitoBean
    private SellerPortfolioService sellerPortfolioService;

    @MockitoBean
    private SellerCertificationService sellerCertificationService;

    @Test
    @DisplayName("When called POST on /api/seller-application, And no issues Then it should call proper method")
    public void testAddSeller() throws Exception  {
        UUID userId = UUID.randomUUID();
        StartSellerApplicationRequestDto requestDto =
                new StartSellerApplicationRequestDto(userId);

        mockMvc.perform(post("/api/seller-application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(sellerApplicationService).startSellerApplication(requestDto);
    }

    @Test
    @DisplayName("""
            When called POST on /api/seller-application/{id}/seller-education,
            And there is no error,
            Then it should invoke the respective method and return proper response
            """)
    public void shouldExecuteAddSellerEducation() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        SellerEducationRequestDto dto = SellerApplicationTestData
                .validSellerEducationRequestDto()
                .build();

        mockMvc.perform(post("/api/seller-application/{applicationId}/seller-education", applicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());

        verify(sellerEducationService, times(1)).create(applicationId, dto);
    }

    @Test
    @DisplayName("""
            When called POST on /api/seller-application/{id}/seller-portfolio,
            And there is no error,
            Then it should invoke the respective method and return proper response
            """)
    public void shouldExecuteAddSellerPortfolio() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        SellerPortfolioRequestDto dto = SellerApplicationTestData.validSellerPortfolioRequestDto().build();

        mockMvc.perform(post("/api/seller-application/{id}/seller-portfolio", applicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());

        verify(sellerPortfolioService, times(1)).create(applicationId, dto);
    }

    @Test
    @DisplayName("""
            """)
    public void shouldExecuteAddSellerCertification() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        SellerCertificationRequestDto dto = SellerApplicationTestData.validSellerCertificationRequestDto().build();

        mockMvc.perform(post("/api/seller-application/{id}/seller-certification", applicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());

        verify(sellerCertificationService, times(1)).create(applicationId, dto);
    }
}
