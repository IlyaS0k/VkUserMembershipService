package ru.gazpromneft.vk_user_membership_service.route;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
import ru.gazpromneft.vk_user_membership_service.dto.request.MembershipInfoRequest;
import ru.gazpromneft.vk_user_membership_service.dto.response.*;
import ru.gazpromneft.vk_user_membership_service.exception.VkUserNotFoundException;
import ru.gazpromneft.vk_user_membership_service.service.VkResponseValidator;

@Component
@RequiredArgsConstructor
public class VkApiRoute extends RouteBuilder {

    private final ObjectMapper mapper;

    private final VkResponseValidator vkResponseValidator;

    private final String USER_GROUP_EXCHANGE_PROP = "userGroupInfo";

    private final String USER_NAME_EXCHANGE_PROP = "userName";

    private final String IS_MEMBER_EXCHANGE_PROP = "isMember";

    @Override
    public void configure() {

        from("direct:getUserMembershipInfo")
                .routeId("get-user-membership-info-route")
                .log("Получение информации о пользователе ${body.userId} и его участии в группе: ${body.groupId}")
                .setProperty(USER_GROUP_EXCHANGE_PROP, body())
                .doTry()
                .to("direct:getUserInfo")
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> {
                    var userNameResponseJson = exchange.getIn().getBody(JsonNode.class);
                    vkResponseValidator.validate(userNameResponseJson);
                    var userNameResponse = mapper.treeToValue(
                            userNameResponseJson,
                            new TypeReference<VkResponse<VkUserNameResponse>>() {
                            }
                    );
                    var userName = userNameResponse.getResponse().stream().findFirst().orElse(null);
                    if (userName == null) {
                        var userId = exchange.getProperty(USER_GROUP_EXCHANGE_PROP, MembershipInfoRequest.class).getUserId();
                        throw new VkUserNotFoundException("Не найден пользователь с id " + userId);
                    }

                    exchange.setProperty(USER_NAME_EXCHANGE_PROP, userName);
                })
                .to("direct:checkGroupMembership")
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> {
                    var isMemberResponseJson = exchange.getIn().getBody(JsonNode.class);
                    vkResponseValidator.validate(isMemberResponseJson);
                    var isMemberResponse = mapper.treeToValue(isMemberResponseJson, IsVkGroupMemberResponse.class);
                    var isMember = isMemberResponse.getResponse() == 1;

                    exchange.setProperty(IS_MEMBER_EXCHANGE_PROP, isMember);
                })
                .doCatch(Exception.class)
                .process(exchange -> {
                    var exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    log.error("Ошибка при обработке запроса об участии пользователя в группе: {}", exception.getMessage());

                    exchange.getIn().setBody(MembershipInfoResult.error(exception));
                })
                .stop()
                .end()
                .process(exchange -> {
                    var userName = exchange.getProperty(USER_NAME_EXCHANGE_PROP, VkUserNameResponse.class);
                    var isMember = exchange.getProperty(IS_MEMBER_EXCHANGE_PROP, Boolean.class);
                    var result = MembershipInfoResponse.builder()
                            .firstName(userName.getFirstName())
                            .lastName(userName.getLastName())
                            .middleName(userName.getNickname())
                            .member(isMember)
                            .build();

                    var userGroupInfo = exchange.getProperty(USER_GROUP_EXCHANGE_PROP, MembershipInfoRequest.class);
                    log.info(
                            "Получена информация о пользователе {} и его участии в группе {}, результат: {}",
                            userGroupInfo.getUserId(), userGroupInfo.getGroupId(), result.toString()
                    );
                    exchange.getIn().setBody(MembershipInfoResult.success(result));
                })
                .convertBodyTo(MembershipInfoResult.class);

        from("direct:getUserInfo")
                .routeId("get-user-info-route")
                .setHeader("CamelHttpMethod", constant("GET"))
                .setBody(exchangeProperty(USER_GROUP_EXCHANGE_PROP))
                .toD("{{vk.api.base-url}}{{vk.api.endpoints.users-get}}?user_ids=${body.userId}&" +
                        "fields=nickname&access_token=${header.vk_service_token}&v={{vk.api.version}}");

        from("direct:checkGroupMembership")
                .routeId("check-group-membership-route")
                .setHeader("CamelHttpMethod", constant("GET"))
                .setBody(exchangeProperty(USER_GROUP_EXCHANGE_PROP))
                .toD("{{vk.api.base-url}}{{vk.api.endpoints.groups-is-member}}?user_id=${body.userId}&" +
                        "group_id=${body.groupId}&access_token=${header.vk_service_token}&v={{vk.api.version}}");
    }
}

