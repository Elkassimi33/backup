package com.rsi.resto.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rsi.resto.dtos.*;
import com.rsi.resto.enums.StatutCommande;
import com.rsi.resto.enums.TypeCommande;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommandeApiService {

    private final String BASE_URL = "http://localhost:8081/api/commandes";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter dateTimeFormatter;
    private TokenManager tokenManager;

    public CommandeApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
    }

    public CommandeApiService(TokenManager tokenManager) {
        this();
        this.tokenManager = tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    /**
     * Helper method to add Authorization header to HttpRequest.Builder
     */
    private HttpRequest.Builder addAuthHeader(HttpRequest.Builder builder) {
        if (tokenManager != null && tokenManager.isAuthenticated()) {
            String authHeader = tokenManager.getAuthorizationHeader();
            if (authHeader != null) {
                builder.header("Authorization", authHeader);
            }
        }
        return builder;
    }

    // POST /api/commandes - Create new commande
    public CommandeResponseDTO createCommande(CommandeRequestDTO request) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(request);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest httpRequest = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), CommandeResponseDTO.class);
        } else {
            throw new RuntimeException("Erreur lors de la création de la commande: " +
                    response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/commandes/{id} - Get commande by ID
    public CommandeResponseDTO getCommandeById(Long id) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), CommandeResponseDTO.class);
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Commande non trouvée avec l'ID: " + id);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/commandes - Get all commandes with pagination
    public PageResponseDTO<CommandeResponseDTO> getAllCommandes(PageableDTO pageableDTO) throws Exception {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("?page=").append(pageableDTO.getPage());
        urlBuilder.append("&size=").append(pageableDTO.getSize());

        if (pageableDTO.getSort() != null && !pageableDTO.getSort().isEmpty()) {
            urlBuilder.append("&sort=").append(pageableDTO.getSort());
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder.toString()))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<PageResponseDTO<CommandeResponseDTO>> typeRef =
                    new TypeReference<PageResponseDTO<CommandeResponseDTO>>() {
                    };
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur lors de la récupération des commandes: " +
                    response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/commandes/client/{clientId} - Get commandes by client
    public List<CommandeResponseDTO> getCommandesByClient(Long clientId) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/client/" + clientId))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<CommandeResponseDTO>> typeRef =
                    new TypeReference<List<CommandeResponseDTO>>() {
                    };
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/commandes/statut/{statut} - Get commandes by status
    public List<CommandeResponseDTO> getCommandesByStatut(StatutCommande statut) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/statut/" + statut.name()))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<CommandeResponseDTO>> typeRef =
                    new TypeReference<List<CommandeResponseDTO>>() {
                    };
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/commandes/type/{type} - Get commandes by type
    public List<CommandeResponseDTO> getCommandesByType(TypeCommande type) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/type/" + type.name()))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<CommandeResponseDTO>> typeRef =
                    new TypeReference<List<CommandeResponseDTO>>() {
                    };
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/commandes/aujourd-hui - Get today's commandes
    public List<CommandeResponseDTO> getCommandesDuJour() throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/aujourd-hui"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<CommandeResponseDTO>> typeRef =
                    new TypeReference<List<CommandeResponseDTO>>() {
                    };
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/commandes/en-attente - Get pending commandes
    public List<CommandeResponseDTO> getCommandesEnAttente() throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/en-attente"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<CommandeResponseDTO>> typeRef =
                    new TypeReference<List<CommandeResponseDTO>>() {
                    };
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/commandes/periode?debut={debut}&fin={fin} - Get commandes by period
    public List<CommandeResponseDTO> getCommandesByPeriode(LocalDateTime debut, LocalDateTime fin) throws Exception {
        String debutStr = URLEncoder.encode(debut.format(dateTimeFormatter), StandardCharsets.UTF_8);
        String finStr = URLEncoder.encode(fin.format(dateTimeFormatter), StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/periode?debut=" + debutStr + "&fin=" + finStr))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<CommandeResponseDTO>> typeRef =
                    new TypeReference<List<CommandeResponseDTO>>() {
                    };
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // PATCH /api/commandes/{id}/statut?nouveauStatut={statut} - Update status
    public CommandeResponseDTO updateStatut(Long id, StatutCommande nouveauStatut) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/statut?nouveauStatut=" + nouveauStatut.name()))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), CommandeResponseDTO.class);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // PUT /api/commandes/{id} - Update commande
    public CommandeResponseDTO updateCommande(Long id, CommandeRequestDTO request) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(request);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest httpRequest = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), CommandeResponseDTO.class);
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Commande non trouvée avec l'ID: " + id);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

}