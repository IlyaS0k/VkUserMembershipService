package ru.gazpromneft.vk_user_membership_service.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    @DisplayName("Проверка корректного создания ErrorResponse через метод fromException")
    void createErrorResponseFromException() {
        RuntimeException exception = new RuntimeException("test error");

        ErrorResponse response = ErrorResponse.fromException(exception);

        assertEquals("test error", response.getMessage());
        assertEquals("RuntimeException", response.getType());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Проверка корректного создания ErrorResponse через builder")
    void buildErrorResponseWithBuilder() {
        Instant timestamp = Instant.now();
        ErrorResponse response = ErrorResponse.builder()
                .message("Error message")
                .type("TestException")
                .timestamp(timestamp)
                .build();

        assertEquals("Error message", response.getMessage());
        assertEquals("TestException", response.getType());
        assertEquals(timestamp, response.getTimestamp());
    }
}
