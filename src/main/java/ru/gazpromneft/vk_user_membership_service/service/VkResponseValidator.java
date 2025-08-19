package ru.gazpromneft.vk_user_membership_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import ru.gazpromneft.vk_user_membership_service.exception.VkException;

@Service
public class VkResponseValidator {

    public void validate(JsonNode responseJson) {
        if (responseJson.has("error")) {
            var error = responseJson.get("error");
            var errorMsg = error.get("error_msg").textValue();
            var errorCode = Integer.parseInt(error.get("error_code").toString());
            throw new VkException(errorMsg, errorCode);
        }
    }

}
