package com.raktim.fiverclone.fileUpload.service;

import com.raktim.fiverclone.fileUpload.dto.CompleteFileUploadResponseDto;
import com.raktim.fiverclone.fileUpload.dto.FileUploadDto;
import com.raktim.fiverclone.fileUpload.dto.GetUploadUrlResponseDto;
import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import com.raktim.fiverclone.fileUpload.utils.FileStatus;

import java.util.UUID;

public interface FileUploadService {
    GetUploadUrlResponseDto getUploadUrl(FileUploadDto fileUploadDto);
    CompleteFileUploadResponseDto completeFileUpload(UUID id, UUID userId, FileStatus fileStatus);
    UserFileEntity findByIdAndUserIdOrThrow(UUID id, UUID userId);
}
