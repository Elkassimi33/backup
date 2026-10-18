package com.rsi.resto.ui;

import com.rsi.resto.dtos.AuthResponseDTO;
import com.rsi.resto.service.TokenManager;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private TokenManager tokenManager;
    private AuthResponseDTO authResponse;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainWindow(AuthResponseDTO authResponse, TokenManager tokenManager) {
        this.authResponse = authResponse;
        this.tokenManager = tokenManager;
        
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        String userName = authResponse.getFullName() != null ? authResponse.getFullName() : 
                         (authResponse.getNom() != null && authResponse.getPrenom() != null ? 
                          authResponse.getNom() + " " + authResponse.getPrenom() : 
                          authResponse.getEmail() != null ? authResponse.getEmail() : "Utilisateur");
        setTitle("Restaurant Management System - " + userName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create side menu
        JPanel sideMenu = createSideMenu();
        add(sideMenu, BorderLayout.WEST);

        // Create content panels
        contentPanel.add(new DashboardPanel(authResponse, tokenManager), "dashboard");
        contentPanel.add(new StatisticsPanel(authResponse, tokenManager), "statistics");
        contentPanel.add(new ManagerPanel(tokenManager), "managers");
        contentPanel.add(new ClientPanel(tokenManager), "clients");
        contentPanel.add(new CommandePanel(tokenManager), "commandes");
        contentPanel.add(new AlertePanel(authResponse, tokenManager), "alertes");

        add(contentPanel, BorderLayout.CENTER);

        // Show dashboard by default
        cardLayout.show(contentPanel, "dashboard");
    }

    private JPanel createSideMenu() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuPanel.setBackground(new Color(240, 240, 240));
        menuPanel.setPreferredSize(new Dimension(200, 0));

        // User info
        String fullName = authResponse.getFullName() != null ? authResponse.getFullName() : 
                         (authResponse.getNom() != null && authResponse.getPrenom() != null ? 
                          authResponse.getNom() + " " + authResponse.getPrenom() : "Utilisateur");
        String email = authResponse.getEmail() != null ? authResponse.getEmail() : "N/A";
        JLabel userLabel = new JLabel("<html><b>" + fullName + "</b><br/>" + email + "</html>");
        userLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        menuPanel.add(userLabel);
        menuPanel.add(new JSeparator());

        // Menu buttons
        addMenuButton(menuPanel, "Dashboard", "dashboard", true);
        addMenuButton(menuPanel, "Statistiques", "statistics", false);
        addMenuButton(menuPanel, "Managers", "managers", false);
        addMenuButton(menuPanel, "Clients", "clients", false);
        addMenuButton(menuPanel, "Commandes", "commandes", false);
        addMenuButton(menuPanel, "Alertes", "alertes", false);

        menuPanel.add(Box.createVerticalGlue());

        // Logout button
        JButton logoutButton = new JButton("Déconnexion");
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment vous déconnecter?",
                "Déconnexion",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                tokenManager.clearTokens();
                dispose();
                new LoginWindow((authResponse1, tokenManager1) -> {
                    new MainWindow(authResponse1, tokenManager1).setVisible(true);
                }).setVisible(true);
            }
        });
        menuPanel.add(logoutButton);

        return menuPanel;
    }

    private void addMenuButton(JPanel panel, String text, String cardName, boolean selected) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setPreferredSize(new Dimension(180, 40));
        if (selected) {
            button.setBackground(new Color(200, 220, 255));
        }
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, cardName);
            // Update button styles
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JButton && comp != button) {
                    comp.setBackground(null);
                }
            }
            button.setBackground(new Color(200, 220, 255));
        });
        panel.add(button);
        panel.add(Box.createVerticalStrut(5));
    }

    public void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
        // Update button styles in side menu
        JPanel sideMenu = (JPanel) ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.WEST);
        if (sideMenu != null) {
            for (Component comp : sideMenu.getComponents()) {
                if (comp instanceof JButton) {
                    comp.setBackground(null);
                }
            }
            // Find and highlight the correct button
            for (Component comp : sideMenu.getComponents()) {
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    String btnText = btn.getText().toLowerCase();
                    if ((cardName.equals("dashboard") && btnText.contains("dashboard")) ||
                        (cardName.equals("statistics") && btnText.contains("statistiques")) ||
                        (cardName.equals("managers") && btnText.contains("managers")) ||
                        (cardName.equals("clients") && btnText.contains("clients")) ||
                        (cardName.equals("commandes") && btnText.contains("commandes")) ||
                        (cardName.equals("alertes") && btnText.contains("alertes"))) {
                        btn.setBackground(new Color(200, 220, 255));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        // For testing only
        SwingUtilities.invokeLater(() -> {
            TokenManager tm = new TokenManager();
            AuthResponseDTO auth = AuthResponseDTO.builder()
                    .fullName("Test User")
                    .email("test@test.com")
                    .build();
            new MainWindow(auth, tm).setVisible(true);
        });
    }
}

