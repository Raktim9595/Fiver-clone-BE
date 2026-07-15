package com.raktim.fiverclone.country;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.country.controller.CountryController;
import com.raktim.fiverclone.country.service.CountryService;
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

@WebMvcTest(CountryController.class)
public class CountryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CountryService countryService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("""
            When called GET /api/info/country When called,
            Then it should hit the respective service method and return proper data
            """)
    public void test_findAllCountries() throws Exception {
        mockMvc.perform(get("/api/info/country")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(countryService, times(1)).findAll();
    }
}
