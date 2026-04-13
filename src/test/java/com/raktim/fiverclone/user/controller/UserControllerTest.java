package com.raktim.fiverclone.user.controller;

import com.raktim.fiverclone.common.DTO.SearchRequestDto;
import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.user.DTO.UserDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("When called endpoint /user, than it should create a new user")
    void shouldCreateNewUser() throws Exception {
       UserDTO userDTO = UserTestDataFactory.validUserDto().build();

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());

        verify(userService).createUser(userDTO);
    }

    @Test
    @DisplayName("When called endpoint /user/id, then it should return user with given id.")
    void shouldReturnUserWithGivenId() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/user/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).findUserById(id);
    }

    @Test
    @DisplayName("When called endpoint /users/search then it should find paginated users according to filter")
    void shouldPaginatedSearch() throws Exception {
        SearchRequestDto searchRequestDto = new SearchRequestDto(0, 10);
        mockMvc.perform(post("/api/users/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequestDto)))
                .andExpect(status().isOk());

        verify(userService).findAllUsers(0, 10);
    }
}
