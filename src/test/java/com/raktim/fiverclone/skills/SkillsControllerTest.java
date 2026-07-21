package com.raktim.fiverclone.skills;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.seeds.skills.SkillsController;
import com.raktim.fiverclone.seeds.skills.SkillsService;
import com.raktim.fiverclone.user.service.CustomUserDetailService;
import com.raktim.fiverclone.user.service.UserService;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillsController.class)
public class SkillsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SkillsService skillsService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @Description("""
              When called GET /api/skills When called,
            Then it should hit the respective service method and return proper data
            """)
    public void test_getSkills()  throws Exception {
        mockMvc.perform(get("/api/skills")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(skillsService, times(1)).findAll();
    }
}
