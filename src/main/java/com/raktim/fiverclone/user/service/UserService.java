package com.raktim.fiverclone.user.service;

import com.raktim.fiverclone.common.DTO.PaginatedResponseDto;
import com.raktim.fiverclone.user.DTO.UserDTO;
import com.raktim.fiverclone.user.DTO.UserListResponseDto;
import com.raktim.fiverclone.user.DTO.UserResponseDTO;
import com.raktim.fiverclone.user.model.UserEntity;

import java.util.UUID;

public interface UserService {
    UserResponseDTO createUser(UserDTO userDTO);
    UserResponseDTO findUserById(UUID id);
    PaginatedResponseDto<UserListResponseDto> findAllUsers(Integer pageNumber, Integer pageSize);
    UserEntity findUserByIdOrThrow(UUID id);
}
