package com.raktim.fiverclone.fileUpload;

import com.raktim.fiverclone.common.exceptions.BusinessException;
import com.raktim.fiverclone.common.utils.GenerateUploadUrlResult;
import com.raktim.fiverclone.common.utils.S3Service;
import com.raktim.fiverclone.fileUpload.dto.FileUploadDto;
import com.raktim.fiverclone.fileUpload.dto.GetUploadUrlResponseDto;
import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import com.raktim.fiverclone.fileUpload.repo.FileUploadRepo;
import com.raktim.fiverclone.fileUpload.service.FileUploadServiceImpl;
import com.raktim.fiverclone.fileUpload.utils.FileStatus;
import com.raktim.fiverclone.fileUpload.utils.FileUploadMapper;
import com.raktim.fiverclone.mocks.FileUploadTestData;
import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.service.UserService;
import com.raktim.fiverclone.utils.ExceptionTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class FileUploadServiceImplTest {
    @Mock
    private FileUploadRepo fileUploadRepo;

    @Mock
    private UserService userService;

    @Mock
    private S3Service s3Service;

    private FileUploadServiceImpl fileUploadService;

    @BeforeEach
    public void setUp() {
        FileUploadMapper mapper = Mappers.getMapper(FileUploadMapper.class);
        fileUploadService = new FileUploadServiceImpl(
                fileUploadRepo,
                s3Service,
                userService,
                mapper
        );
    }

    @Test
    @DisplayName("""
            Given getUploadUrl, When called,
            And the method throws exceptions due to business logic,
            Then it should throw proper exceptions
            """)
    public void getUploadUrl_business_exception() {
        UUID userId = UUID.randomUUID();

        FileUploadDto mockFileUploadDto = FileUploadTestData
                .validFileUploadDto()
                .userId(userId)
                .build();

        when(userService.findUserByIdOrThrow(userId)).thenThrow(
                new BusinessException(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                "user not found"
        ));

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                "user not found",
                () -> fileUploadService.getUploadUrl(mockFileUploadDto)
        );

        verify(userService, times(1)).findUserByIdOrThrow(userId);
        verify(s3Service, never()).generateUploadUrl(any(), any());
    }

    @Test
    @DisplayName("""
            Given getUploadUrl, When called,
            And the s3 service is unable to generate the uploadUrl,
            Then it should throw proper exception message
            """)
    public void getUploadUrl_exception() {
        UUID userId = UUID.randomUUID();
        UUID fileId = UUID.randomUUID();

        FileUploadDto mockFileUploadDto = FileUploadTestData
                .validFileUploadDto()
                .userId(userId)
                .build();

        UserEntity mockUserEntity = UserTestDataFactory.validUserEntity()
                .build();
        mockUserEntity.setId(userId);

        when(userService.findUserByIdOrThrow(userId)).thenReturn(mockUserEntity);

        UserFileEntity mockUploadedFile = FileUploadTestData
                .validUserFileEntity()
                .user(mockUserEntity)
                .build();

        mockUploadedFile.setId(fileId);

        when(fileUploadRepo.save(any(UserFileEntity.class))).thenReturn(mockUploadedFile);

        when(s3Service.generateUploadUrl(any(), any())).thenThrow(
                new RuntimeException("Just a random exception")
        );

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "UNEXPECTED_ERROR",
                "Just a random exception",
                () -> fileUploadService.getUploadUrl(mockFileUploadDto)
        );

        verify(userService, times(1)).findUserByIdOrThrow(userId);
        verify(fileUploadRepo, times(1)).save(any(UserFileEntity.class));
    }

    @Test
    @DisplayName("""
            Given getUploadUrl, When called,
            And unable to save the file to the db,
            Then it should throw proper exception message
            """)
    public void getUploadUrl_repo_exception() {
        UUID userId = UUID.randomUUID();
        UUID fileId = UUID.randomUUID();

        FileUploadDto mockFileUploadDto = FileUploadTestData
                .validFileUploadDto()
                .userId(userId)
                .build();

        UserEntity mockUserEntity = UserTestDataFactory
                .validUserEntity()
                .build();
        mockUserEntity.setId(userId);

        when(userService.findUserByIdOrThrow(userId)).thenReturn(mockUserEntity);

        UserFileEntity mockUploadedFile = FileUploadTestData
                .validUserFileEntity()
                .user(mockUserEntity)
                .build();
        mockUploadedFile.setId(fileId);

        when(fileUploadRepo.save(any(UserFileEntity.class))).thenThrow(
                new RuntimeException("Unable to save data")
        );

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "UNEXPECTED_ERROR",
                "Unable to save data",
                () -> fileUploadService.getUploadUrl(mockFileUploadDto)
        );

        verify(userService, times(1)).findUserByIdOrThrow(userId);
        verify(fileUploadRepo, times(1)).save(any(UserFileEntity.class));
        verify(s3Service, never()).generateUploadUrl(any(), any());
    }

    @Test
    @DisplayName("""
            Given getUploadUrl When called,
            And every step is a success,
            Than it should return proper response
            """)
    public void getUploadUrl_success() {
        UUID userId = UUID.randomUUID();
        UUID fileId = UUID.randomUUID();


        FileUploadDto mockFileUploadDto = FileUploadTestData
                .validFileUploadDto()
                .userId(userId)
                .build();

        UserEntity mockUserEntity = UserTestDataFactory
                .validUserEntity()
                .build();
        mockUserEntity.setId(userId);

        when(userService.findUserByIdOrThrow(userId)).thenReturn(mockUserEntity);

        UserFileEntity mockUploadedFile = FileUploadTestData
                .validUserFileEntity()
                .user(mockUserEntity)
                .build();
        mockUploadedFile.setId(fileId);

        Instant now = Instant.now();

        GenerateUploadUrlResult generateUploadUrlResult = new GenerateUploadUrlResult(
                "https://upload-file",
                now
        );

        when(fileUploadRepo.save(any(UserFileEntity.class))).thenReturn(mockUploadedFile);
        when(s3Service.generateUploadUrl(any(), any())).thenReturn(generateUploadUrlResult);

        GetUploadUrlResponseDto result = fileUploadService.getUploadUrl(mockFileUploadDto);
        assertInstanceOf(GetUploadUrlResponseDto.class, result);

        assertAll(
                () -> assertInstanceOf(GetUploadUrlResponseDto.class, result),
                () -> assertEquals("https://upload-file", result.uploadUrl()),
                () -> assertEquals(now, result.expiresAt()),
                () -> assertEquals(fileId, result.id()),
                () -> assertNotNull(result.s3Key()),
                () -> assertEquals(FileStatus.UPLOADING, result.status()),
                () -> verify(userService, times(1)).findUserByIdOrThrow(userId),
                () -> verify(fileUploadRepo, times(1)).save(any(UserFileEntity.class)),
                () -> verify(s3Service, times(1)).generateUploadUrl(any(), any())
        );
    }
}
