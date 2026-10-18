package com.youssef.http;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class InterfaceCommande extends JFrame {

    private JTextField tableField;
    private JComboBox<String> platCombo;
    private JSpinner quantiteSpinner;
    private JTextArea instructionsArea;

    private List<Plat> platsDisponibles = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    // Valeurs fixes pour le test
    private static final Long ID_CLIENT = 1L;
    private static final Long ID_SERVEUR = 1L;

    public InterfaceCommande() {
        setTitle("Prise de commande - Serveur");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        chargerPlats();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel central avec les champs de saisie
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Numéro de table
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Numéro de table :"), gbc);

        gbc.gridx = 1;
        tableField = new JTextField();
        formPanel.add(tableField, gbc);

        // Sélection du plat
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Plat :"), gbc);

        gbc.gridx = 1;
        platCombo = new JComboBox<>();
        platCombo.addItem("Chargement des plats...");
        platCombo.setEnabled(false);
        formPanel.add(platCombo, gbc);

        // Quantité
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Quantité :"), gbc);

        gbc.gridx = 1;
        quantiteSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        formPanel.add(quantiteSpinner, gbc);

        // Instructions spéciales
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Instructions spéciales :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        instructionsArea = new JTextArea(3, 20);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(instructionsArea);
        formPanel.add(scrollPane, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnRafraichir = new JButton("🔄 Rafraîchir");
        btnRafraichir.addActionListener(e -> chargerPlats());
        buttonPanel.add(btnRafraichir);

        JButton btnValider = new JButton("✅ Valider la commande");
        btnValider.addActionListener(e -> envoyerCommande());
        buttonPanel.add(btnValider);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void chargerPlats() {
        SwingWorker<List<Plat>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Plat> doInBackground() throws Exception {
                // Essayez d'abord le nouveau endpoint, sinon l'ancien
                URL url = new URL("http://localhost:8080/api/plats/disponibles");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                            // Limitez la taille de la réponse pour éviter les problèmes de mémoire
                            if (response.length() > 100000) { // 100KB max
                                break;
                            }
                        }

                        // Solution alternative : parser manuellement le JSON
                        String jsonResponse = response.toString();
                        List<Plat> plats = new ArrayList<>();

                        // Recherche simple des plats dans le JSON
                        // Cela fonctionne si le JSON est structuré simplement
                        String[] platStrings = jsonResponse.split("\\},\\s*\\{");

                        for (String platStr : platStrings) {
                            try {
                                // Trouver l'ID du plat
                                String idPattern = "\"idPlat\"\\s*:\\s*(\\d+)";
                                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(idPattern);
                                java.util.regex.Matcher matcher = pattern.matcher(platStr);

                                if (matcher.find()) {
                                    Plat plat = new Plat();
                                    plat.setIdPlat(Long.parseLong(matcher.group(1)));

                                    // Trouver le nom
                                    String nomPattern = "\"nom\"\\s*:\\s*\"([^\"]+)\"";
                                    pattern = java.util.regex.Pattern.compile(nomPattern);
                                    matcher = pattern.matcher(platStr);
                                    if (matcher.find()) {
                                        plat.setNom(matcher.group(1));
                                    }

                                    // Trouver le prix
                                    String prixPattern = "\"prix\"\\s*:\\s*([\\d.]+)";
                                    pattern = java.util.regex.Pattern.compile(prixPattern);
                                    matcher = pattern.matcher(platStr);
                                    if (matcher.find()) {
                                        plat.setPrix(new BigDecimal(matcher.group(1)));
                                    }

                                    // Trouver la catégorie
                                    String catPattern = "\"categorie\"\\s*:\\s*\"([^\"]+)\"";
                                    pattern = java.util.regex.Pattern.compile(catPattern);
                                    matcher = pattern.matcher(platStr);
                                    if (matcher.find()) {
                                        plat.setCategorie(matcher.group(1));
                                    }

                                    // Trouver disponible
                                    String dispoPattern = "\"disponible\"\\s*:\\s*(true|false)";
                                    pattern = java.util.regex.Pattern.compile(dispoPattern);
                                    matcher = pattern.matcher(platStr);
                                    if (matcher.find()) {
                                        plat.setDisponible(Boolean.parseBoolean(matcher.group(1)));
                                    }

                                    plats.add(plat);
                                }
                            } catch (Exception e) {
                                // Ignorer les erreurs de parsing pour ce plat
                                System.err.println("Erreur parsing plat: " + e.getMessage());
                            }
                        }

                        return plats;
                    }
                } else {
                    throw new Exception("Erreur HTTP : " + responseCode);
                }
            }

            @Override
            protected void done() {
                try {
                    platsDisponibles = get();
                    mettreAJourComboBox();
                    JOptionPane.showMessageDialog(InterfaceCommande.this,
                            "✅ " + platsDisponibles.size() + " plats chargés avec succès",
                            "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(InterfaceCommande.this,
                            "❌ Erreur lors du chargement des plats : " + e.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void mettreAJourComboBox() {
        platCombo.removeAllItems();
        for (Plat plat : platsDisponibles) {
            if (plat.isDisponible()) {
                String affichage = String.format("%s - %.2f € (%s)",
                        plat.getNom(),
                        plat.getPrix(),
                        plat.getCategorie() != null ? plat.getCategorie() : "Non catégorisé");
                platCombo.addItem(affichage);
            }
        }
        platCombo.setEnabled(platCombo.getItemCount() > 0);
        if (platCombo.getItemCount() == 0) {
            platCombo.addItem("Aucun plat disponible");
            platCombo.setEnabled(false);
        }
    }

    private void envoyerCommande() {
        try {
            int selectedIndex = platCombo.getSelectedIndex();
            if (selectedIndex < 0 || selectedIndex >= platsDisponibles.size()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner un plat valide",
                        "Erreur",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Plat platSelectionne = platsDisponibles.get(selectedIndex);
            int numeroTable = Integer.parseInt(tableField.getText());
            int quantite = (int) quantiteSpinner.getValue();
            String instructions = instructionsArea.getText().trim();

            String json = """
            {
              "idClient": %d,
              "idServeur": %d,
              "idTable": %d,
              "typeCommande": "SUR_PLACE",
              "lignesCommande": [
                {
                  "idPlat": %d,
                  "quantite": %d
                }
              ],
              "instructionsSpeciales": "%s",
              "remise": 0
            }
            """.formatted(ID_CLIENT, ID_SERVEUR, numeroTable,
                    platSelectionne.getIdPlat(), quantite,
                    instructions.replace("\"", "\\\""));

            URL url = new URL("http://localhost:8080/commande/add");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                BigDecimal total = platSelectionne.getPrix().multiply(new BigDecimal(quantite));
                JOptionPane.showMessageDialog(this,
                        "✅ Commande envoyée avec succès !\n" +
                                "Plat : " + platSelectionne.getNom() + "\n" +
                                "Description : " +
                                (platSelectionne.getDescription() != null && !platSelectionne.getDescription().isEmpty() ?
                                        platSelectionne.getDescription() : "Aucune description") + "\n" +
                                "Quantité : " + quantite + "\n" +
                                "Prix unitaire : " + platSelectionne.getPrix() + " €\n" +
                                "Total : " + total + " €\n" +
                                "Catégorie : " +
                                (platSelectionne.getCategorie() != null ? platSelectionne.getCategorie() : "Non catégorisé"),
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                // Réinitialiser les champs
                tableField.setText("");
                quantiteSpinner.setValue(1);
                instructionsArea.setText("");
            } else {
                String errorMessage;
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder error = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        error.append(line);
                    }
                    errorMessage = error.toString();
                } catch (Exception ex) {
                    errorMessage = "Erreur " + responseCode;
                }

                JOptionPane.showMessageDialog(this,
                        "❌ Erreur serveur : " + errorMessage,
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez entrer un numéro de table valide",
                    "Erreur de saisie",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur : " + ex.getMessage(),
                    "Exception",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Classe interne pour représenter les plats - version simplifiée
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Plat {
        private Long idPlat;
        private String nom;
        private String description;
        private BigDecimal prix;
        private String categorie;
        private Integer tempsPreparation;
        private boolean disponible;
        private String image;
        // On retire LocalDate car nous ne l'utilisons pas

        public Long getIdPlat() { return idPlat; }
        public void setIdPlat(Long idPlat) { this.idPlat = idPlat; }

        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public BigDecimal getPrix() { return prix; }
        public void setPrix(BigDecimal prix) { this.prix = prix; }

        public String getCategorie() { return categorie; }
        public void setCategorie(String categorie) { this.categorie = categorie; }

        public Integer getTempsPreparation() { return tempsPreparation; }
        public void setTempsPreparation(Integer tempsPreparation) { this.tempsPreparation = tempsPreparation; }

        public boolean isDisponible() { return disponible; }
        public void setDisponible(boolean disponible) { this.disponible = disponible; }

        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceCommande frame = new InterfaceCommande();
            frame.setVisible(true);
        });
    }
}