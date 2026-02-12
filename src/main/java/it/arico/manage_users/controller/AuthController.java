package it.arico.manage_users.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final WebClient webClient;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public AuthController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PostMapping("/login")
    public Mono<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        return webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("username", username)
                        .with("password", password)
                        .with("scope", "openid profile email")
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(token -> Map.of("access_token", token.get("access_token")));
    }
}
