package com.raktim.fiverclone.common;

import com.raktim.fiverclone.common.DTO.ApiResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import tools.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@RequiredArgsConstructor
class TestBody {
    private final String name;
}

@DisplayName("GlobalResponseHandler Unit Test")
public class GlobalResponseHandlerTest {
    private GlobalResponseHandler globalResponseHandler;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        globalResponseHandler = new GlobalResponseHandler(objectMapper);
    }


    @Nested
    @DisplayName("Given supports method, When called")
    class SupportTests {
        @Test
        @DisplayName("Then it should always return true")
        void shouldAlwaysSupportAnyReturnType() {
            MethodParameter methodParameter = mock(MethodParameter.class);

            boolean result = globalResponseHandler.supports(
                    methodParameter,
                    null
            );

            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("Given beforeBodyWrite method, When called")
    class BeforeBodyWriteTests {
        @Test
        @DisplayName("With response not instance of ServletServerHttpResponse, Then it returns original body")
        void shouldReturnOriginalBodyWhenResponseIsNotServletServerHttpResponse() {
            Object body = new Object();
            MethodParameter returnType = mock(MethodParameter.class);
            ServerHttpRequest request = mock(ServerHttpRequest.class);
            ServerHttpResponse response = mock(ServerHttpResponse.class);

            Object result = globalResponseHandler.beforeBodyWrite(
                    body,
                    returnType,
                    MediaType.APPLICATION_JSON,
                    null,
                    request,
                    response
            );

            assertSame(body, result);
        }

        @Test
        @DisplayName("With status code not 2xx passed, Then it should return original body")
        void shouldReturnOriginalBodyWhenStatusIsNot2xx() {
            Object body = new Object();
            MethodParameter returnType = mock(MethodParameter.class);
            ServerHttpRequest request = mock(ServerHttpRequest.class);

            HttpServletResponse servletResponse = mock(HttpServletResponse.class);
            when(servletResponse.getStatus()).thenReturn(400);

            ServletServerHttpResponse response = new ServletServerHttpResponse(servletResponse);

            Object result = globalResponseHandler.beforeBodyWrite(
                    body,
                    returnType,
                    MediaType.APPLICATION_JSON,
                    null,
                    request,
                    response
            );

            assertSame(body, result);
        }

        @Test
        @DisplayName("When content type which is not JSON passed, then it should return original body")
        void shouldReturnOriginalBodyWhenContentTypeIsNotJson() {
            Object body = new Object();
            MethodParameter returnType = mock(MethodParameter.class);
            ServerHttpRequest request = mock(ServerHttpRequest.class);

            HttpServletResponse servletResponse = mock(HttpServletResponse.class);
            when(servletResponse.getStatus()).thenReturn(200);

            ServletServerHttpResponse response = new ServletServerHttpResponse(servletResponse);

            Object result = globalResponseHandler.beforeBodyWrite(
                    body,
                    returnType,
                    MediaType.TEXT_PLAIN,
                    null,
                    request,
                    response
            );

            assertSame(body, result);
        }

        @Test
        @DisplayName("When null body is passed, than it should return null")
        void shouldReturnOriginalBodyWhenBodyIsNull() {
            MethodParameter returnType = mock(MethodParameter.class);
            ServerHttpRequest request = mock(ServerHttpRequest.class);

            HttpServletResponse servletResponse = mock(HttpServletResponse.class);
            when(servletResponse.getStatus()).thenReturn(200);

            ServletServerHttpResponse response = new ServletServerHttpResponse(servletResponse);

            Object result = globalResponseHandler.beforeBodyWrite(
                    null,
                    returnType,
                    MediaType.APPLICATION_JSON,
                    null,
                    request,
                    response
            );

            assertNull(null);
        }

        @Test
        @DisplayName("When body is instance of ApiResponseDto, Then it should not do any modifications.")
        void shouldReturnOriginalBodyWhenBodyIsAlreadyApiResponseDto() {
            ApiResponseDTO<String> body = new ApiResponseDTO<>(
                    null,
                    200,
                    "ok",
                    "/test",
                    "data"
            );

            MethodParameter returnType = mock(MethodParameter.class);
            ServerHttpRequest request = mock(ServerHttpRequest.class);
            HttpServletResponse servletResponse = mock(HttpServletResponse.class);

            when(servletResponse.getStatus()).thenReturn(200);

            ServletServerHttpResponse response = new ServletServerHttpResponse(servletResponse);

            Object result = globalResponseHandler.beforeBodyWrite(
                    body,
                    returnType,
                    MediaType.APPLICATION_JSON,
                    null,
                    request,
                    response
            );

            assertSame(body, result);
        }

        @Test
        @DisplayName("When String is passed in Object, Then it should return ApiResponseDto")
        void shouldWrapStringIntoApiResponseDto() {
            String body = "hello";
            MethodParameter returnType = mock(MethodParameter.class);
            ServerHttpRequest request = mock(ServerHttpRequest.class);
            when(request.getURI()).thenReturn(java.net.URI.create("http://localhost/test"));

            HttpServletResponse servletResponse = mock(HttpServletResponse.class);
            when(servletResponse.getStatus()).thenReturn(200);

            ServletServerHttpResponse response = new ServletServerHttpResponse(servletResponse);

            Object result = globalResponseHandler.beforeBodyWrite(
                    body,
                    returnType,
                    MediaType.APPLICATION_JSON,
                    null,
                    request,
                    response
            );

            assertInstanceOf(String.class, result);
        }


        @Test
        @DisplayName("When proper object is passed, Then it should wrap it with APiResponseDto")
        void shouldWrapObjectBodyIntoApiResponseDTO() {
            TestBody body = new TestBody("Raktim");
            MethodParameter returnType = mock(MethodParameter.class);
            ServerHttpRequest request = mock(ServerHttpRequest.class);
            when(request.getURI()).thenReturn(java.net.URI.create("/api/test"));

            HttpServletResponse servletResponse = mock(HttpServletResponse.class);
            when(servletResponse.getStatus()).thenReturn(201);

            ServletServerHttpResponse response = new ServletServerHttpResponse(servletResponse);

            Object result = globalResponseHandler.beforeBodyWrite(
                    body,
                    returnType,
                    MediaType.APPLICATION_JSON,
                    null,
                    request,
                    response
            );

            assertInstanceOf(ApiResponseDTO.class, result);

            ApiResponseDTO<?> apiResponse = (ApiResponseDTO<?>) result;
            assertEquals(201, apiResponse.status());
            assertEquals("Resource created successfully", apiResponse.message());
            assertEquals("/api/test", apiResponse.path());
            assertEquals(body, apiResponse.data());
            assertNotNull(apiResponse.timestamp());
        }
    }

}
