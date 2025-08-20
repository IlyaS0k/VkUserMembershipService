package ru.gazpromneft.vk_user_membership_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.gazpromneft.vk_user_membership_service.dto.request.MembershipInfoRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.yaml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MembershipInfoControllerTest {

    @Value("${vk.api.version}")
    private String vkApiVersion;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void returnMembershipInfoSuccessfully() throws Exception {
        Stubs.usersGet(
                "123456789",
                "nickname",
                "test-token",
                vkApiVersion,
                """
                        {
                            "response": [
                                {
                                    "id": 123456789,
                                    "first_name": "Иван",
                                    "last_name": "Иванов",
                                    "nickname": "Иванович"
                                }
                            ]
                        }
                        """
        );

        Stubs.groupsIsMember(
                123456789L,
                111111111L,
                "test-token",
                vkApiVersion,
                "{ \"response\": 1 } "
        );

        MembershipInfoRequest request = MembershipInfoRequest.builder()
                .userId(123456789L)
                .groupId(111111111L)
                .build();

        mockMvc.perform(post("/vk-user-membership-info")
                        .header("vk_service_token", "test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Иванов"))
                .andExpect(jsonPath("$.middleName").value("Иванович"))
                .andExpect(jsonPath("$.member").value(true));
    }

    @Test
    void returnNotFoundWhenUserNotFound() throws Exception {
        Stubs.usersGet(
                "123456789",
                "nickname",
                "test-token",
                vkApiVersion,
                " { \"response\": [] } "
        );

        MembershipInfoRequest request = MembershipInfoRequest.builder()
                .userId(123456789L)
                .groupId(111111111L)
                .build();

        mockMvc.perform(post("/vk-user-membership-info")
                        .header("vk_service_token", "test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("VkUserNotFoundException"));
    }

    @Test
    void handleVkApiError() throws Exception {
        Stubs.usersGet(
                "123456789",
                "nickname",
                "bad-token",
                vkApiVersion,
                """
                        {
                            "error": {
                                "error_code": 100,
                                "error_msg": "Invalid token"
                            }
                        }
                        """
        );

        MembershipInfoRequest request = MembershipInfoRequest.builder()
                .userId(123456789L)
                .groupId(111111111L)
                .build();

        mockMvc.perform(post("/vk-user-membership-info")
                        .header("vk_service_token", "bad-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Invalid token"));
    }

    @Test
    void returnBadRequestWhenValidationFails() throws Exception {
        MembershipInfoRequest invalidRequest = MembershipInfoRequest.builder()
                .userId(null)
                .groupId(null)
                .build();

        mockMvc.perform(post("/vk-user-membership-info")
                        .header("vk_service_token", "test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

}