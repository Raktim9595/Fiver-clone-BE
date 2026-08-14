package com.raktim.fiverclone.seller.controller;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.seller.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.seller.service.sellerApplication.SellerApplicationService;
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

@WebMvcTest(SellerController.class)
public class SellerControllerTest {
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

    @Test
    @DisplayName("When called POST on /api/seller, And no issues Then it should call proper method")
    public void testAddSeller() throws Exception  {
        UUID userId = UUID.randomUUID();
        StartSellerApplicationRequestDto requestDto =
                new StartSellerApplicationRequestDto(userId);

        mockMvc.perform(post("/api/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(sellerApplicationService).startSellerApplication(requestDto);
    }
}
