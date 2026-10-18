package com.rsi.resto.ui;

import com.rsi.resto.service.ClientApiService;
import com.rsi.resto.service.TokenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientPanel extends JPanel {
    private ClientApiService clientService;
    private JTable clientTable;
    private DefaultTableModel tableModel;

    public ClientPanel(TokenManager tokenManager) {
        this.clientService = new ClientApiService(tokenManager);
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Gestion des Clients");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> loadClients());
        headerPanel.add(refreshButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Statut"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        clientTable = new JTable(tableModel);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(clientTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load data
        loadClients();
    }

    private void loadClients() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Clear table
                tableModel.setRowCount(0);

                // Load clients
                var pageable = com.rsi.resto.dtos.PageableDTO.builder()
                        .page(0)
                        .size(10)
                        .build();
                
                var response = clientService.getAllClients(pageable);
                
                if (response.getContent() != null) {
                    for (var client : response.getContent()) {
                        tableModel.addRow(new Object[]{
                            client.getIdClient(),
                            client.getNom(),
                            client.getPrenom(),
                            client.getEmail(),
                            client.getTelephone(),
                            client.getStatut() != null ? client.getStatut().name() : "N/A"
                        });
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des clients: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

