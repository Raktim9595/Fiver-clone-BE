package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryRequestDto;
import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import com.raktim.fiverclone.sellerApplication.service.sellerApplicationStatusHistory.SellerApplicationStatusHistoryService;
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

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellerApplicationHistoryController.class)
public class SellerApplicationHistoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SellerApplicationStatusHistoryService service;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("When called POST on /api/seller-application/id/status-history, And no issues Then it should call proper method")
    public void shouldInvokeCorrectMethods() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        SellerApplicationHistoryRequestDto requestDto =
                SellerApplicationHistoryRequestDto
                        .builder()
                        .changedBy(userId)
                        .previousStatus(SellerApplicationStatus.DRAFT)
                        .newStatus(SellerApplicationStatus.APPROVED)
                        .reason("Nothing wrong approved")
                        .build();

        mockMvc.perform(post( "/api/seller-application/{applicationId}/status-history", applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(service).createApplicationHistory(applicationId,requestDto);
    }
}
