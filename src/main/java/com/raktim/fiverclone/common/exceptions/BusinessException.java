package com.raktim.fiverclone.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// This is the exceptions class to handle exceptions generated using business logic.
// Other custom exceptions can extend this and use like UserNotFoundException extends BusinessException
// The main purpose of doing this is to facilitate our GlobalExceptionHandler Class
@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    public BusinessException(HttpStatus httpStatus, String errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
