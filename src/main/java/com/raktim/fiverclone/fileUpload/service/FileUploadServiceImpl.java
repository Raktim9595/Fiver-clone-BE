package com.raktim.fiverclone.fileUpload.service;

import com.raktim.fiverclone.common.exceptions.BusinessException;
import com.raktim.fiverclone.common.utils.GenerateUploadUrlResult;
import com.raktim.fiverclone.common.utils.S3Service;
import com.raktim.fiverclone.fileUpload.dto.FileUploadDto;
import com.raktim.fiverclone.fileUpload.dto.GetUploadUrlResponseDto;
import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import com.raktim.fiverclone.fileUpload.repo.FileUploadRepo;
import com.raktim.fiverclone.fileUpload.utils.FileStatus;
import com.raktim.fiverclone.fileUpload.utils.FileUploadMapper;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final FileUploadRepo fileUploadRepo;
    private final S3Service s3Service;
    private final UserService userService;
    private final FileUploadMapper fileUploadMapper;

    private static final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Override
    @Transactional
    public GetUploadUrlResponseDto getUploadUrl(FileUploadDto fileUploadDto) {
     try {
         log.info("Generating sign url for the file upload {}", fileUploadDto);

         String key = this.generateS3Key(fileUploadDto);
         UserEntity user = userService.findUserByIdOrThrow(fileUploadDto.userId());

         UserFileEntity newEntity = fileUploadMapper.toEntity(
                 fileUploadDto,
                 FileStatus.UPLOADING,
                 key,
                 user
         );

         UserFileEntity savedFile = fileUploadRepo.save(newEntity);
         GenerateUploadUrlResult uploadUrlResult = s3Service.generateUploadUrl(key, fileUploadDto.contentType());

         return fileUploadMapper.toUploadUrlResponseDto(
                 savedFile,
                 uploadUrlResult.uploadUrl(),
                 uploadUrlResult.expiresAt()
         );
     } catch (BusinessException ex) {
         throw ex;
     }
     catch (Exception ex) {
         throw new BusinessException(
                 HttpStatus.INTERNAL_SERVER_ERROR,
                 "UNEXPECTED_ERROR",
                 ex.getMessage()
         );
     }
    }


    private String generateS3Key(FileUploadDto dto) {
        String cleanFileName = sanitizeFileName(dto.fileName());
        return "users/%s/%s/%s-%s".formatted(
                dto.userId(),
                dto.type().name().toLowerCase(),
                UUID.randomUUID().toString(),
                cleanFileName
        );
    }

    private String sanitizeFileName(String fileName) {
        return fileName
                .replaceAll("\\s+", "_")
                .replaceAll("[^a-zA-Z0-9._-]", "");
    }
}
