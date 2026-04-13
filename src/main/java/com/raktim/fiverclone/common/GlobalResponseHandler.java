package com.raktim.fiverclone.common;

import com.raktim.fiverclone.common.DTO.ApiResponseDTO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackages = "com.raktim.fiverclone")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;

    public GlobalResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @NullMarked
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response
    ) {
        if (!(response instanceof ServletServerHttpResponse servletResponse)) {
            return body;
        }

        int status = servletResponse.getServletResponse().getStatus();

        if (status < 200 || status >= 300) {
            return body;
        }

        if (!MediaType.APPLICATION_JSON.includes(selectedContentType)
                && !selectedContentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            return body;
        }

        if (body == null
                || body instanceof ApiResponseDTO<?>
                || body instanceof ProblemDetail
                ) {
            return body;
        }

        ApiResponseDTO<Object> apiResponse = new ApiResponseDTO<>(
                LocalDateTime.now(),
                status,
                resolveMessage(status),
                request.getURI().getPath(),
                body
        );

        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(apiResponse);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize API response", e);
            }
        }

        return apiResponse;
    }

    private String resolveMessage(int status) {
        return status == 201 ? "Resource created successfully" : "Request successful";
    }

}
