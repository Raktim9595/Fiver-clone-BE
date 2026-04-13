package com.raktim.fiverclone.utils;

import com.raktim.fiverclone.common.exceptions.BusinessException;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTestUtil {

    public static void assertBusinessException(
            HttpStatus expectedStatus,
            String expectedErrorCode,
            String expectedMessage,
            Executable executable
    ) {

        BusinessException exception = assertThrows(
                BusinessException.class,
                executable
        );

        assertAll(
                () -> assertEquals(expectedStatus, exception.getHttpStatus()),
                () -> assertEquals(expectedErrorCode, exception.getErrorCode()),
                () -> assertEquals(expectedMessage, exception.getMessage())
        );
    }
}