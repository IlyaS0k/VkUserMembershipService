package ru.gazpromneft.vk_user_membership_service.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MembershipInfoRequest {

    private Long userId;

    private Long groupId;
}
