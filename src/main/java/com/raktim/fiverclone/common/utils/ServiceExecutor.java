package com.raktim.fiverclone.common.utils;

import com.raktim.fiverclone.common.exceptions.BusinessException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@NoArgsConstructor
public class ServiceExecutor {
    public static <T> T execute(Supplier<T> action) {
        try {
            return action.get();
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "UNEXPECTED_ERROR",
                    ex.getMessage()
            );
        }
    }
}
