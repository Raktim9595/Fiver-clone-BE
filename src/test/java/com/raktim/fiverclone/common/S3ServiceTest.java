package com.raktim.fiverclone.common;

import com.raktim.fiverclone.common.utils.GenerateUploadUrlResult;
import com.raktim.fiverclone.common.utils.S3Service;
import com.raktim.fiverclone.utils.ExceptionTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.net.URL;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    private S3Presigner s3Presigner;
    private S3Service s3Service;
    private S3Client s3Client;

    @BeforeEach
    void setUp() {
        s3Presigner = mock(S3Presigner.class);
        s3Client = mock(S3Client.class);
        s3Service = new S3Service(s3Presigner, s3Client);

        ReflectionTestUtils.setField(
                s3Service,
                "bucket",
                "test-bucket"
        );
    }

    @Test
    @DisplayName("""
        Given generateUploadUrl,
        When called and no exception occurs,
        Then it should return signed url with expiration instant
        """)
    void generateUploadUrl_valid() throws Exception {

        PresignedPutObjectRequest presignedRequest =
                mock(PresignedPutObjectRequest.class);

        URL url = URI
                .create("https://test-bucket.s3.amazonaws.com/test-file.png")
                .toURL();


        when(presignedRequest.url()).thenReturn(url);

        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenReturn(presignedRequest);

        GenerateUploadUrlResult result = s3Service.generateUploadUrl(
                "uploads/test-file.png",
                "image/png"
        );

        assertEquals(
                "https://test-bucket.s3.amazonaws.com/test-file.png",
                result.uploadUrl()
        );

        assertInstanceOf(Instant.class, result.expiresAt());

        verify(s3Presigner, times(1))
                .presignPutObject(any(PutObjectPresignRequest.class));
    }


    @Test
    @DisplayName("""
            Given generateUploadUrl,
            When called And it throw exception, Then it should throw proper error messages
            """)
    void generateUploadUrl_exception() throws Exception {
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenThrow(new RuntimeException("AWS error"));

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "UNABLE_TO_GENERATE_UPLOAD_URL",
                "AWS error",
                () -> s3Service.generateUploadUrl(
                        "uploads/test-file.png",
                        "image/png"
                )
        );

        verify(s3Presigner, times(1))
                .presignPutObject(any(PutObjectPresignRequest.class));
    }

    @Test
    @DisplayName("""
            Given getImageUrl, When called
            And it does not throws any exception,
            Then it should return proper result
            """)
    void getImageUrl_valid() throws Exception {
        PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);

        URL url = URI
                .create("https://test-bucket.s3.amazonaws.com/get")
                .toURL();

        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenReturn(presignedRequest);

        when(presignedRequest.url()).thenReturn(url);
        String result = s3Service.getImageUrl("test-file.png");

        assertEquals("https://test-bucket.s3.amazonaws.com/get", result);
        verify(s3Presigner, times(1)).presignGetObject(any(GetObjectPresignRequest.class));
    }

    @Test
    @DisplayName("""
            Given getImageUrl, When called
            And it does not throws exception,
            Then it should display proper messages
            """)
    void getImageUrl_exception() throws Exception {
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenThrow(new RuntimeException("AWS error"));

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "UNABLE_TO_GET_IMAGE_URL",
                "AWS error",
                () -> s3Service.getImageUrl("test-file.png")
        );

        verify(s3Presigner, times(1)).presignGetObject(any(GetObjectPresignRequest.class));
    }

    @Test
    @DisplayName("""
            Given deleteFile, When called
            And it does not throws any exception
            Then it should delete the file using S3client
            """)
    void deleteFile_valid() throws Exception {
        when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
                .thenReturn(null);

        s3Service.deleteFile("test-file.png");
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}