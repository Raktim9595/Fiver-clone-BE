package com.raktim.fiverclone.fileUpload;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.fileUpload.dto.FileUploadDto;
import com.raktim.fiverclone.fileUpload.service.FileUploadService;
import com.raktim.fiverclone.mocks.FileUploadTestData;
import com.raktim.fiverclone.user.service.CustomUserDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;
import com.raktim.fiverclone.fileUpload.controller.FileUploadController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileUploadController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FileUploadControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FileUploadService fileUploadService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;


    @Test
    @DisplayName("""
            Given endpoint "/api/file-upload/user-profile",
            When called, Then it should generate new signUrl to upload user-profile image
            """)
    public void getUploadUrl() throws Exception {
        UUID userId = UUID.randomUUID();
        FileUploadDto fileUploadDto = FileUploadTestData
                .validFileUploadDto()
                .userId(userId)
                .build();

        mockMvc.perform(
                post("/api/file-upload/user-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fileUploadDto)))
                .andExpect(status().isOk());

        verify(fileUploadService, times(1)).getUploadUrl(fileUploadDto);
    }
}
