package ru.gazpromneft.vk_user_membership_service.dto.response;

import lombok.Getter;

@Getter
public class MembershipInfoResult {

    private boolean success;

    private MembershipInfoResponse response;

    private Exception error;

    private MembershipInfoResult() {}

    public static MembershipInfoResult success(MembershipInfoResponse response) {
        var instance = new MembershipInfoResult();
        instance.success = true;
        instance.response = response;
        return instance;
    }

    public static MembershipInfoResult error(Exception error) {
        var instance = new MembershipInfoResult();
        instance.success = false;
        instance.error = error;
        return instance;
    }

}
