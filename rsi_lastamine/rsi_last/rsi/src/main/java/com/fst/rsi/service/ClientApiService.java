package com.fst.rsi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fst.rsi.dtos.*;
import com.fst.rsi.model.StatutClient;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClientApiService {

    private final String BASE_URL = "http://localhost:8081/api/clients";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private TokenManager tokenManager;

    public ClientApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Gestion des LocalDate
    }

    public ClientApiService(TokenManager tokenManager) {
        this();
        this.tokenManager = tokenManager;
    }


    private void addAuthHeader(HttpRequest.Builder builder) {
        if (tokenManager != null && tokenManager.isAuthenticated()) {
            String authHeader = tokenManager.getAuthorizationHeader();
            if (authHeader != null) {
                builder.header("Authorization", authHeader);
            }
        }
    }
    public ClientResponseDTO createClient(ClientRequestDTO requestDTO) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(requestDTO);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), ClientResponseDTO.class);
        } else {
            throw new RuntimeException("Erreur création: " + response.statusCode() + " - " + response.body());
        }
    }


    public ClientResponseDTO utiliserPoints(Long id, Integer points) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/points/use?points=" + points))
                .POST(HttpRequest.BodyPublishers.noBody());

        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ClientResponseDTO.class);
        } else {
            throw new RuntimeException("Erreur points: " + response.body());
        }
    }



    // GET /api/clients - Get all clients with pagination
    public PageResponseDTO<ClientResponseDTO> getAllClients(PageableDTO pageableDTO) throws Exception {
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
            TypeReference<PageResponseDTO<ClientResponseDTO>> typeRef =
                    new TypeReference<PageResponseDTO<ClientResponseDTO>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur lors de la récupération des clients: " +
                    response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/{id} - Get client by ID
    public ClientResponseDTO getClientById(Long id) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ClientResponseDTO.class);
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Client non trouvé avec l'ID: " + id);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/email/{email} - Get client by email
    public ClientResponseDTO getClientByEmail(String email) throws Exception {
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/email/" + encodedEmail))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ClientResponseDTO.class);
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Client non trouvé avec l'email: " + email);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/telephone/{telephone} - Get client by telephone
    public ClientResponseDTO getClientByTelephone(String telephone) throws Exception {
        String encodedTelephone = URLEncoder.encode(telephone, StandardCharsets.UTF_8);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/telephone/" + encodedTelephone))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ClientResponseDTO.class);
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Client non trouvé avec le téléphone: " + telephone);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/actifs - Get active clients
    public List<ClientResponseDTO> getClientsActifs() throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/actifs"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<ClientResponseDTO>> typeRef = new TypeReference<List<ClientResponseDTO>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/statut/{statut} - Get clients by status
    public List<ClientResponseDTO> getClientsByStatut(StatutClient statut) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/statut/" + statut.name()))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<ClientResponseDTO>> typeRef = new TypeReference<List<ClientResponseDTO>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/search?searchTerm={term} - Search clients
    public List<ClientResponseDTO> searchClients(String searchTerm) throws Exception {
        String encodedTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/search?searchTerm=" + encodedTerm))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<ClientResponseDTO>> typeRef = new TypeReference<List<ClientResponseDTO>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/vip - Get VIP clients
    public List<ClientResponseDTO> getClientsVIP() throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/vip"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<ClientResponseDTO>> typeRef = new TypeReference<List<ClientResponseDTO>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/nouveaux - Get new clients
    public List<ClientResponseDTO> getNouveauxClients() throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/nouveaux"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<ClientResponseDTO>> typeRef = new TypeReference<List<ClientResponseDTO>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/inactifs?joursInactivite={days} - Get inactive clients
    public List<ClientResponseDTO> getClientsInactifs(int joursInactivite) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/inactifs?joursInactivite=" + joursInactivite))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<ClientResponseDTO>> typeRef = new TypeReference<List<ClientResponseDTO>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // PUT /api/clients/{id} - Update client
    public ClientResponseDTO updateClient(Long id, ClientUpdateDTO updateDTO) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(updateDTO);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ClientResponseDTO.class);
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Client non trouvé avec l'ID: " + id);
        } else if (response.statusCode() == 409) {
            throw new RuntimeException("Téléphone déjà utilisé");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // PUT /api/clients/{id}/password - Change password
    public void changePassword(Long id, ChangePasswordDTO passwordDTO) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(passwordDTO);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/password"))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return;
        } else if (response.statusCode() == 400) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // PATCH /api/clients/{id}/toggle-status - Toggle client status
    public ClientResponseDTO toggleClientStatus(Long id) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/toggle-status"))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ClientResponseDTO.class);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // PATCH /api/clients/{id}/deactivate - Deactivate client
    public void deactivateClient(Long id) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/deactivate"))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return;
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // DELETE /api/clients/{id} - Delete client
    public void deleteClient(Long id) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            return;
        } else if (response.statusCode() == 400) {
            throw new RuntimeException("Client avec commandes actives ne peut pas être supprimé");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // POST /api/clients/{id}/points/add?points={points} - Add loyalty points
    public ClientResponseDTO ajouterPointsFidelite(Long id, Integer points) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/points/add?points=" + points))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ClientResponseDTO.class);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // POST /api/clients/{id}/points/use?points={points} - Use loyalty points
    public ClientResponseDTO utiliserPointsFidelite(Long id, Integer points) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/points/use?points=" + points))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ClientResponseDTO.class);
        } else if (response.statusCode() == 400) {
            throw new RuntimeException("Points insuffisants");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/{id}/commandes - Get order history
    public List<CommandeHistoriqueDTO> getHistoriqueCommandes(Long id) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/commandes"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<CommandeHistoriqueDTO>> typeRef =
                    new TypeReference<List<CommandeHistoriqueDTO>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/clients/{id}/statistics - Get client statistics
    public ClientStatisticsDTO getClientStatistics(Long id) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/statistics"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ClientStatisticsDTO.class);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }
}