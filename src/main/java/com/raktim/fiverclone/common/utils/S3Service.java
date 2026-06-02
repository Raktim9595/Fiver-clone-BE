package com.raktim.fiverclone.common.utils;

import com.raktim.fiverclone.common.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public GenerateUploadUrlResult generateUploadUrl(String key, String contentType) {
    try {
        Duration duration = Duration.ofMinutes(10);

        PutObjectRequest putObjectRequest = getPutObjectRequest(key, contentType);

        PutObjectPresignRequest presignRequest =
                getPutObjectPresignRequest(putObjectRequest, duration);

        String uploadUrl = s3Presigner.presignPutObject(presignRequest)
                .url()
                .toString();

        return new GenerateUploadUrlResult(
                uploadUrl,
                Instant.now().plus(duration)
        );
    } catch (RuntimeException ex) {
        throw new BusinessException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "UNABLE_TO_GENERATE_UPLOAD_URL",
                ex.getMessage()
        );
    }}

    public String getImageUrl(String key) {
    try {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    } catch (RuntimeException ex) {
        throw new BusinessException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "UNABLE_TO_GET_IMAGE_URL",
                ex.getMessage()
        );
    }}

    public void deleteFile(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

    private PutObjectRequest getPutObjectRequest(String key, String contentType) {
        return  PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();
    }

    private PutObjectPresignRequest getPutObjectPresignRequest(
            PutObjectRequest putObjectRequest,
            Duration duration
    ) {
        return PutObjectPresignRequest.builder()
                .signatureDuration(duration)
                .putObjectRequest(putObjectRequest)
                .build();
    }
}
