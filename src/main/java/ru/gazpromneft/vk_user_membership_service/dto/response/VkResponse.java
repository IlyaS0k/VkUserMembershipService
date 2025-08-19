package ru.gazpromneft.vk_user_membership_service.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class VkResponse<T> {

    @NotNull(message = "Ответ из VK не должен быть null")
    private List<T> response;
}
