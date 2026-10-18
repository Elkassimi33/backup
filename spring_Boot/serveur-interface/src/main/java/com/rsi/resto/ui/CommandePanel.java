package com.rsi.resto.ui;

import com.rsi.resto.dtos.CommandeResponseDTO;
import com.rsi.resto.enums.StatutCommande;
import com.rsi.resto.service.CommandeApiService;
import com.rsi.resto.service.TokenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommandePanel extends JPanel {
    private CommandeApiService commandeService;
    private JTable commandeTable;
    private DefaultTableModel tableModel;
    private List<CommandeResponseDTO> commandesList = new ArrayList<>();

    public CommandePanel(TokenManager tokenManager) {
        this.commandeService = new CommandeApiService(tokenManager);
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Gestion des Commandes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton modifyButton = new JButton("Modifier Statut");
        modifyButton.addActionListener(e -> showModifyCommandeDialog());
        buttonPanel.add(modifyButton);
        
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> loadCommandes());
        buttonPanel.add(refreshButton);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Client", "Serveur", "Type", "Statut", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        commandeTable = new JTable(tableModel);
        commandeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        commandeTable.getSelectionModel().addListSelectionListener(e -> {
            // Enable/disable modify button based on selection
        });
        JScrollPane scrollPane = new JScrollPane(commandeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load data
        loadCommandes();
    }

    private void loadCommandes() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Clear table
                tableModel.setRowCount(0);

                // Load commandes
                var pageable = com.rsi.resto.dtos.PageableDTO.builder()
                        .page(0)
                        .size(10)
                        .build();
                
                var response = commandeService.getAllCommandes(pageable);
                
                commandesList.clear();
                if (response.getContent() != null) {
                    commandesList.addAll(response.getContent());
                    for (var commande : response.getContent()) {
                        String clientInfo = "N/A";
                        if (commande.getClient() != null && commande.getClient().getIdClient() != null) {
                            clientInfo = commande.getClient().getIdClient().toString();
                        }
                        
                        String serveurInfo = "N/A";
                        if (commande.getServeur() != null && commande.getServeur().getIdServeur() != null) {
                            serveurInfo = commande.getServeur().getIdServeur().toString();
                        }
                        
                        tableModel.addRow(new Object[]{
                            commande.getIdCommande(),
                            clientInfo,
                            serveurInfo,
                            commande.getTypeCommande() != null ? commande.getTypeCommande().name() : "N/A",
                            commande.getStatut() != null ? commande.getStatut().name() : "N/A",
                            commande.getMontantTotal() != null ? commande.getMontantTotal().toString() : "0.00"
                        });
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des commandes: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void showModifyCommandeDialog() {
        int selectedRow = commandeTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une commande à modifier",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        CommandeResponseDTO selectedCommande = commandesList.get(selectedRow);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Modifier Commande", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Display commande info
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><b>Commande ID:</b> " + selectedCommande.getIdCommande() + 
                                     "<br/><b>Client:</b> " + (selectedCommande.getClient() != null ? 
                                     selectedCommande.getClient().getIdClient() : "N/A") +
                                     "<br/><b>Total:</b> " + (selectedCommande.getMontantTotal() != null ? 
                                     selectedCommande.getMontantTotal() : "0.00") + "</html>");
        formPanel.add(infoLabel, gbc);
        gbc.gridwidth = 1;

        // Current status
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Statut actuel:"), gbc);
        gbc.gridx = 1;
        JLabel currentStatusLabel = new JLabel(selectedCommande.getStatut() != null ? 
                                              selectedCommande.getStatut().name() : "N/A");
        currentStatusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(currentStatusLabel, gbc);

        // New status
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Nouveau statut *:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JComboBox<StatutCommande> statusCombo = new JComboBox<>(StatutCommande.values());
        if (selectedCommande.getStatut() != null) {
            statusCombo.setSelectedItem(selectedCommande.getStatut());
        }
        formPanel.add(statusCombo, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        
        saveButton.addActionListener(e -> {
            try {
                StatutCommande newStatut = (StatutCommande) statusCombo.getSelectedItem();
                
                if (newStatut == null) {
                    JOptionPane.showMessageDialog(dialog,
                        "Veuillez sélectionner un statut",
                        "Erreur de validation",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Update status
                commandeService.updateStatut(selectedCommande.getIdCommande(), newStatut);
                
                JOptionPane.showMessageDialog(dialog,
                    "Statut de la commande mis à jour avec succès!",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                loadCommandes(); // Refresh table
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur lors de la mise à jour: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }
}

