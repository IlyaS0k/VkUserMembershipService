package ru.gazpromneft.vk_user_membership_service.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class IsVkGroupMemberResponse {

    @NotNull(message = "Ответ не должен быть null")
    @Min(value = 0, message = "Ответ должен быть 0 или 1")
    @Max(value = 1, message = "Ответ должен быть 0 или 1")
    private Integer response;
}
