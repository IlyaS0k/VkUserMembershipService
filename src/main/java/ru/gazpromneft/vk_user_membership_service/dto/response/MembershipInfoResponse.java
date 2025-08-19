package ru.gazpromneft.vk_user_membership_service.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MembershipInfoResponse {

    @NotNull(message = "Фамилия не должен быть null")
    private String lastName;

    @NotNull(message = "Имя не должно быть null")
    private String firstName;

    @NotNull(message = "Отчество не должно быть null")
    private String middleName;

    @NotNull(message = "Членство в группе не должно быть null")
    private boolean member;
}
