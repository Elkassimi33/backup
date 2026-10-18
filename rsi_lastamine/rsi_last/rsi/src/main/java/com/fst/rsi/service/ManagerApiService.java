package com.fst.rsi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fst.rsi.dtos.*;
import com.fst.rsi.model.StatutAlerte;
import com.fst.rsi.model.RapportVente;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ManagerApiService {

    private final String BASE_URL = "http://localhost:8081/api/managers";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter dateFormatter;
    private TokenManager tokenManager;

    public ManagerApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.dateFormatter = DateTimeFormatter.ISO_DATE;
    }

    public ManagerApiService(TokenManager tokenManager) {
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

    // ==================== CRUD OPERATIONS ====================

    // POST /api/managers - Create manager
    public ManagerResponseDTO createManager(ManagerRequestDTO request) throws Exception {
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
            return objectMapper.readValue(response.body(), ManagerResponseDTO.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non autorisé. Veuillez vous reconnecter.");
        } else {
            throw new RuntimeException("Erreur lors de la création du manager: " +
                    response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/managers/{id} - Get manager by ID
    public ManagerResponseDTO getManagerById(Long id) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ManagerResponseDTO.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non autorisé. Veuillez vous reconnecter.");
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Manager non trouvé avec l'ID: " + id);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/managers - Get all managers with pagination
    public PageResponseDTO<ManagerResponseDTO> getAllManagers(PageableDTO pageableDTO) throws Exception {
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
            TypeReference<PageResponseDTO<ManagerResponseDTO>> typeRef =
                    new TypeReference<PageResponseDTO<ManagerResponseDTO>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non autorisé. Veuillez vous reconnecter.");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/managers/actifs - Get active managers
    public List<ManagerResponseDTO> getManagersActifs() throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/actifs"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<ManagerResponseDTO>> typeRef =
                    new TypeReference<List<ManagerResponseDTO>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non autorisé. Veuillez vous reconnecter.");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // PUT /api/managers/{id} - Update manager
    public ManagerResponseDTO updateManager(Long id, ManagerRequestDTO request) throws Exception {
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
            return objectMapper.readValue(response.body(), ManagerResponseDTO.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non autorisé. Veuillez vous reconnecter.");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // PATCH /api/managers/{id}/toggle-status - Toggle manager status
    public ManagerResponseDTO toggleManagerStatus(Long id) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/toggle-status"))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ManagerResponseDTO.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non autorisé. Veuillez vous reconnecter.");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // PUT /api/managers/{id}/password - Change password
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
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non autorisé. Veuillez vous reconnecter.");
        } else if (response.statusCode() == 400) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // ==================== RAPPORTS DE VENTE ====================

    // POST /api/managers/{idManager}/rapports/generer - Generate sales report
    public Map<String, Object> genererRapportVentes(Long idManager, LocalDate dateDebut, LocalDate dateFin) throws Exception {
        String debutStr = URLEncoder.encode(dateDebut.format(dateFormatter), StandardCharsets.UTF_8);
        String finStr = URLEncoder.encode(dateFin.format(dateFormatter), StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idManager + "/rapports/generer?dateDebut=" + debutStr + "&dateFin=" + finStr))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non autorisé. Veuillez vous reconnecter.");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // POST /api/managers/{idManager}/rapports/sauvegarder - Save report
    public RapportVente sauvegarderRapport(Long idManager, Map<String, Object> rapportData) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(rapportData);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idManager + "/rapports/sauvegarder"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), RapportVente.class);
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non autorisé. Veuillez vous reconnecter.");
        } else if (response.statusCode() == 401) {
            throw new RuntimeException("Non autorisé. Veuillez vous reconnecter.");
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/managers/{idManager}/rapports - Get reports by manager
    public List<RapportVente> getRapportsByManager(Long idManager) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idManager + "/rapports"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<RapportVente>> typeRef = new TypeReference<List<RapportVente>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // POST /api/managers/{idManager}/rapports/generer-et-sauvegarder - Generate and save report
    public RapportVente genererEtSauvegarderRapport(Long idManager, LocalDate dateDebut, LocalDate dateFin) throws Exception {
        String debutStr = URLEncoder.encode(dateDebut.format(dateFormatter), StandardCharsets.UTF_8);
        String finStr = URLEncoder.encode(dateFin.format(dateFormatter), StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idManager + "/rapports/generer-et-sauvegarder?dateDebut=" + debutStr + "&dateFin=" + finStr))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), RapportVente.class);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // ==================== DASHBOARD ====================

    // GET /api/managers/dashboard/stats - Get dashboard statistics
    public Map<String, Object> getDashboardStats() throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/dashboard/stats"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/managers/dashboard/resume - Get period summary
    public Map<String, Object> getResumePeriode(LocalDate dateDebut, LocalDate dateFin) throws Exception {
        String debutStr = URLEncoder.encode(dateDebut.format(dateFormatter), StandardCharsets.UTF_8);
        String finStr = URLEncoder.encode(dateFin.format(dateFormatter), StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/dashboard/resume?dateDebut=" + debutStr + "&dateFin=" + finStr))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // ==================== GESTION DU PERSONNEL ====================

    // POST /api/managers/{idManager}/evaluer-serveur/{idServeur} - Evaluate server
    public PerformanceServeur evaluerServeur(Long idManager, Long idServeur, LocalDate dateDebut,
                                             LocalDate dateFin, Map<String, Object> evaluation) throws Exception {
        String debutStr = URLEncoder.encode(dateDebut.format(dateFormatter), StandardCharsets.UTF_8);
        String finStr = URLEncoder.encode(dateFin.format(dateFormatter), StandardCharsets.UTF_8);
        String jsonBody = objectMapper.writeValueAsString(evaluation);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idManager + "/evaluer-serveur/" + idServeur +
                        "?dateDebut=" + debutStr + "&dateFin=" + finStr))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), PerformanceServeur.class);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // ==================== GESTION DES ALERTES ====================

    // GET /api/managers/alertes/non-traitees - Get unhandled alerts
    public List<AlerteStock> getAlertesNonTraitees() throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/alertes/non-traitees"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<List<AlerteStock>> typeRef = new TypeReference<List<AlerteStock>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // PATCH /api/managers/{idManager}/alertes/{idAlerte}/traiter - Handle alert
    public AlerteStock traiterAlerte(Long idManager, Long idAlerte, String actionPrise,
                                     StatutAlerte nouveauStatut) throws Exception {
        String encodedAction = URLEncoder.encode(actionPrise, StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idManager + "/alertes/" + idAlerte + "/traiter" +
                        "?actionPrise=" + encodedAction + "&nouveauStatut=" + nouveauStatut.name()))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), AlerteStock.class);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // ==================== RAPPORTS SPÉCIFIQUES ====================

    // GET /api/managers/{idManager}/rapports/journalier - Daily report
    public Map<String, Object> rapportJournalier(Long idManager, LocalDate date) throws Exception {
        String url = BASE_URL + "/" + idManager + "/rapports/journalier";
        if (date != null) {
            String dateStr = URLEncoder.encode(date.format(dateFormatter), StandardCharsets.UTF_8);
            url += "?date=" + dateStr;
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // Overloaded method for today's report
    public Map<String, Object> rapportJournalier(Long idManager) throws Exception {
        return rapportJournalier(idManager, null);
    }

    // GET /api/managers/{idManager}/rapports/hebdomadaire - Weekly report
    public Map<String, Object> rapportHebdomadaire(Long idManager) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idManager + "/rapports/hebdomadaire"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/managers/{idManager}/rapports/mensuel - Monthly report
    public Map<String, Object> rapportMensuel(Long idManager, Integer mois, Integer annee) throws Exception {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/" + idManager + "/rapports/mensuel");
        boolean hasParams = false;

        if (mois != null) {
            urlBuilder.append("?mois=").append(mois);
            hasParams = true;
        }
        if (annee != null) {
            urlBuilder.append(hasParams ? "&" : "?").append("annee=").append(annee);
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder.toString()))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // Overloaded method for current month
    public Map<String, Object> rapportMensuel(Long idManager) throws Exception {
        return rapportMensuel(idManager, null, null);
    }

    // GET /api/managers/{idManager}/rapports/annuel - Annual report
    public Map<String, Object> rapportAnnuel(Long idManager, Integer annee) throws Exception {
        String url = BASE_URL + "/" + idManager + "/rapports/annuel";
        if (annee != null) {
            url += "?annee=" + annee;
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // Overloaded method for current year
    public Map<String, Object> rapportAnnuel(Long idManager) throws Exception {
        return rapportAnnuel(idManager, null);
    }

    // ==================== STATISTIQUES AVANCÉES ====================

    // GET /api/managers/statistiques/performance-globale - Global performance statistics
    public Map<String, Object> getPerformanceGlobale(LocalDate dateDebut, LocalDate dateFin) throws Exception {
        String debutStr = URLEncoder.encode(dateDebut.format(dateFormatter), StandardCharsets.UTF_8);
        String finStr = URLEncoder.encode(dateFin.format(dateFormatter), StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/statistiques/performance-globale?dateDebut=" + debutStr + "&dateFin=" + finStr))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/managers/statistiques/comparaison - Compare periods
    public Map<String, Object> comparerPeriodes(LocalDate debut1, LocalDate fin1,
                                                LocalDate debut2, LocalDate fin2) throws Exception {
        String debut1Str = URLEncoder.encode(debut1.format(dateFormatter), StandardCharsets.UTF_8);
        String fin1Str = URLEncoder.encode(fin1.format(dateFormatter), StandardCharsets.UTF_8);
        String debut2Str = URLEncoder.encode(debut2.format(dateFormatter), StandardCharsets.UTF_8);
        String fin2Str = URLEncoder.encode(fin2.format(dateFormatter), StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/statistiques/comparaison?debut1=" + debut1Str +
                        "&fin1=" + fin1Str + "&debut2=" + debut2Str + "&fin2=" + fin2Str))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // ==================== EXPORTS ====================

    // GET /api/managers/{idManager}/rapports/{idRapport}/export/pdf - Export report to PDF
    public String exportRapportPDF(Long idManager, Long idRapport) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idManager + "/rapports/" + idRapport + "/export/pdf"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/managers/{idManager}/rapports/{idRapport}/export/excel - Export report to Excel
    public String exportRapportExcel(Long idManager, Long idRapport) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idManager + "/rapports/" + idRapport + "/export/excel"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/managers/statistiques/export/csv - Export statistics to CSV
    public byte[] exportStatisticsCSV(LocalDate dateDebut, LocalDate dateFin) throws Exception {
        String debutStr = URLEncoder.encode(dateDebut.format(dateFormatter), StandardCharsets.UTF_8);
        String finStr = URLEncoder.encode(dateFin.format(dateFormatter), StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/statistiques/export/csv?dateDebut=" + debutStr + "&dateFin=" + finStr))
                .GET()
                .header("Accept", "text/csv");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Erreur lors de l'export CSV: " + response.statusCode());
        }
    }

    // Helper method to save CSV file
    public void saveCSVToFile(byte[] csvData, String filePath) throws Exception {
        java.nio.file.Files.write(java.nio.file.Paths.get(filePath), csvData);
    }

    // ==================== ACTIONS RAPIDES ====================

    // GET /api/managers/{idManager}/actions-rapides - Get quick actions
    public Map<String, Object> getActionsRapides(Long idManager) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idManager + "/actions-rapides"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }

    // GET /api/managers/statut-restaurant - Get restaurant status
    public Map<String, Object> getStatutRestaurant() throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/statut-restaurant"))
                .GET()
                .header("Accept", "application/json");
        addAuthHeader(requestBuilder);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Erreur: " + response.statusCode() + " - " + response.body());
        }
    }
}