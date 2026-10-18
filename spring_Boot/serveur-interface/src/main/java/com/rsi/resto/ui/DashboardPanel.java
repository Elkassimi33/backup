package com.rsi.resto.ui;

import com.rsi.resto.dtos.AuthResponseDTO;
import com.rsi.resto.service.ManagerApiService;
import com.rsi.resto.service.TokenManager;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DashboardPanel extends JPanel {
    private ManagerApiService managerService;
    
    public DashboardPanel(AuthResponseDTO authResponse, TokenManager tokenManager) {
        this.managerService = new ManagerApiService(tokenManager);
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header with gradient effect
        JPanel headerPanel = createHeaderPanel(authResponse);
        add(headerPanel, BorderLayout.NORTH);

        // Main content area
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Welcome card (larger)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        JPanel welcomeCard = createWelcomeCard(authResponse);
        contentPanel.add(welcomeCard, gbc);
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        // Stats cards row 1
        gbc.gridx = 0;
        gbc.gridy = 1;
        JPanel statsCard1 = createStatisticsCard(authResponse);
        contentPanel.add(statsCard1, gbc);

        gbc.gridx = 1;
        JPanel statsCard2 = createStatCard("📈 Rapports", "Analyses", 
            "Générez et consultez les rapports", new Color(236, 72, 153));
        contentPanel.add(statsCard2, gbc);

        // Stats cards row 2
        gbc.gridx = 0;
        gbc.gridy = 2;
        JPanel statsCard3 = createAlertsCard(authResponse);
        contentPanel.add(statsCard3, gbc);

        gbc.gridx = 1;
        JPanel statsCard4 = createStatCard("👥 Personnel", "Équipe", 
            "Gérez votre équipe", new Color(34, 197, 94));
        contentPanel.add(statsCard4, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel(AuthResponseDTO authResponse) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Tableau de bord");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(30, 41, 59));
        header.add(titleLabel, BorderLayout.WEST);

        // Date label
        JLabel dateLabel = new JLabel(java.time.LocalDate.now().toString());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(100, 116, 139));
        header.add(dateLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createWelcomeCard(AuthResponseDTO authResponse) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 255, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Add shadow effect using multiple borders
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Left side - Welcome message
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        String fullName = authResponse.getFullName() != null ? authResponse.getFullName() : 
                         (authResponse.getNom() != null && authResponse.getPrenom() != null ? 
                          authResponse.getNom() + " " + authResponse.getPrenom() : 
                          "Utilisateur");

        JLabel welcomeLabel = new JLabel("👋 Bienvenue, " + fullName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(30, 41, 59));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        leftPanel.add(welcomeLabel, gbc);

        JLabel emailLabel = new JLabel("📧 " + (authResponse.getEmail() != null ? authResponse.getEmail() : "N/A"));
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailLabel.setForeground(new Color(100, 116, 139));
        gbc.gridy = 1;
        leftPanel.add(emailLabel, gbc);

        if (authResponse.getRoles() != null && !authResponse.getRoles().isEmpty()) {
            JLabel rolesLabel = new JLabel("🔑 Rôles: " + authResponse.getRoles().toString().replaceAll("[\\[\\]]", ""));
            rolesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            rolesLabel.setForeground(new Color(100, 116, 139));
            gbc.gridy = 2;
            leftPanel.add(rolesLabel, gbc);
        }

        card.add(leftPanel, BorderLayout.WEST);

        // Right side - Quick info
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        rightPanel.setOpaque(false);

        JLabel infoLabel1 = new JLabel("🕐 " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        infoLabel1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        infoLabel1.setForeground(new Color(99, 102, 241));
        infoLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(infoLabel1);

        JLabel infoLabel2 = new JLabel("Système opérationnel");
        infoLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel2.setForeground(new Color(100, 116, 139));
        infoLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(infoLabel2);

        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    private JPanel createStatCard(String iconTitle, String title, String description, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(300, 200));

        // Top section with icon and title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(iconTitle);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(accentColor);
        topPanel.add(iconLabel, BorderLayout.WEST);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 41, 59));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.NORTH);

        // Description
        JLabel descLabel = new JLabel("<html><div style='width:200px'>" + description + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(100, 116, 139));
        descLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        card.add(descLabel, BorderLayout.CENTER);

        // Bottom accent line
        JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(0, 4));
        card.add(accentLine, BorderLayout.SOUTH);

        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    BorderFactory.createEmptyBorder(23, 23, 23, 23)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
            }
        });

        return card;
    }

    private JPanel createStatisticsCard(AuthResponseDTO authResponse) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(300, 200));

        // Top section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel iconLabel = new JLabel("📊 Statistiques");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(new Color(99, 102, 241));
        topPanel.add(iconLabel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Vue d'ensemble");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 41, 59));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.NORTH);

        // Stats display
        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel statsLabel = new JLabel("Chargement...");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statsLabel.setForeground(new Color(100, 116, 139));
        statsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statsPanel.add(statsLabel);

        JLabel clickLabel = new JLabel("Cliquez pour voir les détails");
        clickLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        clickLabel.setForeground(new Color(100, 116, 139));
        clickLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statsPanel.add(clickLabel);

        card.add(statsPanel, BorderLayout.CENTER);

        // Load stats in background
        new Thread(() -> {
            try {
                var stats = managerService.getDashboardStats();
                SwingUtilities.invokeLater(() -> {
                    if (stats != null && !stats.isEmpty()) {
                        // Show first few key stats
                        StringBuilder statsText = new StringBuilder();
                        int count = 0;
                        for (Map.Entry<String, Object> entry : stats.entrySet()) {
                            if (count < 2) {
                                String key = entry.getKey().length() > 15 ? 
                                           entry.getKey().substring(0, 15) + "..." : entry.getKey();
                                statsText.append(key).append(": ").append(entry.getValue()).append("<br/>");
                                count++;
                            }
                        }
                        if (statsText.length() > 0) {
                            statsLabel.setText("<html><div style='text-align:center'>" + statsText.toString() + "</div></html>");
                        } else {
                            statsLabel.setText("Statistiques disponibles");
                        }
                    } else {
                        statsLabel.setText("Aucune statistique");
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statsLabel.setText("Erreur de chargement");
                });
            }
        }).start();

        // Bottom accent line
        JPanel accentLine = new JPanel();
        accentLine.setBackground(new Color(99, 102, 241));
        accentLine.setPreferredSize(new Dimension(0, 4));
        card.add(accentLine, BorderLayout.SOUTH);

        // Make clickable
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(99, 102, 241), 2),
                    BorderFactory.createEmptyBorder(23, 23, 23, 23)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Container parent = DashboardPanel.this.getParent();
                while (parent != null && !(parent instanceof MainWindow)) {
                    parent = parent.getParent();
                }
                if (parent instanceof MainWindow) {
                    ((MainWindow) parent).showCard("statistics");
                }
            }
        });

        return card;
    }

    private JPanel createAlertsCard(AuthResponseDTO authResponse) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(300, 200));

        // Top section with icon and title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel iconLabel = new JLabel("⚠️ Alertes");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(new Color(251, 146, 60));
        topPanel.add(iconLabel, BorderLayout.WEST);

        // Title
        JLabel titleLabel = new JLabel("Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 41, 59));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.NORTH);

        // Alert count
        JLabel countLabel = new JLabel("Chargement...");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        countLabel.setForeground(new Color(251, 146, 60));
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        card.add(countLabel, BorderLayout.CENTER);

        // Description
        JLabel descLabel = new JLabel("<html><div style='width:200px; text-align:center'>Alertes non traitées</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(100, 116, 139));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);

        // Load alert count in background
        new Thread(() -> {
            try {
                var alertes = managerService.getAlertesNonTraitees();
                int count = alertes != null ? alertes.size() : 0;
                SwingUtilities.invokeLater(() -> {
                    countLabel.setText(String.valueOf(count));
                    if (count > 0) {
                        countLabel.setForeground(new Color(239, 68, 68)); // Red for urgent
                    } else {
                        countLabel.setForeground(new Color(34, 197, 94)); // Green for no alerts
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    countLabel.setText("?");
                    countLabel.setForeground(new Color(100, 116, 139));
                });
            }
        }).start();

        // Bottom accent line
        JPanel accentLine = new JPanel();
        accentLine.setBackground(new Color(251, 146, 60));
        accentLine.setPreferredSize(new Dimension(0, 4));
        card.add(accentLine, BorderLayout.SOUTH);

        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(251, 146, 60), 2),
                    BorderFactory.createEmptyBorder(23, 23, 23, 23)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
            }
        });

        return card;
    }
}

