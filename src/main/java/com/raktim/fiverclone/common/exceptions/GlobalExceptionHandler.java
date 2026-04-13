package com.raktim.fiverclone.common.exceptions;

import com.raktim.fiverclone.common.DTO.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.raktim.fiverclone")
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        // we can match the timestamp in logger and error response so that we can query by timestamp in log file.
        LocalDateTime timestamp = getCurrentTime();
        logger.error("MethodArgumentNotValidException, {}, {} ", timestamp, ex.getMessage());

        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        validationErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                timestamp,
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                "Validation failed",
                request.getRequestURI(),
                validationErrors
        );
        System.out.println("Here");

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {
        LocalDateTime timestamp = getCurrentTime();
        logger.error("MethodArgumentNotValidException, {}, {} ", timestamp, ex.getMessage());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                timestamp,
                ex.getHttpStatus().value(),
                ex.getErrorCode(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    // Keep on adding new database exceptions for logging
    @ExceptionHandler({
            DataIntegrityViolationException.class,
            DuplicateKeyException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleDatabaseConstraintException(
            HttpServletRequest request
    ) {
        LocalDateTime timestamp = getCurrentTime();
        logger.error("MethodArgumentNotValidException, {}, ", timestamp);

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                timestamp,
                HttpStatus.CONFLICT.value(),
                "INTERNAL_SERVER_ERROR",
                "Something went wrong executing db query, please refer to logs for more details.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        LocalDateTime timestamp = getCurrentTime();
        logger.error("HttpMessageNotReadableException, {}, {}", timestamp, ex.getMessage());

        String message = getMessage(ex);

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                timestamp,
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    private static @NonNull String getMessage(HttpMessageNotReadableException ex) {
        String message = "Malformed JSON request";

        if (ex.getCause() instanceof InvalidFormatException ifx) {
            if (ifx.getTargetType()!=null && ifx.getTargetType().isEnum()) {
                message = String.format(
                        "Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                        ifx.getValue(),
                        ifx.getPath().getFirst().getPropertyName(),
                        Arrays.toString(ifx.getTargetType().getEnumConstants())
                );
            }
        }
        return message;
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
