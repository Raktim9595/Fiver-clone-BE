package com.raktim.fiverclone.timezone;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.timezone.controller.TimezoneController;
import com.raktim.fiverclone.timezone.service.TimezoneService;
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

@WebMvcTest(TimezoneController.class)
public class TimezoneControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TimezoneService timezoneService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("""
            When called GET /api/info/timezone When called,
            Then it should hit the respective service method and return proper data
            """)
    public void testFindAllLanguages() throws Exception {
        mockMvc.perform(get("/api/info/timezone")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(timezoneService, times(1)).findAll();
    }
}
