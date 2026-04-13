package com.raktim.fiverclone.auth.controller;

import com.raktim.fiverclone.auth.service.AuthService;
import com.raktim.fiverclone.common.DTO.auth.SignInRequestDto;
import com.raktim.fiverclone.common.DTO.auth.SignInResponseDto;
import com.raktim.fiverclone.user.DTO.UserDTO;
import com.raktim.fiverclone.user.DTO.UserResponseDTO;
import com.raktim.fiverclone.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth management API")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signin")
    @Operation(summary = "SignIn", description = "Sign in user and return authentication JWT token")
    public ResponseEntity<SignInResponseDto> authenticateUser(
            @RequestBody SignInRequestDto signInRequestDto
    ) {
        SignInResponseDto result = authService.signIn(signInRequestDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign Up", description = "Create a new User")
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserDTO userDTO
            ) {
        UserResponseDTO result = userService.createUser(userDTO);
        return ResponseEntity.ok(result);
    }
}
