package com.raktim.fiverclone.auth;

import com.raktim.fiverclone.auth.service.AuthService;
import com.raktim.fiverclone.common.DTO.auth.SignInRequestDto;
import com.raktim.fiverclone.common.DTO.auth.SignInResponseDto;
import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.user.model.UserPrincipal;
import com.raktim.fiverclone.utils.ExceptionTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Mock
    private Authentication authentication;

    private final SignInRequestDto signInRequestDto =
            new SignInRequestDto("Raktim", "1234");

    @Test
    @DisplayName("When signIn called, And no issues found, than it should return the JWT token")
    public void testSignIn() {

        UserPrincipal userDetails = new UserPrincipal(
                UserTestDataFactory.validUserEntity().build()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(any())).thenReturn("mock-jwt-token");

        SignInResponseDto response = authService.signIn(signInRequestDto);

        assertInstanceOf(SignInResponseDto.class, response);
        assertEquals("mock-jwt-token", response.token());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication).getPrincipal();
        verify(jwtUtil).generateToken(any(UserPrincipal.class));
    }

    @Test
    @DisplayName("When signIn called, And invalid credentials are passed, Then it throws exception")
    public void testSignInInvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad Credentials"));

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.BAD_REQUEST,
                "BAD_CREDENTIALS",
                "Bad Credentials",
                () -> authService.signIn(signInRequestDto)
        );

        verify(jwtUtil, never()).generateToken(any(UserPrincipal.class));
        verify(authentication, never()).getPrincipal();
    }

    @Test
    @DisplayName("When signIn called, And unexepected runtime exception occurs, Then it should throw proper exception")
    public void testSignInUnexpectedRuntimeException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Database unavailable"));

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Database unavailable",
                () -> authService.signIn(signInRequestDto)
        );

        verify(jwtUtil, never()).generateToken(any(UserPrincipal.class));
        verify(authentication, never()).getPrincipal();

    }
}
