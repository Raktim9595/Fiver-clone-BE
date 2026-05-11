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

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public String generateUploadUrl(String key, String contentType) {
    try {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                        .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .putObjectRequest(putObjectRequest)
                        .build();

        return s3Presigner.presignPutObject(presignRequest)
                .url()
                .toString();
    } catch (RuntimeException ex) {
        throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "UNABLE_TO_GENERATE_UPLOAD_URL", ex.getMessage());
    }}
}
