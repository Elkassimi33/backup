package com.fst.rsi.ui.client;


import com.fst.rsi.dtos.ClientResponseDTO;
import com.fst.rsi.service.ClientApiService;
import com.fst.rsi.service.TokenManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ProfilePanel extends JPanel {
    private final RestaurantAppUI app;
    private final ClientApiService clientService;

    private JLabel lblProfName, lblProfEmail, lblProfPoints, lblProfTel, lblProfAdresse;
    private JTextField txtUsePoints;
    private JButton btnUsePoints, btnLogout;

    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color WARNING_COLOR = new Color(230, 126, 34);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);

    public ProfilePanel(RestaurantAppUI app, TokenManager tokenManager) {
        this.app = app;
        this.clientService = new ClientApiService(tokenManager);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Search bar with logout
        JPanel searchPanel = createSearchPanel();
        JPanel profileCard = createProfileCard();

        add(searchPanel, BorderLayout.NORTH);
        add(profileCard, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(Color.WHITE);

        // Logout button
        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(DANGER_COLOR);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(new EmptyBorder(5, 15, 5, 15));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setOpaque(true);
        btnLogout.addActionListener(e -> handleLogout());

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(Color.WHITE);
        logoutPanel.add(btnLogout);

        panel.add(logoutPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createProfileCard() {
        JPanel card = new JPanel(new BorderLayout(20, 20));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(LIGHT_GRAY, 2),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // Client info panel
        JPanel infoPanel = createInfoPanel();

        // Loyalty panel
        JPanel loyaltyPanel = createLoyaltyPanel();

        card.add(infoPanel, BorderLayout.WEST);
        card.add(loyaltyPanel, BorderLayout.EAST);

        return card;
    }

    private JPanel createInfoPanel()  {

        ClientResponseDTO client = app.getCurrentClient();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 0, 8, 0);

        lblProfName = new JLabel(client.getPrenom()+" "+client.getNom());
        lblProfName.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblProfName.setForeground(PRIMARY_COLOR);

        lblProfEmail = new JLabel(client.getEmail());
        lblProfEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblProfEmail.setForeground(Color.GRAY);

        lblProfTel = new JLabel("Phone: ---"+client.getTelephone());
        lblProfTel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        lblProfAdresse = new JLabel("Address: ---"+client.getAdresse());
        lblProfAdresse.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblProfName, gbc);
        gbc.gridy = 1;
        panel.add(lblProfEmail, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 0, 5, 0);
        panel.add(lblProfTel, gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 8, 0);
        panel.add(lblProfAdresse, gbc);

        return panel;
    }

    private JPanel createLoyaltyPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(LIGHT_GRAY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(WARNING_COLOR, 2),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblLoyaltyTitle = new JLabel("💎 Loyalty Points");
        lblLoyaltyTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));

        lblProfPoints = new JLabel("0 pts");
        lblProfPoints.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblProfPoints.setForeground(WARNING_COLOR);

        JPanel usePointsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        usePointsPanel.setOpaque(false);

        JLabel lblUse = new JLabel("Use points:");
        lblUse.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        txtUsePoints = new JTextField(8);
        txtUsePoints.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsePoints.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(6, 8, 6, 8)
        ));

        btnUsePoints = new JButton("Redeem");
        btnUsePoints.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnUsePoints.setBackground(WARNING_COLOR);
        btnUsePoints.setForeground(Color.WHITE);
        btnUsePoints.setFocusPainted(false);
        btnUsePoints.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnUsePoints.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnUsePoints.setContentAreaFilled(false);
        btnUsePoints.setOpaque(true);
        btnUsePoints.addActionListener(e -> handleUsePoints());

        usePointsPanel.add(lblUse);
        usePointsPanel.add(txtUsePoints);
        usePointsPanel.add(btnUsePoints);

        JPanel loyaltyContent = new JPanel(new BorderLayout(10, 10));
        loyaltyContent.setOpaque(false);
        loyaltyContent.add(lblLoyaltyTitle, BorderLayout.NORTH);
        loyaltyContent.add(lblProfPoints, BorderLayout.CENTER);
        loyaltyContent.add(usePointsPanel, BorderLayout.SOUTH);

        panel.add(loyaltyContent);
        return panel;
    }

    public void handleLoadProfile() {
        try {
            Long currentClientId = app.getCurrentClient().getIdClient();
            if (currentClientId == null) {
                JOptionPane.showMessageDialog(app,
                        "Please register first!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                app.getTabbedPane().setSelectedIndex(0);
                return;
            }

            ClientResponseDTO client = clientService.getClientById(currentClientId);

            lblProfName.setText(client.getPrenom() + " " + client.getNom());
            lblProfEmail.setText(client.getEmail());
            lblProfTel.setText("Phone: " + client.getTelephone());
            lblProfAdresse.setText("Address: " + (client.getAdresse() != null ? client.getAdresse() : "N/A"));
            lblProfPoints.setText(client.getPointsFidelite() + " pts");

            JOptionPane.showMessageDialog(app,
                    "Profile loaded successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(app,
                    "Failed to load profile: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUsePoints() {
        try {
            ClientResponseDTO currentClient = app.getCurrentClient();
            if (currentClient == null) {
                JOptionPane.showMessageDialog(app,
                        "Please register first!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                app.getTabbedPane().setSelectedIndex(0);
                return;
            }

            String pointsText = txtUsePoints.getText().trim();
            if (pointsText.isEmpty()) {
                JOptionPane.showMessageDialog(app,
                        "Please enter points amount",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer points = Integer.parseInt(pointsText);
            clientService.utiliserPoints(currentClient.getIdClient(), points);

            handleLoadProfile();

            JOptionPane.showMessageDialog(app,
                    "Points redeemed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            txtUsePoints.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(app,
                    "Invalid points format",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(app,
                    "Failed to use points: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(app,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            app.dispose();
            app.setCurrentClient(null);

            // Reset UI
            lblProfName.setText("--- ---");
            lblProfEmail.setText("email@example.com");
            lblProfTel.setText("Phone: ---");
            lblProfAdresse.setText("Address: ---");
            lblProfPoints.setText("0 pts");

            // Hide all tabs except registration
            for (int i = 1; i < app.getTabbedPane().getTabCount(); i++) {
                app.getTabbedPane().setEnabledAt(i, false);
            }

            app.getTabbedPane().setSelectedIndex(0);

            JOptionPane.showMessageDialog(app,
                    "Logged out successfully!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}