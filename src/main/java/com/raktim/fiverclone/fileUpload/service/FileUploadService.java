package com.raktim.fiverclone.fileUpload.service;

import com.raktim.fiverclone.fileUpload.dto.FileUploadDto;
import com.raktim.fiverclone.fileUpload.dto.GetUploadUrlResponseDto;

public interface FileUploadService {
    GetUploadUrlResponseDto getUploadUrl(FileUploadDto fileUploadDto);
}
