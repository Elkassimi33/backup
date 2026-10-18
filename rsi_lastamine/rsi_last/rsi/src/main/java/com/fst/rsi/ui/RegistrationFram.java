package com.fst.rsi.ui;

import com.fst.rsi.dtos.*;
import com.fst.rsi.service.AuthApiService;
import com.fst.rsi.service.TokenManager;
import com.fst.rsi.ui.client.RestaurantAppUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RegistrationFram extends JFrame {
    private final AuthApiService authApiService;
    private JTextField regNom, regPrenom, regEmail, regTel, regAdresse, regPref;
    private JPasswordField regPass;
    private JButton btnRegister;

    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(230, 126, 34);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);
    private static final Color CART_COLOR = new Color(241, 196, 15);
    private static final Color INFO_COLOR = new Color(41, 128, 185);

    public RegistrationFram() {
        this.authApiService = new AuthApiService();
        setTitle("regester page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,800);
        setLocationRelativeTo(null);
        createPanel();
    }

    private void createPanel() {
        // Set layout for this panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(LIGHT_GRAY, 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("New Client Registration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Form fields
        regNom = addStyledFormRow(formPanel, "Last Name *", 1, gbc);
        regPrenom = addStyledFormRow(formPanel, "First Name *", 2, gbc);
        regEmail = addStyledFormRow(formPanel, "Email *", 3, gbc);
        regTel = addStyledFormRow(formPanel, "Phone Number *", 4, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel lblPass = new JLabel("Password *");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(lblPass, gbc);

        regPass = new JPasswordField(20);
        regPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        regPass.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(LIGHT_GRAY, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        formPanel.add(regPass, gbc);

        regAdresse = addStyledFormRow(formPanel, "Address", 6, gbc);
        regPref = addStyledFormRow(formPanel, "Food Preferences", 7, gbc);

        // Submit button
        btnRegister = new JButton("Create Account");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegister.setBackground(SUCCESS_COLOR);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(new EmptyBorder(12, 30, 12, 30));
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> handleRegistration());

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        formPanel.add(btnRegister, gbc);

        // Required fields note
        JLabel note = new JLabel("* Required fields");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(Color.GRAY);
        gbc.gridy = 9;
        gbc.insets = new Insets(10, 10, 0, 10);
        formPanel.add(note, gbc);

        mainPanel.add(formPanel);

        // Add mainPanel to this panel
        panel.add(mainPanel, BorderLayout.CENTER);
        add(panel);

    }

    private JTextField addStyledFormRow(JPanel panel, String label, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lbl, gbc);

        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(LIGHT_GRAY, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        panel.add(field, gbc);

        return field;
    }

    private void handleRegistration() {
        btnRegister.setEnabled(false);
        btnRegister.setText("Registering...");

        new Thread(() -> {
            try {
                if (regNom.getText().trim().isEmpty() ||
                        regPrenom.getText().trim().isEmpty() ||
                        regEmail.getText().trim().isEmpty() ||
                        regTel.getText().trim().isEmpty() ||
                        new String(regPass.getPassword()).trim().isEmpty()) {

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "Please fill all required fields (*)",
                                "Validation Error",
                                JOptionPane.WARNING_MESSAGE);
                        btnRegister.setEnabled(true);
                        btnRegister.setText("Create Account");
                    });
                    return;
                }

                ClientRequestDTO dto = ClientRequestDTO.builder()
                        .nom(regNom.getText().trim())
                        .prenom(regPrenom.getText().trim())
                        .email(regEmail.getText().trim())
                        .telephone(regTel.getText().trim())
                        .password(new String(regPass.getPassword()))
                        .adresse(regAdresse.getText().trim())
                        .preferences(regPref.getText().trim())
                        .build();

                AuthResponseDTO response = authApiService.register(dto);

                TokenManager tokenManager = new TokenManager();
                tokenManager.setTokens(response);
                System.out.println(tokenManager);
                System.out.println(response);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Registration successful!\nYour Client ID: " + response.getId(),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    clearForm();

                    RestaurantAppUI mainWindow = new RestaurantAppUI(tokenManager, response);
                    mainWindow.setVisible(true);

                    dispose();
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Registration failed: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                    btnRegister.setEnabled(true);
                    btnRegister.setText("Create Account");
                });
            }
        }).start();
    }

    private void clearForm() {
        regNom.setText("");
        regPrenom.setText("");
        regEmail.setText("");
        regTel.setText("");
        regPass.setText("");
        regAdresse.setText("");
        regPref.setText("");
    }
}