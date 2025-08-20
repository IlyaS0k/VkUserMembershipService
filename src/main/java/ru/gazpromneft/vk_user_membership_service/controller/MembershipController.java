package ru.gazpromneft.vk_user_membership_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.gazpromneft.vk_user_membership_service.dto.request.MembershipInfoRequest;
import ru.gazpromneft.vk_user_membership_service.dto.response.ErrorResponse;
import ru.gazpromneft.vk_user_membership_service.dto.response.MembershipInfoResponse;
import ru.gazpromneft.vk_user_membership_service.service.MembershipService;

@RestController
@RequiredArgsConstructor
@Tag(name = "VK Membership API", description = "API для работы с членством пользователей VK в группах")
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/vk-user-membership-info")
    @Operation(
            summary = "Получить информацию о членстве пользователя в группе VK",
            description = "Возвращает информацию о пользователе и его членстве в указанной группе VK"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный запрос",
                    content = @Content(schema = @Schema(implementation = MembershipInfoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидный запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден в VK",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public MembershipInfoResponse getVkUserMembershipInfo(
            @Parameter(
                    description = "Сервисный токен доступа к VK API",
                    required = true
            )
            @RequestHeader("vk_service_token") String vkServiceToken,
            @Parameter(
                    description = "Идентификаторы пользователя и группы VK",
                    required = true
            )
            @Valid @RequestBody MembershipInfoRequest request
    ) throws Exception {
        var result = membershipService.getVkUserMembershipInfo(request, vkServiceToken);
        if (result.isSuccess()) {
            return result.getResponse();
        } else {
            throw result.getError();
        }
    }

}
