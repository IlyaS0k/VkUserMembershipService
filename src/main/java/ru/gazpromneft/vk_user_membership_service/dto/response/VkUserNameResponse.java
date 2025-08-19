package ru.gazpromneft.vk_user_membership_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VkUserNameResponse {

    @JsonProperty("first_name")
    @NotNull(message = "firstName не должен быть null")
    private String firstName;

    @JsonProperty("last_name")
    @NotNull(message = "lastName не должен быть null")
    private String lastName;

    @JsonProperty("nickname")
    @NotNull(message = "nickname не должен быть null")
    private String nickname;
}