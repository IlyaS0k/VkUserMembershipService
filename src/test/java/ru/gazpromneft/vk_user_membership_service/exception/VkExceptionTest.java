package ru.gazpromneft.vk_user_membership_service.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VkExceptionTest {

    @Test
    @DisplayName("Проверка корректного создания VkException")
    void createVkExceptionWithMessageAndCode() {
        VkException exception = new VkException("test error", 100);

        assertEquals("test error", exception.getMessage());
        assertEquals(100, exception.getCode());
    }

    @Test
    @DisplayName("Проверка корректного создания VkUserNotFoundException")
    void createVkUserNotFoundException() {
        VkUserNotFoundException exception = new VkUserNotFoundException("User not found");

        assertEquals("User not found", exception.getMessage());
    }
}