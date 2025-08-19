package ru.gazpromneft.vk_user_membership_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.gazpromneft.vk_user_membership_service.dto.response.ErrorResponse;
import ru.gazpromneft.vk_user_membership_service.exception.VkException;
import ru.gazpromneft.vk_user_membership_service.exception.VkUserNotFoundException;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class CustomControllerAdvice {

    @ExceptionHandler(VkUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVkUserNotFoundException(VkUserNotFoundException ex) {
        log.warn("Пользователь VK не найден: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.fromException(ex));
    }

    @ExceptionHandler(VkException.class)
    public ResponseEntity<ErrorResponse> handleVkUserNotFoundException(VkException ex) {
        log.warn("Ошибка при обращении к VK: {}", ex.getMessage());
        var errorMessage = "Ошибка при обращении к VK API, код: " + ex.getCode() + "\n" + ex.getMessage();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorResponse.builder()
                        .type(ex.getClass().getSimpleName())
                        .message(errorMessage)
                        .timestamp(Instant.now())
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex) {
        log.error("Произошла непредвиденная ошибка: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.fromException(ex));
    }
}
