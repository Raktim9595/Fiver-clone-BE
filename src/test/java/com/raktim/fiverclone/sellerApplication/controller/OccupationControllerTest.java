package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.sellerApplication.service.OccupationService;
import com.raktim.fiverclone.user.service.CustomUserDetailService;
import com.raktim.fiverclone.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OccupationController.class)
public class OccupationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private OccupationService occupationService;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("""
            When hit GET on endpoint /api/occupations,
            And there is no error,
            Then it should return proper response and status code
            """)
    public void shouldReturnProperResponse() throws Exception {
        mockMvc.perform(get("/api/occupations")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(occupationService, times(1)).findAllOccupations();
    }
}
