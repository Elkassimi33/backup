package com.fst.rsi.ui;

import com.fst.rsi.dtos.AuthResponseDTO;
import com.fst.rsi.dtos.LoginRequestDTO;
import com.fst.rsi.service.AuthApiService;
import com.fst.rsi.service.TokenManager;
import com.fst.rsi.ui.client.RestaurantAppUI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {

    private final AuthApiService authApiService=new AuthApiService();
    private final TokenManager tokenManager=new TokenManager();

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    private JButton signUpButton;
    private JLabel statusLabel;
    private final LoginCallback callback;

    public interface LoginCallback {
        void onLoginSuccess(AuthResponseDTO authResponse, TokenManager tokenManager);
    }
    
    public Login(LoginCallback callback) {
        this.callback=callback;
        setTitle("Login page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,800);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        addListeners();
    }
    private void initComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        resetButton = new JButton("Reset");
        signUpButton = new JButton("Sign up");
        statusLabel = new JLabel("Enter your username and password");
        statusLabel.setForeground(Color.BLUE);
        
        loginButton.setBackground(new Color(70,130,180));
        loginButton.setForeground(Color.WHITE);
        resetButton.setBackground(new Color(220,220,220));
        
    }
    private void layoutComponents() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new  GridBagConstraints();
    gbc.insets = new Insets(10,10,10,10);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    JLabel titleLabel = new JLabel("Login Form");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    panel.add(titleLabel, gbc);
    
    gbc.gridwidth = 1;
    gbc.gridy = 1;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.EAST;
    panel.add(new JLabel("Username:"), gbc);
    
     gbc.gridx = 1;
     gbc.anchor  = GridBagConstraints.WEST;
     panel.add(usernameField, gbc);
     
     gbc.gridy = 2;
     gbc.gridx=0;
     panel.add(new JLabel("password:"), gbc);
     
     gbc.gridx = 1;
     gbc.anchor  = GridBagConstraints.WEST;
     panel.add(passwordField, gbc);
     
     gbc.gridy = 3;
     gbc.gridx=0;
     panel.add(loginButton, gbc);
     
     gbc.gridx =1;
     panel.add(resetButton, gbc);

     gbc.gridx =2;
     panel.add(signUpButton, gbc);
     
     gbc.gridy = 4;
     gbc.gridx=0;
     gbc.gridwidth = 2;
     gbc.anchor = GridBagConstraints.CENTER;
     panel.add(statusLabel,gbc);
     
     add(panel);
     
    }
    
    private void addListeners() {
         loginButton.addActionListener(e->authenticate());
         
         resetButton.addActionListener( e->{
             usernameField.setText("");
             passwordField.setText("");
             statusLabel.setText("Enter your username and password");
             statusLabel.setForeground(Color.BLUE);
             usernameField.requestFocus();
             
         });
        signUpButton.addActionListener(e -> {
            new Thread(() -> {
                try {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            RegistrationFram registrationPanel = new RegistrationFram();
                            registrationPanel.setVisible(true);
                            dispose();
                        } catch (Exception ex) {
                            statusLabel.setText("Erreur lors de l'ouverture: " + ex.getMessage());
                            statusLabel.setForeground(Color.RED);
                            loginButton.setEnabled(true);
                        }
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Erreur: " + ex.getMessage());
                        statusLabel.setForeground(Color.RED);
                        loginButton.setEnabled(true);
                    });
                }
            }).start();
        });
         passwordField.addKeyListener(new KeyAdapter() {
             @Override
             public void keyPressed(KeyEvent e){
                 if (e.getKeyCode() == KeyEvent.VK_ENTER){
                     authenticate();
                 }
             }
         });

        usernameField.addKeyListener(new KeyAdapter() {
             @Override
             public void keyPressed(KeyEvent e){
                 if (e.getKeyCode() == KeyEvent.VK_ENTER){
                     passwordField.requestFocus();
                 }
             }
         });
    }

    private void authenticate() {
        
        String username=usernameField.getText().trim();
        String password=new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()){
            statusLabel.setText("Please fill all the fields");
            statusLabel.setForeground(Color.RED);
            return;
        }

        loginButton.setEnabled(false);
        statusLabel.setText("Connexion en cours...");
        statusLabel.setForeground(Color.BLUE);

        new Thread(() -> {
            try {
                LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                        .email(username)
                        .password(password)
                        .build();

                AuthResponseDTO authResponse = authApiService.login(loginRequest);
                tokenManager.setTokens(authResponse);

                SwingUtilities.invokeLater(() -> {
                    try {
                        if (callback != null) {
                            callback.onLoginSuccess(authResponse, tokenManager);
                        } else {

                            System.out.println("  ");
                        }
                        dispose();
                    } catch (Exception ex) {
                        statusLabel.setText("Erreur lors de l'ouverture: " + ex.getMessage());
                        statusLabel.setForeground(Color.RED);
                        loginButton.setEnabled(true);
                    }
                });
                } catch (Exception e) {

                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Erreur: " + e.getMessage());
                    statusLabel.setForeground(Color.RED);
                    loginButton.setEnabled(true);
                });
            }
        }).start();
    }
    public static void  main(String[] args) throws Exception {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Login loginWindow = new Login((authResponse, tokenManager) -> {
                RestaurantAppUI mainWindow = new RestaurantAppUI(tokenManager, authResponse);
                mainWindow.setVisible(true);
            });
            loginWindow.setVisible(true);
        });

//        AuthApiService authApiService = new AuthApiService();
//        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
//                .email("test@testt.com")
//                .password("1234567890")
//                .build();
//
//        System.out.println(authApiService.login(loginRequestDTO));
    }
}