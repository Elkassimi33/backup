package com.fst.rsi.ui.manaer;

import com.rsi.resto.dtos.AuthResponseDTO;
import com.rsi.resto.service.ManagerApiService;
import com.rsi.resto.service.TokenManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class StatisticsPanel extends JPanel {
    private ManagerApiService managerService;
    private JTextArea statsArea;

    public StatisticsPanel(AuthResponseDTO authResponse, TokenManager tokenManager) {
        this.managerService = new ManagerApiService(tokenManager);
        
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("📊 Statistiques");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton dashboardStatsButton = new JButton("📈 Stats Dashboard");
        dashboardStatsButton.addActionListener(e -> loadDashboardStats());
        buttonPanel.add(dashboardStatsButton);
        
        JButton periodStatsButton = new JButton("📅 Stats Période");
        periodStatsButton.addActionListener(e -> showPeriodStatsDialog());
        buttonPanel.add(periodStatsButton);
        
        JButton performanceButton = new JButton("⚡ Performance Globale");
        performanceButton.addActionListener(e -> showPerformanceDialog());
        buttonPanel.add(performanceButton);
        
        JButton compareButton = new JButton("🔄 Comparer Périodes");
        compareButton.addActionListener(e -> showCompareDialog());
        buttonPanel.add(compareButton);
        
        JButton refreshButton = new JButton("🔄 Actualiser");
        refreshButton.addActionListener(e -> loadDashboardStats());
        buttonPanel.add(refreshButton);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Statistics display area
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Courier New", Font.PLAIN, 13));
        statsArea.setBackground(Color.WHITE);
        statsArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JScrollPane scrollPane = new JScrollPane(statsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        add(scrollPane, BorderLayout.CENTER);

        // Load dashboard stats by default
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        new Thread(() -> {
            try {
                Map<String, Object> stats = managerService.getDashboardStats();
                
                SwingUtilities.invokeLater(() -> {
                    StringBuilder statsText = new StringBuilder();
                    statsText.append("=== STATISTIQUES DU TABLEAU DE BORD ===\n\n");
                    statsText.append("Date: ").append(LocalDate.now()).append("\n\n");
                    
                    for (Map.Entry<String, Object> entry : stats.entrySet()) {
                        String key = formatKey(entry.getKey());
                        statsText.append(key).append(": ").append(entry.getValue()).append("\n");
                    }
                    
                    statsArea.setText(statsText.toString());
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statsArea.setText("Erreur lors du chargement des statistiques: " + ex.getMessage() + 
                                    "\n\nVérifiez que le backend est accessible et que vous êtes authentifié.");
                });
            }
        }).start();
    }

    private void showPeriodStatsDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Statistiques par Période", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Date début:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField dateDebutField = new JTextField(LocalDate.now().minusDays(7).toString());
        formPanel.add(dateDebutField, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Date fin:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField dateFinField = new JTextField(LocalDate.now().toString());
        formPanel.add(dateFinField, gbc);
        gbc.weightx = 0;

        JButton loadButton = new JButton("Charger");
        loadButton.addActionListener(e -> {
            try {
                LocalDate dateDebut = LocalDate.parse(dateDebutField.getText());
                LocalDate dateFin = LocalDate.parse(dateFinField.getText());
                
                Map<String, Object> resume = managerService.getResumePeriode(dateDebut, dateFin);
                
                StringBuilder statsText = new StringBuilder();
                statsText.append("=== RÉSUMÉ DE LA PÉRIODE ===\n\n");
                statsText.append("Période: ").append(dateDebut).append(" à ").append(dateFin).append("\n\n");
                
                for (Map.Entry<String, Object> entry : resume.entrySet()) {
                    String key = formatKey(entry.getKey());
                    statsText.append(key).append(": ").append(entry.getValue()).append("\n");
                }
                
                statsArea.setText(statsText.toString());
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loadButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private void showPerformanceDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Performance Globale", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Date début:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField dateDebutField = new JTextField(LocalDate.now().minusDays(30).toString());
        formPanel.add(dateDebutField, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Date fin:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField dateFinField = new JTextField(LocalDate.now().toString());
        formPanel.add(dateFinField, gbc);
        gbc.weightx = 0;

        JButton loadButton = new JButton("Charger");
        loadButton.addActionListener(e -> {
            try {
                LocalDate dateDebut = LocalDate.parse(dateDebutField.getText());
                LocalDate dateFin = LocalDate.parse(dateFinField.getText());
                
                Map<String, Object> performance = managerService.getPerformanceGlobale(dateDebut, dateFin);
                
                StringBuilder statsText = new StringBuilder();
                statsText.append("=== PERFORMANCE GLOBALE ===\n\n");
                statsText.append("Période: ").append(dateDebut).append(" à ").append(dateFin).append("\n\n");
                
                for (Map.Entry<String, Object> entry : performance.entrySet()) {
                    String key = formatKey(entry.getKey());
                    statsText.append(key).append(": ").append(entry.getValue()).append("\n");
                }
                
                statsArea.setText(statsText.toString());
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loadButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private void showCompareDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Comparer Périodes", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel period1Label = new JLabel("Période 1:");
        period1Label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(period1Label, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Début:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField debut1Field = new JTextField(LocalDate.now().minusDays(14).toString());
        formPanel.add(debut1Field, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Fin:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField fin1Field = new JTextField(LocalDate.now().minusDays(7).toString());
        formPanel.add(fin1Field, gbc);
        gbc.weightx = 0;

        JLabel period2Label = new JLabel("Période 2:");
        period2Label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(period2Label, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Début:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField debut2Field = new JTextField(LocalDate.now().minusDays(7).toString());
        formPanel.add(debut2Field, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Fin:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField fin2Field = new JTextField(LocalDate.now().toString());
        formPanel.add(fin2Field, gbc);
        gbc.weightx = 0;

        JButton compareButton = new JButton("Comparer");
        compareButton.addActionListener(e -> {
            try {
                LocalDate debut1 = LocalDate.parse(debut1Field.getText());
                LocalDate fin1 = LocalDate.parse(fin1Field.getText());
                LocalDate debut2 = LocalDate.parse(debut2Field.getText());
                LocalDate fin2 = LocalDate.parse(fin2Field.getText());
                
                Map<String, Object> comparison = managerService.comparerPeriodes(debut1, fin1, debut2, fin2);
                
                StringBuilder statsText = new StringBuilder();
                statsText.append("=== COMPARAISON DE PÉRIODES ===\n\n");
                statsText.append("Période 1: ").append(debut1).append(" à ").append(fin1).append("\n");
                statsText.append("Période 2: ").append(debut2).append(" à ").append(fin2).append("\n\n");
                
                for (Map.Entry<String, Object> entry : comparison.entrySet()) {
                    String key = formatKey(entry.getKey());
                    statsText.append(key).append(": ").append(entry.getValue()).append("\n");
                }
                
                statsArea.setText(statsText.toString());
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erreur: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(compareButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    private String formatKey(String key) {
        // Convert camelCase to readable format
        return key.replaceAll("([a-z])([A-Z])", "$1 $2")
                  .replaceAll("_", " ")
                  .toLowerCase()
                  .substring(0, 1).toUpperCase() + 
                  key.replaceAll("([a-z])([A-Z])", "$1 $2")
                     .replaceAll("_", " ")
                     .toLowerCase()
                     .substring(1);
    }
}

