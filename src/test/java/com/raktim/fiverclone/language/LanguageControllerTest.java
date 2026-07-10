package com.raktim.fiverclone.language;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.language.controller.LanguageController;
import com.raktim.fiverclone.language.service.LanguageService;
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

@WebMvcTest(LanguageController.class)
public class LanguageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LanguageService languageService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("""
            When called GET /api/langugae When called,
            Then it should hit the respective service method and return proper data
            """)
    public void testFindAllLanguages() throws Exception {
        mockMvc.perform(get("/api/language")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(languageService, times(1)).findAll();
    }
}
