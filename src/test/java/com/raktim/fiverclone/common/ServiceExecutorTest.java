package com.raktim.fiverclone.common;


import com.raktim.fiverclone.common.exceptions.BusinessException;
import com.raktim.fiverclone.common.utils.ServiceExecutor;
import com.raktim.fiverclone.utils.ExceptionTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SuppressWarnings("unchecked")
public class ServiceExecutorTest {
    @Test
    @DisplayName("""
            Given ServiceExecutr.execute method, When called
            And action does not throw any error,
            Then it should perform the action
            """)
    public void testExecute_success() {
        Supplier<String> supplier = (Supplier<String>) mock(Supplier.class);
        when(supplier.get()).thenReturn("Success");

        String result = ServiceExecutor.execute(supplier);
        verify(supplier, times(1)).get();
        assertEquals("Success", result);
    }

    @Test
    @DisplayName("""
            Given ServiceExecutr.execute method, When called
            And action throws BusinessException,
            Then it should throw the exception.
            """)
    public void testExecute_businessException() {
        Supplier<String> supplier = (Supplier<String>) mock(Supplier.class);
        when(supplier.get()).thenThrow(new BusinessException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Something went wrong"
        ));

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Something went wrong",
                () -> ServiceExecutor.execute(supplier)
        );
    }

    @Test
    @DisplayName("""
            Given ServiceExecutr.execute method, When called
            And action throws RunTimeException,
            Then it should throw the exception.
            """)
    public void testExecute_runTimeException() {
        Supplier<String> supplier = (Supplier<String>) mock(Supplier.class);
        when(supplier.get()).thenThrow(new RuntimeException("Something went wrong"));

       RuntimeException exception = assertThrows(
               RuntimeException.class,
               () -> ServiceExecutor.execute(supplier)
       );
       assertEquals("Something went wrong", exception.getMessage());
    }
}
