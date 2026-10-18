package com.fst.rsi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fst.rsi.dtos.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthApiService {

    private final String BASE_URL = "http://localhost:8081/api/auth";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;


    public AuthApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public AuthResponseDTO login(LoginRequestDTO loginRequest) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return  objectMapper.readValue(response.body(), AuthResponseDTO.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        } else {
            ErrorResponse errorResponse = objectMapper.readValue(response.body(), ErrorResponse.class);
            throw new RuntimeException("Erreur lors de la connexion: " +
                     " - " + errorResponse.getMessage());
        }
    }



    public AuthResponseDTO register(ClientRequestDTO clientRequestDTO) throws IOException, InterruptedException {
        String jsonBody = objectMapper.writeValueAsString(clientRequestDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/register"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response+"response");
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), AuthResponseDTO.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Refresh token invalide ou expiré");
        } else {
            ErrorResponse errorResponse = objectMapper.readValue(response.body(), ErrorResponse.class);
            throw new RuntimeException("Erreur lors du rafraîchissement du token: " +
                    " - " + errorResponse.getMessage());
        }
    }
    public AuthResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequest) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(refreshTokenRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/refresh"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), AuthResponseDTO.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Refresh token invalide ou expiré");
        } else {
            ErrorResponse errorResponse = objectMapper.readValue(response.body(), ErrorResponse.class);
            throw new RuntimeException("Erreur lors du rafraîchissement du token: " +
                    " - " + errorResponse.getError());
        }
    }
}
