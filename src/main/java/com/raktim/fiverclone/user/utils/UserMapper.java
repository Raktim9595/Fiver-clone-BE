package com.raktim.fiverclone.user.utils;

import com.raktim.fiverclone.user.DTO.UserDTO;
import com.raktim.fiverclone.user.DTO.UserListResponseDto;
import com.raktim.fiverclone.user.DTO.UserResponseDTO;
import com.raktim.fiverclone.user.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserDTO dto);
    UserResponseDTO toDetailResponseDTO(UserEntity user);
    UserListResponseDto toListResponseDTO(UserEntity user);
}
