package com.raktim.fiverclone.fileUpload.controller;

import com.raktim.fiverclone.fileUpload.dto.FileUploadDto;
import com.raktim.fiverclone.fileUpload.dto.GetUploadUrlResponseDto;
import com.raktim.fiverclone.fileUpload.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file-upload")
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "Endpoints for file uploads")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @PostMapping("/user-profile")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Get a signUrl to upload file",
            description = "Endpoint to generate the uploadUrl for the user-profile image"
    )
    public ResponseEntity<GetUploadUrlResponseDto> getUploadUrl(
            @Valid @RequestBody FileUploadDto fileUploadDto
            ) {
        GetUploadUrlResponseDto result = fileUploadService.getUploadUrl(fileUploadDto);
        return  ResponseEntity.ok(result);
    }
}
