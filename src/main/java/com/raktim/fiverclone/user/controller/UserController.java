package com.raktim.fiverclone.user.controller;

import com.raktim.fiverclone.common.DTO.PaginatedResponseDto;
import com.raktim.fiverclone.common.DTO.SearchRequestDto;
import com.raktim.fiverclone.user.DTO.UserDTO;
import com.raktim.fiverclone.user.DTO.UserListResponseDto;
import com.raktim.fiverclone.user.DTO.UserResponseDTO;
import com.raktim.fiverclone.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api")
@AllArgsConstructor
@Tag(name = "Users", description = "User Management API")
public class UserController {
    private final UserService userService;

    @PostMapping("user")
    @Operation(summary = "Create User", description = "Create a single User")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserDTO userDTO
            ) {
        UserResponseDTO user = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("user/{id}")
    @Operation(summary = "Find user by ID", description = "Fetch a single user by UUID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable UUID id) {
        UserResponseDTO user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

   /*  Here we use post mapping because this is gonna be the search query where we do multiple filters, pagination
     and sorting
   * */
    @PostMapping("users/search")
    @Operation(summary = "Fetch all users", description = "Fetch all the available users from db")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PaginatedResponseDto<UserListResponseDto>> findAllUsers(
            @Valid @RequestBody SearchRequestDto dto
            ) {
        PaginatedResponseDto<UserListResponseDto> users = userService.findAllUsers(
                dto.pageNumber(),
                dto.pageSize()
        );
        return ResponseEntity.ok(users);
    }
}
