package com.raktim.fiverclone.fileUpload.service;

import com.raktim.fiverclone.common.exceptions.BusinessException;
import com.raktim.fiverclone.fileUpload.dto.CompleteFileUploadResponseDto;
import com.raktim.fiverclone.fileUpload.dto.FileUploadDto;
import com.raktim.fiverclone.fileUpload.dto.SearchFileRequestDto;
import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import com.raktim.fiverclone.fileUpload.repo.FileUploadRepo;
import com.raktim.fiverclone.fileUpload.utils.FileStatus;
import com.raktim.fiverclone.fileUpload.utils.FileType;
import com.raktim.fiverclone.fileUpload.utils.FileUploadMapper;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilePersistenceService {

    private static final Logger log =
            LoggerFactory.getLogger(FilePersistenceService.class);

    private final FileUploadRepo fileUploadRepo;
    private final UserService userService;
    private final FileUploadMapper fileUploadMapper;

    @Transactional
    public UserFileEntity createUploadingFile(
            FileUploadDto fileUploadDto,
            String key
    ) {
        UserEntity user =
                userService.findUserByIdOrThrow(
                        fileUploadDto.userId()
                );

        UserFileEntity newEntity =
                fileUploadMapper.toEntity(
                        fileUploadDto,
                        FileStatus.UPLOADING,
                        key,
                        user
                );

        return fileUploadRepo.save(newEntity);
    }

    @Transactional(readOnly = true)
    public List<FileSearchResult> searchFiles(
            SearchFileRequestDto dto
    ) {
        log.info("Getting files for details {}", dto);
        UserEntity user =
                userService.findUserByIdOrThrow(dto.userId());

        log.info(
                "Getting files for user {} and status {} and type {}",
                user.getId(),
                dto.status(),
                dto.type()
        );

        return fileUploadRepo
                .findAllByUser_IdAndStatusAndType(
                        dto.userId(),
                        dto.status(),
                        dto.type()
                )
                .stream()
                .map(file ->
                        new FileSearchResult(
                                file.getId(),
                                file.getS3Key(),
                                file.getType()
                        )
                )
                .toList();
    }

    @Transactional
    public CompleteFileUploadResponseDto completeFileUpload(
            UUID id,
            UUID userId,
            FileStatus fileStatus
    ) {
        log.info(
                "Completing file upload {} with status of {}",
                id,
                fileStatus
        );

        UserFileEntity savedFile =
                findByIdAndUserIdOrThrow(id, userId);

        verifyStatusBeforeUpdate(savedFile);

        savedFile.setStatus(fileStatus);
        UserFileEntity updatedFile =
                fileUploadRepo.save(savedFile);

        log.info(
                "Completed file upload {} with status of {}",
                id,
                fileStatus
        );

        return fileUploadMapper
                .toCompleteFileUploadResponseDto(updatedFile);
    }

    private void verifyStatusBeforeUpdate(
            UserFileEntity userFile
    ) {
        if (!userFile.getStatus().equals(FileStatus.UPLOADING)) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_FILE_STATUS",
                    "Only file with status UPLOADING can be updated"
            );
        }
    }

    @Transactional(readOnly = true)
    public UserFileEntity findByIdAndUserIdOrThrow(
            UUID id,
            UUID userId
    ) {
        return fileUploadRepo
                .findByIdAndUserId(id, userId)
                .orElseThrow(
                        () -> new BusinessException(
                                HttpStatus.NOT_FOUND,
                                "USER_FILE_NOT_FOUND",
                                "File %s for user %s not found"
                                        .formatted(id, userId)
                        )
                );
    }

    @Transactional(readOnly = true)
    public UserFileEntity findByIdOrThrow(UUID id) {
        return fileUploadRepo
                .findById(id)
                .orElseThrow(
                        () -> new BusinessException(
                                HttpStatus.NOT_FOUND,
                                "FILE_NOT_FOUND",
                                "File %s not found".formatted(id)
                        )
                );
    }

    @Transactional(readOnly = true)
    public FileDeletionResult prepareFileDeletion(
            UUID id,
            UUID userId
    ) {
        UserFileEntity file =
                fileUploadRepo
                        .findById(id)
                        .orElseThrow(
                                () -> new BusinessException(
                                        HttpStatus.NOT_FOUND,
                                        "FILE_NOT_FOUND",
                                        "File %s not found"
                                                .formatted(id)
                                )
                        );
        if (!file.getUser().getId().equals(userId)) {
            throw new BusinessException(
                    HttpStatus.FORBIDDEN,
                    "FORBIDDEN_TO_ACCESS",
                    "You are not allowed to delete the file."
            );
        }
        return new FileDeletionResult(
                file.getId(),
                file.getS3Key()
        );
    }

    @Transactional
    public void deletePreparedFile(UUID id) {
        UserFileEntity file =
                fileUploadRepo
                        .findById(id)
                        .orElseThrow(
                                () -> new BusinessException(
                                        HttpStatus.NOT_FOUND,
                                        "FILE_NOT_FOUND",
                                        "File %s not found"
                                                .formatted(id)
                                )
                        );

        fileUploadRepo.delete(file);
    }

    public record FileSearchResult(
            UUID id,
            String s3Key,
            FileType type
    ) {
    }

    public record FileDeletionResult(
            UUID id,
            String s3Key
    ) {
    }
}