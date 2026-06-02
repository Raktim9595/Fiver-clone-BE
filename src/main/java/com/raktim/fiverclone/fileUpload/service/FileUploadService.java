package com.raktim.fiverclone.fileUpload.service;

import com.raktim.fiverclone.fileUpload.dto.*;
import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import com.raktim.fiverclone.fileUpload.utils.FileStatus;

import java.util.List;
import java.util.UUID;

public interface FileUploadService {
    GetUploadUrlResponseDto getUploadUrl(FileUploadDto fileUploadDto);
    CompleteFileUploadResponseDto completeFileUpload(UUID id, UUID userId, FileStatus fileStatus);
    UserFileEntity findByIdAndUserIdOrThrow(UUID id, UUID userId);
    List<SearchFileResponseDto> searchFile(SearchFileRequestDto dto);
    void deleteFile(UUID id, UUID userId);
}
