package ru.gazpromneft.vk_user_membership_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MembershipInfoRequest {

    @NotNull(message = "User ID не может быть null")
    @Schema(description = "ID пользователя VK", example = "123456789")
    private Long userId;

    @NotNull(message = "Group ID не может быть null")
    @Schema(description = "ID группы VK", example = "111111111")
    private Long groupId;
}
