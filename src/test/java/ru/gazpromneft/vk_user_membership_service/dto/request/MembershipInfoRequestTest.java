package ru.gazpromneft.vk_user_membership_service.dto.request;

import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование валидации запросов")
class MembershipInfoRequestTest {

    private static final Validator validator;

    static {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    @Test
    @DisplayName("Создание валидного запроса")
    void createValidRequest() {
        MembershipInfoRequest request = MembershipInfoRequest.builder()
                .userId(123456789L)
                .groupId(111111111L)
                .build();

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Проверка валидации: userId не может быть null")
    void failWhenUserIdIsNull() {
        MembershipInfoRequest request = MembershipInfoRequest.builder()
                .userId(null)
                .groupId(111111111L)
                .build();

        var violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("User ID не может быть null", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Проверка валидации: groupId не может быть null")
    void failWhenGroupIdIsNull() {
        MembershipInfoRequest request = MembershipInfoRequest.builder()
                .userId(123456789L)
                .groupId(null)
                .build();

        var violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Group ID не может быть null", violations.iterator().next().getMessage());
    }
}