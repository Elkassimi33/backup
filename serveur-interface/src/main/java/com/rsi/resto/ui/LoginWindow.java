package com.rsi.resto.ui;

import com.rsi.resto.dtos.AuthResponseDTO;
import com.rsi.resto.dtos.LoginRequestDTO;
import com.rsi.resto.service.AuthApiService;
import com.rsi.resto.service.TokenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private AuthApiService authService;
    private TokenManager tokenManager;
    private LoginCallback callback;

    public interface LoginCallback {
        void onLoginSuccess(AuthResponseDTO authResponse, TokenManager tokenManager);
    }

    public LoginWindow(LoginCallback callback) {
        this.callback = callback;
        this.authService = new AuthApiService();
        this.tokenManager = new TokenManager();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Connexion - Restaurant Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Se connecter");
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Email field
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(emailField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(passwordField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton.setPreferredSize(new Dimension(0, 35));
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(loginButton, gbc);
        gbc.gridwidth = 1;

        // Status label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(statusLabel, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Enter key on password field
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Veuillez remplir tous les champs");
            return;
        }

        // Disable button and show loading
        loginButton.setEnabled(false);
        statusLabel.setText("Connexion en cours...");
        statusLabel.setForeground(Color.BLUE);

        // Perform login in background thread (not EDT)
        new Thread(() -> {
            try {
                LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                        .email(email)
                        .password(password)
                        .build();

                AuthResponseDTO authResponse = authService.login(loginRequest);
                tokenManager.setTokens(authResponse);

                // Success - call callback on EDT
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (callback != null) {
                            callback.onLoginSuccess(authResponse, tokenManager);
                        } else {
                            // If no callback, create MainWindow directly
                            MainWindow mainWindow = new MainWindow(authResponse, tokenManager);
                            mainWindow.setVisible(true);
                        }
                        dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        statusLabel.setText("Erreur lors de l'ouverture: " + ex.getMessage());
                        statusLabel.setForeground(Color.RED);
                        loginButton.setEnabled(true);
                    }
                });

            } catch (Exception ex) {
                // Error - update UI on EDT
                SwingUtilities.invokeLater(() -> {
                    ex.printStackTrace();
                    statusLabel.setText("Erreur: " + ex.getMessage());
                    statusLabel.setForeground(Color.RED);
                    loginButton.setEnabled(true);
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginWindow(null).setVisible(true);
        });
    }
}

