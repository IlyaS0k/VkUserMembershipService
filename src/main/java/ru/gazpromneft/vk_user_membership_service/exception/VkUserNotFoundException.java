package ru.gazpromneft.vk_user_membership_service.exception;

public class VkUserNotFoundException extends RuntimeException {

    public VkUserNotFoundException(String message) {
        super(message);
    }
}
