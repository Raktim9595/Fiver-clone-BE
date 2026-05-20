package com.raktim.fiverclone.fileUpload.utils;

import com.raktim.fiverclone.fileUpload.dto.FileUploadDto;
import com.raktim.fiverclone.fileUpload.dto.GetUploadUrlResponseDto;
import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import com.raktim.fiverclone.user.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface FileUploadMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "s3Key", source = "key")
    @Mapping(target = "originalFileName", source = "dto.fileName")
    UserFileEntity toEntity(
            FileUploadDto dto,
            FileStatus status,
            String key,
            UserEntity user
    );

    @Mapping(target = "uploadUrl", source = "uploadUrl")
    @Mapping(target = "expiresAt", source = "expiresAt")
    GetUploadUrlResponseDto toUploadUrlResponseDto(
            UserFileEntity userFile,
            String uploadUrl,
            Instant expiresAt
    );
}
