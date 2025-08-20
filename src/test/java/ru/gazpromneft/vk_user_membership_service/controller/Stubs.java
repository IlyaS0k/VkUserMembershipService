package ru.gazpromneft.vk_user_membership_service.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;

public class Stubs {

    public static void usersGet(
            String userIds,
            String fields,
            String accessToken,
            String vkApiVersion,
            String body
    ) {
        stubFor(WireMock.get(urlPathEqualTo("/method/users.get"))
                .withQueryParam("user_ids", equalTo(userIds))
                .withQueryParam("fields", equalTo(fields))
                .withQueryParam("access_token", equalTo(accessToken))
                .withQueryParam("v", equalTo(vkApiVersion))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(body)
                )
        );
    }

    public static void groupsIsMember(
            Long userId,
            Long groupId,
            String accessToken,
            String vkApiVersion,
            String body
    ) {
        stubFor(WireMock.get(urlPathEqualTo("/method/groups.isMember"))
                .withQueryParam("user_id", equalTo(userId.toString()))
                .withQueryParam("group_id", equalTo(groupId.toString()))
                .withQueryParam("access_token", equalTo(accessToken))
                .withQueryParam("v", equalTo(vkApiVersion))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(body)
                )
        );
    }
}
