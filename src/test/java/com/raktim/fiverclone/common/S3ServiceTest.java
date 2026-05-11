package com.raktim.fiverclone.common;

import com.raktim.fiverclone.common.utils.S3Service;
import com.raktim.fiverclone.utils.ExceptionTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    private S3Presigner s3Presigner;
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        s3Presigner = mock(S3Presigner.class);
        s3Service = new S3Service(s3Presigner);

        ReflectionTestUtils.setField(
                s3Service,
                "bucket",
                "test-bucket"
        );
    }

    @Test
    @DisplayName("Given generateUploadUrl, When called And it does not throw any error Then it should return proper" +
            "signed url")
    void generateUploadUrl_valid() throws Exception {
        PresignedPutObjectRequest presignedRequest =
                mock(PresignedPutObjectRequest.class);

        URL url = URI
                .create("https://test-bucket.s3.amazonaws.com/test-file.png")
                .toURL();

        when(presignedRequest.url()).thenReturn(url);
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenReturn(presignedRequest);

        String result = s3Service.generateUploadUrl(
                "uploads/test-file.png",
                "image/png"
        );

        assertEquals(
                "https://test-bucket.s3.amazonaws.com/test-file.png",
                result
        );

        verify(s3Presigner, times(1))
                .presignPutObject(any(PutObjectPresignRequest.class));
    }

    @Test
    @DisplayName("Given generateUploadUrl, When called And it throw exception, Then it should throw proper" +
            "error messages")
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

}