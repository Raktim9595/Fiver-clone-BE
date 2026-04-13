package com.raktim.fiverclone.auth.service;

import com.raktim.fiverclone.common.DTO.auth.SignInRequestDto;
import com.raktim.fiverclone.common.DTO.auth.SignInResponseDto;
import com.raktim.fiverclone.common.exceptions.BusinessException;
import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.user.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) {
        log.info("SignInRequestDto: {}", signInRequestDto);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequestDto.username(),
                            signInRequestDto.password()
                    )
            );

            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

            if (userDetails == null) {
                throw new UsernameNotFoundException("User with username " + signInRequestDto.username() + " not found");
            }

            String token = jwtUtil.generateToken(userDetails);
            return new SignInResponseDto(token);
        } catch (BadCredentialsException ex) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "BAD_CREDENTIALS",
                    ex.getMessage()
            );
        } catch (Exception ex) {
            throw new BusinessException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "INTERNAL_SERVER_ERROR",
                    ex.getMessage()
            );
        }
    }

}
