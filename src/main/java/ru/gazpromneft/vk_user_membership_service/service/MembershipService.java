package ru.gazpromneft.vk_user_membership_service.service;

import lombok.RequiredArgsConstructor;
import org.apache.camel.ProducerTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.gazpromneft.vk_user_membership_service.dto.request.MembershipInfoRequest;
import ru.gazpromneft.vk_user_membership_service.dto.response.MembershipInfoResult;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final ProducerTemplate producerTemplate;

    @Cacheable(value = "membershipInfo", key = "{#request.userId, #request.groupId, #vkServiceToken}")
    public MembershipInfoResult getVkUserMembershipInfo(MembershipInfoRequest request, String vkServiceToken) {

        return producerTemplate.requestBodyAndHeader(
                "direct:getUserMembershipInfo",
                request,
                "vk_service_token",
                vkServiceToken,
                MembershipInfoResult.class
        );
    }
}



