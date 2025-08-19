package ru.gazpromneft.vk_user_membership_service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class ErrorResponse {
    private String message;
    private String type;
    private Instant timestamp;

    public static ErrorResponse fromException(Exception ex) {

        return ErrorResponse.builder()
                .type(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();
    }
}
