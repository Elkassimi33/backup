package com.rsi.resto.ui;

import com.rsi.resto.dtos.AlerteStock;
import com.rsi.resto.dtos.AuthResponseDTO;
import com.rsi.resto.enums.StatutAlerte;
import com.rsi.resto.service.ManagerApiService;
import com.rsi.resto.service.TokenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AlertePanel extends JPanel {
    private ManagerApiService managerService;
    private JTable alerteTable;
    private DefaultTableModel tableModel;
    private List<AlerteStock> alertesList = new ArrayList<>();
    private AuthResponseDTO authResponse;

    public AlertePanel(AuthResponseDTO authResponse, TokenManager tokenManager) {
        this.authResponse = authResponse;
        this.managerService = new ManagerApiService(tokenManager);
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("⚠️ Gestion des Alertes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton traiterButton = new JButton("✅ Traiter Alerte");
        traiterButton.addActionListener(e -> showTraiterAlerteDialog());
        buttonPanel.add(traiterButton);
        
        JButton refreshButton = new JButton("🔄 Actualiser");
        refreshButton.addActionListener(e -> loadAlertes());
        buttonPanel.add(refreshButton);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Type", "Message", "Date Alerte", "Statut", "Traité par"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        alerteTable = new JTable(tableModel);
        alerteTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        alerteTable.setRowHeight(30);
        alerteTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Color rows based on status
        alerteTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected && row < alertesList.size() && row >= 0) {
                    AlerteStock alerte = alertesList.get(row);
                    if (alerte.getStatut() == StatutAlerte.NON_TRAITEE) {
                        c.setBackground(new Color(254, 242, 242)); // Light red
                    } else if (alerte.getStatut() == StatutAlerte.EN_COURS) {
                        c.setBackground(new Color(255, 251, 235)); // Light yellow
                    } else if (alerte.getStatut() == StatutAlerte.TRAITEE) {
                        c.setBackground(new Color(240, 253, 244)); // Light green
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else if (isSelected) {
                    c.setBackground(new Color(200, 220, 255));
                } else {
                    c.setBackground(Color.WHITE);
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(alerteTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        add(scrollPane, BorderLayout.CENTER);

        // Status label
        JLabel statusLabel = new JLabel("Chargement...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        statusLabel.setForeground(new Color(100, 116, 139));
        add(statusLabel, BorderLayout.SOUTH);

        // Load data
        loadAlertes();
    }

    private void loadAlertes() {
        new Thread(() -> {
            try {
                List<AlerteStock> alertes = managerService.getAlertesNonTraitees();
                
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    alertesList.clear();
                    alertesList.addAll(alertes);
                    
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    
                    for (AlerteStock alerte : alertes) {
                        String dateStr = alerte.getDateAlerte() != null ? 
                                       alerte.getDateAlerte().format(formatter) : "N/A";
                        String statutStr = alerte.getStatut() != null ? 
                                         alerte.getStatut().name() : "N/A";
                        String traitePar = alerte.getTraiteePar() != null ? 
                                         alerte.getTraiteePar() : "-";
                        
                        tableModel.addRow(new Object[]{
                            alerte.getIdAlerte(),
                            alerte.getTypeAlerte() != null ? alerte.getTypeAlerte().name() : "N/A",
                            alerte.getMessage() != null ? alerte.getMessage() : "Alerte de stock",
                            dateStr,
                            statutStr,
                            traitePar
                        });
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Erreur lors du chargement des alertes: " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void showTraiterAlerteDialog() {
        int selectedRow = alerteTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une alerte à traiter",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        AlerteStock selectedAlerte = alertesList.get(selectedRow);
        
        if (selectedAlerte.getStatut() == StatutAlerte.TRAITEE) {
            JOptionPane.showMessageDialog(this,
                "Cette alerte est déjà traitée",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Traiter Alerte", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Alerte info
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><b>Alerte ID:</b> " + selectedAlerte.getIdAlerte() + 
                                     "<br/><b>Type:</b> " + (selectedAlerte.getTypeAlerte() != null ? 
                                     selectedAlerte.getTypeAlerte().name() : "N/A") +
                                     "<br/><b>Message:</b> " + (selectedAlerte.getMessage() != null ? 
                                     selectedAlerte.getMessage() : "N/A") + "</html>");
        formPanel.add(infoLabel, gbc);
        gbc.gridwidth = 1;

        // Action prise
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Action prise *:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextArea actionField = new JTextArea(3, 20);
        actionField.setLineWrap(true);
        actionField.setWrapStyleWord(true);
        JScrollPane actionScroll = new JScrollPane(actionField);
        formPanel.add(actionScroll, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        // Nouveau statut
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Nouveau statut *:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JComboBox<StatutAlerte> statutCombo = new JComboBox<>(StatutAlerte.values());
        statutCombo.setSelectedItem(StatutAlerte.TRAITEE);
        formPanel.add(statutCombo, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        
        saveButton.addActionListener(e -> {
            try {
                String actionPrise = actionField.getText().trim();
                StatutAlerte nouveauStatut = (StatutAlerte) statutCombo.getSelectedItem();
                
                if (actionPrise.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Veuillez décrire l'action prise",
                        "Erreur de validation",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (nouveauStatut == null) {
                    JOptionPane.showMessageDialog(dialog,
                        "Veuillez sélectionner un statut",
                        "Erreur de validation",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (authResponse.getId() == null) {
                    JOptionPane.showMessageDialog(dialog,
                        "ID manager non disponible",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Traiter l'alerte
                managerService.traiterAlerte(
                    authResponse.getId(),
                    selectedAlerte.getIdAlerte(),
                    actionPrise,
                    nouveauStatut
                );
                
                JOptionPane.showMessageDialog(dialog,
                    "Alerte traitée avec succès!",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                loadAlertes(); // Refresh table
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur lors du traitement: " + ex.getMessage(),
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

