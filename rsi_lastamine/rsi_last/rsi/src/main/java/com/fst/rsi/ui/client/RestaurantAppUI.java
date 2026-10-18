package com.fst.rsi.ui.client;



import com.fst.rsi.dtos.AuthResponseDTO;
import com.fst.rsi.dtos.ClientResponseDTO;
import com.fst.rsi.service.ClientApiService;
import com.fst.rsi.service.TokenManager;
import com.fst.rsi.ui.RegistrationFram;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class RestaurantAppUI extends JFrame {

    private TokenManager tokenManager;
    private AuthResponseDTO authResponse;
    private final ClientApiService clientApiService;

    private JTabbedPane tabbedPane;
    private ClientResponseDTO currentClient;

    // Panels
    private RegistrationFram registrationPanel;
    private ProfilePanel profilePanel;
    private MenuPanel menuPanel;
    private CartPanel cartPanel;
    private OrdersPanel ordersPanel;

    public RestaurantAppUI(TokenManager tokenManager,AuthResponseDTO authResponse) {
        this.authResponse = authResponse;
        this.tokenManager = tokenManager;
        this.clientApiService=new ClientApiService(tokenManager);

        try {
            currentClient = clientApiService.getClientByEmail(authResponse.getEmail());
        } catch (Exception e) {
        }
        // Configure main window
        setTitle("Restaurant Management System - RSI");
        setSize(1300, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeUI();

//        hideAllTabsExceptRegistration();
    }

    private void hideAllTabsExceptRegistration() {
        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, false);
        }
    }

    public void enableAllTabs() {
        if (currentClient.getIdClient() == null) {
            JOptionPane.showMessageDialog(this,
                    "Please register first!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            tabbedPane.setSelectedIndex(0);
            return;
        }

        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, true);
        }
        tabbedPane.setEnabledAt(0, false);
    }

    private void initializeUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        HeaderPanel header = new HeaderPanel();
        mainPanel.add(header, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        tabbedPane.setBackground(Color.WHITE);

        // Initialize panels
        registrationPanel = new RegistrationFram();
        profilePanel = new ProfilePanel(this,tokenManager);
        menuPanel = new MenuPanel(this,tokenManager);
        cartPanel = new CartPanel(this, tokenManager);
        ordersPanel = new OrdersPanel(tokenManager,this);

//        tabbedPane.addTab("  📝 Registration  ", registrationPanel);
        tabbedPane.addTab("  👤 Profile  ", profilePanel);
        tabbedPane.addTab("  🍽️ Menu  ", menuPanel);
        tabbedPane.addTab("  🛒 Cart  ", cartPanel);
        tabbedPane.addTab("  📋 My Orders  ", ordersPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Footer
        FooterPanel footer = new FooterPanel();
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RestaurantAppUI frame = new RestaurantAppUI(null,null);
            frame.setVisible(true);
        });
    }
}