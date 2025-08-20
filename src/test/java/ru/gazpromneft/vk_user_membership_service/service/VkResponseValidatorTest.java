package ru.gazpromneft.vk_user_membership_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gazpromneft.vk_user_membership_service.exception.VkException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VkResponseValidatorTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private VkResponseValidator vkResponseValidator;

    @Test
    @DisplayName("Проверка валидации успешного ответа")
    void validateSuccessResponse() throws Exception {
        String json = "{\"response\": {\"first_name\": \"Иван\", \"last_name\": \"Иванов\"}}";
        JsonNode jsonNode = new ObjectMapper().readTree(json);

        assertDoesNotThrow(() -> vkResponseValidator.validate(jsonNode));
    }

    @Test
    @DisplayName("Проверка валидации ответа с ошибкой")
    void throwExceptionWhenErrorInResponse() throws Exception {
        String json = """
                {
                    "error": {
                        "error_code": 100,
                        "error_msg": "One of the parameters specified was missing or invalid: group_id is undefined",
                        "request_params": [
                            {
                                "key": "method",
                                "value": "groups.isMember"
                            }
                        ]
                    }
                }
                """;
        JsonNode jsonNode = new ObjectMapper().readTree(json);

        VkException exception = assertThrows(VkException.class,
                () -> vkResponseValidator.validate(jsonNode));

        assertEquals(100, exception.getCode());
        assertEquals(
                "One of the parameters specified was missing or invalid: group_id is undefined",
                exception.getMessage()
        );
    }
}