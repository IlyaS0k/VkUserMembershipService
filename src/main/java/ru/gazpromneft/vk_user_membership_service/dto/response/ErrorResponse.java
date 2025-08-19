package ru.gazpromneft.vk_user_membership_service.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public class ErrorResponse {
    private String message;
    private String type;
    private Instant timestamp;

    public static ErrorResponse byException(Exception ex) {

        return ErrorResponse.builder()
                .type(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();
    }
}
