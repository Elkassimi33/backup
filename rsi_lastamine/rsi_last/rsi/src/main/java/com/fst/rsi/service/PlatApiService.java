package com.fst.rsi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fst.rsi.dtos.PlatDTO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class PlatApiService {

    private final String BASE_URL = "http://localhost:8081/api/plats";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private TokenManager tokenManager;

    public PlatApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public PlatApiService(TokenManager tokenManager) {
        this();
        this.tokenManager = tokenManager;
    }

    private HttpRequest.Builder addAuthHeader(HttpRequest.Builder builder) {
        if (tokenManager != null && tokenManager.isAuthenticated()) {
            String authHeader = tokenManager.getAuthorizationHeader();
            if (authHeader != null) {
                builder.header("Authorization", authHeader);
            }
        }
        return builder;
    }
    public List<PlatDTO> getPlatsDisponibles() {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/disponibles"))
                    .GET();

            addAuthHeader(requestBuilder);
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<PlatDTO>>(){});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<PlatDTO> searchPlats(String term) {
        try {
            //  espaces sont remplacé par %20 dans les url
            String encodedTerm = term.replace(" ", "%20");
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/search?searchTerm=" + encodedTerm))
                    .GET();

            addAuthHeader(requestBuilder);
            HttpRequest request = requestBuilder.build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<PlatDTO>>(){});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<String> getCategories() {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/categories"))
                    .GET();

            addAuthHeader(requestBuilder);
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<String>>(){});
            }
        } catch (Exception ignored) {}
        return Collections.emptyList();
    }

    public BufferedImage getPlatImage(Long id) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id + "/image"))
                    .GET();

            addAuthHeader(requestBuilder);
            HttpRequest request = requestBuilder.build();
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200 && response.body().length > 0) {
                return ImageIO.read(new ByteArrayInputStream(response.body()));
            }
        } catch (Exception e) {
            // Ignorer l'erreur d'image, on retournera null (image par défaut affichée par l'UI)
        }
        return null;
    }
}