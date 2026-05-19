package com.raktim.fiverclone.common.utils;

import com.raktim.fiverclone.common.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public GenerateUploadUrlResult generateUploadUrl(String key, String contentType) {
    try {
        Duration duration = Duration.ofMinutes(10);

        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                        .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(duration)
                        .putObjectRequest(putObjectRequest)
                        .build();

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
}
