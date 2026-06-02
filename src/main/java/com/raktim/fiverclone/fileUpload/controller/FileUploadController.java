package com.raktim.fiverclone.fileUpload.controller;

import com.raktim.fiverclone.fileUpload.dto.*;
import com.raktim.fiverclone.fileUpload.service.FileUploadService;
import com.raktim.fiverclone.fileUpload.utils.FileStatus;
import com.raktim.fiverclone.user.model.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "Endpoints for file uploads")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @PostMapping("/upload-url")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Get a signUrl to upload file",
            description = "Endpoint to generate the uploadUrl to FE for saving file in the s3"
    )
    public ResponseEntity<GetUploadUrlResponseDto> getUploadUrl(
            @Valid @RequestBody FileUploadDto fileUploadDto
            ) {
        GetUploadUrlResponseDto result = fileUploadService.getUploadUrl(fileUploadDto);
        return  ResponseEntity.ok(result);
    }

    @PatchMapping("/{fileId}/complete")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Complete the file upload",
            description = "This end-point change the file status to UPLOADED from UPLOADING"
    )
    public ResponseEntity<CompleteFileUploadResponseDto> completeFileWithStatusOfUploaded(
            @PathVariable UUID fileId,
            @AuthenticationPrincipal UserPrincipal user
            ) {
        return ResponseEntity.ok(
                fileUploadService.completeFileUpload(
                        fileId,
                        user.getId(),
                        FileStatus.UPLOADED
                )
        );
    }

    @PatchMapping("/{fileId}/failed")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Complete the file upload",
            description = "This end-point change the file status to FAILED from UPLOADING"
    )
    public ResponseEntity<CompleteFileUploadResponseDto> completeFileWithStatusOfFailed(
            @PathVariable UUID fileId,
            Authentication authentication
    ) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        assert userPrincipal != null;

        return ResponseEntity.ok(
                fileUploadService.completeFileUpload(
                        fileId,
                        userPrincipal.getId(),
                        FileStatus.FAILED
                )
        );
    }

    @PostMapping("/search")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Search the file",
            description = "Endpoint to get the read-url for browser"
    )
    public ResponseEntity<List<SearchFileResponseDto>> searchFile(
            @Valid @RequestBody SearchFileRequestDto searchFileRequestDto
    ) {
        return ResponseEntity.ok(fileUploadService.searchFile(searchFileRequestDto));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Delete the file by id",
            description = "Endpoint to find the file and delete if the file exists"
    )
    public ResponseEntity<String> deleteFile(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        fileUploadService.deleteFile(id, user.getId());
        return ResponseEntity.ok("Successfully deleted the file");
    }
}
