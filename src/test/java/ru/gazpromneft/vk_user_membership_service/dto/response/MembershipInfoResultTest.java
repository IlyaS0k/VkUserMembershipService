package ru.gazpromneft.vk_user_membership_service.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MembershipInfoResultTest {

    @Test
    @DisplayName("Проверка создания успешного MembershipInfoResult")
    void createSuccessResult() {
        MembershipInfoResponse response = MembershipInfoResponse.builder()
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .member(true)
                .build();

        MembershipInfoResult result = MembershipInfoResult.success(response);

        assertTrue(result.isSuccess());
        assertEquals(response, result.getResponse());
        assertNull(result.getError());
    }

    @Test
    @DisplayName("Проверка создания MembershipInfoResult содержащего ошибку")
    void createErrorResult() {
        Exception error = new RuntimeException("test error");

        MembershipInfoResult result = MembershipInfoResult.error(error);

        assertFalse(result.isSuccess());
        assertNull(result.getResponse());
        assertEquals(error, result.getError());
    }
}