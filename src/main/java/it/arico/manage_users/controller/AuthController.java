package it.arico.manage_users.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RestTemplate restTemplate;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestParam String username,
            @RequestParam String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", username);
        formData.add("password", password);
        formData.add("scope", "openid profile email");

        HttpEntity<MultiValueMap<String, String>> request =
            new HttpEntity<>(formData, headers);

        ResponseEntity<Map<String, Object>> response =
        restTemplate.exchange(
                tokenUri,
                org.springframework.http.HttpMethod.POST,
                request,
                new org.springframework.core.ParameterizedTypeReference<>() {}
        );

        Map<String, Object> token = response.getBody();
        if (token == null || !token.containsKey("access_token")) {
            throw new IllegalStateException("Impossible retrive token");
        }

        return Map.of("access_token", token.get("access_token"));
    }
}
