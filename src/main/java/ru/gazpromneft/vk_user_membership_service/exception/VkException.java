package ru.gazpromneft.vk_user_membership_service.exception;

import lombok.Getter;

@Getter
public class VkException extends RuntimeException {
    private final int code;

    public VkException(String message, int code) {
        super(message);
        this.code = code;
    }
}
