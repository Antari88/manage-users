package it.arico.manage_users.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "keycloak.token-uri=http://localhost:8080/auth",
        "keycloak.client-id=test-id",
        "keycloak.client-secret=test-secret"
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void login_shouldReturnAccessToken() throws Exception {

        Map<String, Object> mockResponse = Map.of("access_token", "fake-token");

        ParameterizedTypeReference<Map<String, Object>> responseType =
                new ParameterizedTypeReference<>() {};

        when(restTemplate.<Map<String, Object>>exchange(
                anyString(),
                any(),
                any(),
                eq(responseType)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        mockMvc.perform(post("/auth/login")
                        .param("username", "myUser")
                        .param("password", "myPass"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token")
                        .value("fake-token"));
    }
}
