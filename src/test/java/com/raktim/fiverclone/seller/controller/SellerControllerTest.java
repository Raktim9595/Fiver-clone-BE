package com.raktim.fiverclone.seller.controller;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.mocks.SellerTestData;
import com.raktim.fiverclone.seller.dto.SellerDto;
import com.raktim.fiverclone.seller.service.SellerService;
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
    private SellerService sellerService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("When called POST on /api/seller, And no issues Then it should call proper method")
    public void testAddSeller() throws Exception  {
        SellerDto sellerDto = SellerTestData.validSellerDto().build();

        mockMvc.perform(post("/api/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sellerDto)))
                .andExpect(status().isCreated());

        verify(sellerService).addSeller(sellerDto);
    }
}
