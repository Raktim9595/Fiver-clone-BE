package com.raktim.fiverclone.fileUpload.service;

import com.raktim.fiverclone.common.utils.GenerateUploadUrlResult;
import com.raktim.fiverclone.common.utils.S3Service;
import com.raktim.fiverclone.common.utils.ServiceExecutor;
import com.raktim.fiverclone.fileUpload.dto.*;
import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import com.raktim.fiverclone.fileUpload.utils.FileStatus;
import com.raktim.fiverclone.fileUpload.utils.FileUploadMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger log =
            LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final FilePersistenceService filePersistenceService;
    private final S3Service s3Service;
    private final FileUploadMapper fileUploadMapper;

    @Override
    public GetUploadUrlResponseDto getUploadUrl(FileUploadDto fileUploadDto) {
        return ServiceExecutor.execute(
                () -> handleUploadUrlGeneration(fileUploadDto)
        );
    }

    @Override
    public List<SearchFileResponseDto> searchFile(
            SearchFileRequestDto dto
    ) {
        return ServiceExecutor.execute(
                () -> handlePresignedUrlGeneration(dto)
        );
    }

    @Override
    public CompleteFileUploadResponseDto completeFileUpload(
            UUID id,
            UUID userId,
            FileStatus fileStatus
    ) {
        return ServiceExecutor.execute(
                () -> filePersistenceService.completeFileUpload(
                        id,
                        userId,
                        fileStatus
                )
        );
    }

    @Override
    public UserFileEntity findByIdAndUserIdOrThrow(
            UUID id,
            UUID userId
    ) {
        return filePersistenceService.findByIdAndUserIdOrThrow(
                id,
                userId
        );
    }

    @Override
    public void deleteFile(UUID id, UUID userId) {
        ServiceExecutor.execute(() -> {
            handleFileDeletion(id, userId);
            return null;
        });
    }

    public UserFileEntity findByIdOrThrow(UUID id) {
        return filePersistenceService.findByIdOrThrow(id);
    }

    private GetUploadUrlResponseDto handleUploadUrlGeneration(
            FileUploadDto fileUploadDto
    ) {
        log.info(
                "Generating sign url for the file upload {}",
                fileUploadDto
        );

        String key = generateS3Key(fileUploadDto);
        UserFileEntity savedFile =
                filePersistenceService.createUploadingFile(
                        fileUploadDto,
                        key
                );
        log.info(
                "Generating upload URL for file upload {} and file key {}",
                fileUploadDto,
                key
        );

        GenerateUploadUrlResult uploadUrlResult =
                s3Service.generateUploadUrl(
                        key,
                        fileUploadDto.contentType()
                );

        return fileUploadMapper.toUploadUrlResponseDto(
                savedFile,
                uploadUrlResult.uploadUrl(),
                uploadUrlResult.expiresAt()
        );
    }

    private List<SearchFileResponseDto> handlePresignedUrlGeneration(
            SearchFileRequestDto dto
    ) {
        log.info("Getting files for details {}", dto);
        List<FilePersistenceService.FileSearchResult> files =
                filePersistenceService.searchFiles(dto);

        log.info(
                "{} files found for user with id {}",
                files.size(),
                dto.userId()
        );

        return files.stream()
                .map(file ->
                        SearchFileResponseDto.builder()
                                .id(file.id())
                                .imageUrl(
                                        s3Service.getImageUrl(
                                                file.s3Key()
                                        )
                                )
                                .type(file.type())
                                .build()
                )
                .toList();
    }

    private void handleFileDeletion(
            UUID id,
            UUID userId
    ) {
        log.info("Deleting file {}", id);
        FilePersistenceService.FileDeletionResult file =
                filePersistenceService.prepareFileDeletion(
                        id,
                        userId
                );
        s3Service.deleteFile(file.s3Key());
        filePersistenceService.deletePreparedFile(file.id());

        log.info("Successfully deleted file {}", id);
    }

    private String generateS3Key(FileUploadDto dto) {
        String cleanFileName =
                sanitizeFileName(dto.fileName());

        return "users/%s/%s/%s-%s".formatted(
                dto.userId(),
                dto.type().name().toLowerCase(),
                UUID.randomUUID(),
                cleanFileName
        );
    }

    private String sanitizeFileName(String fileName) {
        return fileName
                .replaceAll("\\s+", "_")
                .replaceAll("[^a-zA-Z0-9._-]", "");
    }
}