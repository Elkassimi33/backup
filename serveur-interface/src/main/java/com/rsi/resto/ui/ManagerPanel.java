package com.rsi.resto.ui;

import com.rsi.resto.dtos.ChangePasswordDTO;
import com.rsi.resto.dtos.ManagerRequestDTO;
import com.rsi.resto.dtos.ManagerResponseDTO;
import com.rsi.resto.model.RapportVente;
import com.rsi.resto.service.ManagerApiService;
import com.rsi.resto.service.TokenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManagerPanel extends JPanel {
    private ManagerApiService managerService;
    private JTable managerTable;
    private DefaultTableModel tableModel;
    private List<ManagerResponseDTO> managersList = new ArrayList<>();

    public ManagerPanel(TokenManager tokenManager) {
        this.managerService = new ManagerApiService(tokenManager);
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Gestion des Managers");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("➕ Ajouter");
        addButton.addActionListener(e -> showAddManagerDialog());
        buttonPanel.add(addButton);
        
        JButton updateButton = new JButton("✏️ Modifier");
        updateButton.addActionListener(e -> showUpdateManagerDialog());
        buttonPanel.add(updateButton);
        
        JButton passwordButton = new JButton("🔒 Mot de passe");
        passwordButton.addActionListener(e -> showChangePasswordDialog());
        buttonPanel.add(passwordButton);
        
        JButton toggleButton = new JButton("🔄 Activer/Désactiver");
        toggleButton.addActionListener(e -> toggleManagerStatus());
        buttonPanel.add(toggleButton);
        
        JButton reportsButton = new JButton("📊 Rapports");
        reportsButton.addActionListener(e -> showReportsDialog());
        buttonPanel.add(reportsButton);
        
        JButton refreshButton = new JButton("🔄 Actualiser");
        refreshButton.addActionListener(e -> loadManagers());
        buttonPanel.add(refreshButton);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Statut"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        managerTable = new JTable(tableModel);
        managerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        managerTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(managerTable);
        add(scrollPane, BorderLayout.CENTER);

        // Status label
        JLabel statusLabel = new JLabel("Prêt");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(statusLabel, BorderLayout.SOUTH);

        // Load data
        loadManagers();
    }

    private void loadManagers() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Clear table
                tableModel.setRowCount(0);

                // Load managers (using pagination with page 0, size 10)
                var pageable = com.rsi.resto.dtos.PageableDTO.builder()
                        .page(0)
                        .size(10)
                        .build();
                
                var response = managerService.getAllManagers(pageable);
                
                managersList.clear();
                if (response.getContent() != null) {
                    managersList.addAll(response.getContent());
                    for (var manager : response.getContent()) {
                        String statut = manager.getActif() != null && manager.getActif() ? "Actif" : "Inactif";
                        tableModel.addRow(new Object[]{
                            manager.getIdManager(),
                            manager.getNom(),
                            manager.getPrenom(),
                            manager.getEmail(),
                            manager.getTelephone(),
                            statut
                        });
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des managers: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void showAddManagerDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Ajouter un Manager", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nom
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nom *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField nomField = new JTextField(20);
        formPanel.add(nomField, gbc);
        gbc.weightx = 0;

        // Prénom
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Prénom *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField prenomField = new JTextField(20);
        formPanel.add(prenomField, gbc);
        gbc.weightx = 0;

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        gbc.weightx = 0;

        // Téléphone
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Téléphone *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField telephoneField = new JTextField(20);
        formPanel.add(telephoneField, gbc);
        gbc.weightx = 0;

        // Password
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Mot de passe *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        gbc.weightx = 0;

        // Adresse
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField adresseField = new JTextField(20);
        formPanel.add(adresseField, gbc);
        gbc.weightx = 0;

        // Salaire
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Salaire:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField salaireField = new JTextField(20);
        formPanel.add(salaireField, gbc);
        gbc.weightx = 0;

        // Spécialités
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Spécialités:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField specialitesField = new JTextField(20);
        formPanel.add(specialitesField, gbc);
        gbc.weightx = 0;

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        
        saveButton.addActionListener(e -> {
            try {
                // Validate required fields
                if (nomField.getText().trim().isEmpty() ||
                    prenomField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    telephoneField.getText().trim().isEmpty() ||
                    passwordField.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(dialog,
                        "Veuillez remplir tous les champs obligatoires (*)",
                        "Erreur de validation",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Create ManagerRequestDTO
                ManagerRequestDTO request = ManagerRequestDTO.builder()
                        .nom(nomField.getText().trim())
                        .prenom(prenomField.getText().trim())
                        .email(emailField.getText().trim())
                        .telephone(telephoneField.getText().trim())
                        .password(new String(passwordField.getPassword()))
                        .adresse(adresseField.getText().trim().isEmpty() ? null : adresseField.getText().trim())
                        .salaire(salaireField.getText().trim().isEmpty() ? null : 
                                new BigDecimal(salaireField.getText().trim()))
                        .specialites(specialitesField.getText().trim().isEmpty() ? null : specialitesField.getText().trim())
                        .build();

                // Call service
                managerService.createManager(request);
                
                JOptionPane.showMessageDialog(dialog,
                    "Manager créé avec succès!",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                loadManagers(); // Refresh table
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Le salaire doit être un nombre valide",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur lors de la création: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private void showUpdateManagerDialog() {
        int selectedRow = managerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un manager à modifier",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        ManagerResponseDTO selectedManager = managersList.get(selectedRow);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Modifier Manager", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nom
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nom *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField nomField = new JTextField(selectedManager.getNom());
        formPanel.add(nomField, gbc);
        gbc.weightx = 0;

        // Prénom
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Prénom *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField prenomField = new JTextField(selectedManager.getPrenom());
        formPanel.add(prenomField, gbc);
        gbc.weightx = 0;

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField emailField = new JTextField(selectedManager.getEmail());
        formPanel.add(emailField, gbc);
        gbc.weightx = 0;

        // Téléphone
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Téléphone *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField telephoneField = new JTextField(selectedManager.getTelephone());
        formPanel.add(telephoneField, gbc);
        gbc.weightx = 0;

        // Adresse
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField adresseField = new JTextField(selectedManager.getAdresse());
        formPanel.add(adresseField, gbc);
        gbc.weightx = 0;

        // Salaire
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Salaire:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField salaireField = new JTextField(selectedManager.getSalaire() != null ? 
                                                 selectedManager.getSalaire().toString() : "");
        formPanel.add(salaireField, gbc);
        gbc.weightx = 0;

        // Spécialités
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Spécialités:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField specialitesField = new JTextField(selectedManager.getSpecialites());
        formPanel.add(specialitesField, gbc);
        gbc.weightx = 0;

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        
        saveButton.addActionListener(e -> {
            try {
                if (nomField.getText().trim().isEmpty() ||
                    prenomField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    telephoneField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Veuillez remplir tous les champs obligatoires (*)",
                        "Erreur de validation",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                ManagerRequestDTO request = ManagerRequestDTO.builder()
                        .nom(nomField.getText().trim())
                        .prenom(prenomField.getText().trim())
                        .email(emailField.getText().trim())
                        .telephone(telephoneField.getText().trim())
                        .adresse(adresseField.getText().trim().isEmpty() ? null : adresseField.getText().trim())
                        .salaire(salaireField.getText().trim().isEmpty() ? null : 
                                new BigDecimal(salaireField.getText().trim()))
                        .specialites(specialitesField.getText().trim().isEmpty() ? null : specialitesField.getText().trim())
                        .build();

                managerService.updateManager(selectedManager.getIdManager(), request);
                
                JOptionPane.showMessageDialog(dialog,
                    "Manager modifié avec succès!",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                loadManagers();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Le salaire doit être un nombre valide",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur lors de la modification: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private void showChangePasswordDialog() {
        int selectedRow = managerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un manager",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        ManagerResponseDTO selectedManager = managersList.get(selectedRow);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Changer le mot de passe", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel infoLabel = new JLabel("Manager: " + selectedManager.getNom() + " " + selectedManager.getPrenom());
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(infoLabel, gbc);
        gbc.gridwidth = 1;

        // Old password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Ancien mot de passe *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPasswordField oldPasswordField = new JPasswordField(20);
        formPanel.add(oldPasswordField, gbc);
        gbc.weightx = 0;

        // New password
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Nouveau mot de passe *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPasswordField newPasswordField = new JPasswordField(20);
        formPanel.add(newPasswordField, gbc);
        gbc.weightx = 0;

        // Confirm password
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Confirmer mot de passe *:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPasswordField confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);
        gbc.weightx = 0;

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        
        saveButton.addActionListener(e -> {
            try {
                String oldPassword = new String(oldPasswordField.getPassword());
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Veuillez remplir tous les champs",
                        "Erreur de validation",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(dialog,
                        "Les nouveaux mots de passe ne correspondent pas",
                        "Erreur de validation",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                ChangePasswordDTO passwordDTO = ChangePasswordDTO.builder()
                        .oldPassword(oldPassword)
                        .newPassword(newPassword)
                        .confirmPassword(confirmPassword)
                        .build();

                managerService.changePassword(selectedManager.getIdManager(), passwordDTO);
                
                JOptionPane.showMessageDialog(dialog,
                    "Mot de passe modifié avec succès!",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private void toggleManagerStatus() {
        int selectedRow = managerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un manager",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        ManagerResponseDTO selectedManager = managersList.get(selectedRow);
        String action = selectedManager.getActif() != null && selectedManager.getActif() ? "désactiver" : "activer";
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Voulez-vous " + action + " le manager " + selectedManager.getNom() + " " + selectedManager.getPrenom() + "?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                managerService.toggleManagerStatus(selectedManager.getIdManager());
                JOptionPane.showMessageDialog(this,
                    "Statut du manager modifié avec succès!",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                loadManagers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showReportsDialog() {
        int selectedRow = managerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un manager",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        ManagerResponseDTO selectedManager = managersList.get(selectedRow);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Rapports - " + selectedManager.getNom(), true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Date selection
        JPanel datePanel = new JPanel(new FlowLayout());
        datePanel.add(new JLabel("Date début:"));
        JTextField dateDebutField = new JTextField(10);
        dateDebutField.setText(LocalDate.now().minusDays(7).toString());
        datePanel.add(dateDebutField);
        
        datePanel.add(new JLabel("Date fin:"));
        JTextField dateFinField = new JTextField(10);
        dateFinField.setText(LocalDate.now().toString());
        datePanel.add(dateFinField);

        JButton generateButton = new JButton("Générer Rapport");
        datePanel.add(generateButton);

        mainPanel.add(datePanel, BorderLayout.NORTH);

        // Report display area
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        generateButton.addActionListener(e -> {
            try {
                LocalDate dateDebut = LocalDate.parse(dateDebutField.getText());
                LocalDate dateFin = LocalDate.parse(dateFinField.getText());

                Map<String, Object> rapport = managerService.genererRapportVentes(
                    selectedManager.getIdManager(), dateDebut, dateFin);

                StringBuilder reportText = new StringBuilder();
                reportText.append("=== RAPPORT DE VENTES ===\n\n");
                reportText.append("Période: ").append(dateDebut).append(" à ").append(dateFin).append("\n\n");
                
                for (Map.Entry<String, Object> entry : rapport.entrySet()) {
                    reportText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }

                reportArea.setText(reportText.toString());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur lors de la génération: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // View saved reports
        JButton viewReportsButton = new JButton("Voir Rapports Sauvegardés");
        viewReportsButton.addActionListener(e -> {
            try {
                List<RapportVente> rapports = managerService.getRapportsByManager(selectedManager.getIdManager());
                
                StringBuilder reportsText = new StringBuilder();
                reportsText.append("=== RAPPORTS SAUVEGARDÉS ===\n\n");
                if (rapports.isEmpty()) {
                    reportsText.append("Aucun rapport sauvegardé.\n");
                } else {
                    for (RapportVente rapport : rapports) {
                        reportsText.append("ID: ").append(rapport.getIdRapport()).append("\n");
                        reportsText.append("Période: ").append(rapport.getDateDebut())
                                   .append(" à ").append(rapport.getDateFin()).append("\n");
                        reportsText.append("Chiffre d'affaires: ").append(rapport.getChiffreAffaireTotal()).append("\n");
                        reportsText.append("Commandes: ").append(rapport.getNombreCommandesTotal()).append("\n");
                        reportsText.append("---\n");
                    }
                }
                reportArea.setText(reportsText.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(viewReportsButton);
        JButton closeButton = new JButton("Fermer");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
}

