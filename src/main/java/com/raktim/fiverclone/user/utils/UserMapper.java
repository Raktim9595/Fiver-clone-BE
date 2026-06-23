package com.raktim.fiverclone.user.utils;

import com.raktim.fiverclone.user.DTO.UpdateUserDto;
import com.raktim.fiverclone.user.DTO.UserDTO;
import com.raktim.fiverclone.user.DTO.UserListResponseDto;
import com.raktim.fiverclone.user.DTO.UserResponseDTO;
import com.raktim.fiverclone.user.model.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "files", ignore = true)
    UserEntity toEntity(UserDTO dto);
    UserResponseDTO toDetailResponseDTO(UserEntity user);
    UserListResponseDto toListResponseDTO(UserEntity user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "files", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(
            UpdateUserDto updateUserDto,
            @MappingTarget UserEntity user
    );
}
