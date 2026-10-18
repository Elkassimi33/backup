//package com.fst.rsi.ui;
//
//import com.fst.rsi.dtos.*;
//import com.fst.rsi.model.CartItem;
//import com.fst.rsi.service.ClientApiService;
//import com.fst.rsi.service.CommandeApiService;
//import com.fst.rsi.service.PlatApiService;
//
//import javax.swing.*;
//import javax.swing.border.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.*;
//import java.math.BigDecimal;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class RestaurantAppUI extends JFrame {
//
//    // --- SERVICES ---
//    private final ClientApiService clientService;
//    private final PlatApiService platService;
//    private final CommandeApiService commandeService;
//
//    // --- UI COMPONENTS ---
//    private JTabbedPane tabbedPane;
//    private Long currentClientId ;
//    private List<PlatDTO> allPlats;
//    private List<CartItem> cartItems = new ArrayList<>();
//
//    // Registration Panel
//    private JTextField regNom, regPrenom, regEmail, regTel, regAdresse, regPref;
//    private JPasswordField regPass;
//    private JButton btnRegister;
//
//    // Profile Panel
//    private JTextField txtSearchId;
//    private JLabel lblProfName, lblProfEmail, lblProfPoints, lblProfTel, lblProfAdresse;
//    private JTextField txtUsePoints;
//    private JButton btnLoadProfile, btnUsePoints;
//
//    // Menu Panel
//    private JPanel platsGridPanel;
//    private JComboBox<String> cmbCategories;
//    private JTextField txtSearch;
//    private JButton btnRefreshMenu, btnSearchMenu;
//    private JLabel lblPlatsCount, lblCartBadge;
//
//    // Cart Panel
//    private JTable cartTable;
//    private DefaultTableModel cartTableModel;
//    private JLabel lblCartTotal;
//    private JTextArea txtSpecialInstructions;
//    private JComboBox<String> cmbOrderType;
//    private JButton btnPlaceOrder, btnClearCart;
//
//    // Orders Panel
//    private JPanel ordersPanel;
//    private JButton btnLoadOrders;
//
//    // Colors
//    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
//    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
//    private static final Color WARNING_COLOR = new Color(230, 126, 34);
//    private static final Color DANGER_COLOR = new Color(231, 76, 60);
//    private static final Color LIGHT_GRAY = new Color(236, 240, 241);
//    private static final Color CART_COLOR = new Color(241, 196, 15);
//
//    public RestaurantAppUI() {
//        // Initialize services
//        this.clientService = new ClientApiService();
//        this.platService = new PlatApiService();
//        this.commandeService = new CommandeApiService();
//
//        // Configure main window
//        setTitle("Restaurant Management System - RSI");
//        setSize(1300, 850);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // Set look and feel
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Initialize UI
//        initializeUI();
//
//        // Cache tous les onglets sauf l'inscription au début
//        hideAllTabsExceptRegistration();
//    }
//
//
////    ########################
////    hadi hya lli katkhbbi les interface lakhrin
//
//
//    private void hideAllTabsExceptRegistration() {
//        // Masque tous les onglets sauf Registration
//        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
//            tabbedPane.setEnabledAt(i, false);
//        }
//    }
//
//
//
////    ########################
////    hadi hya lli katbiyanhom
//
//    private void enableAllTabs() {
//        if (currentClientId == null) {
//            JOptionPane.showMessageDialog(this,
//                    "Please register first!",
//                    "Warning",
//                    JOptionPane.WARNING_MESSAGE);
//            tabbedPane.setSelectedIndex(0); // Retour à l'inscription
//            return;
//        }
//        // Active tous les onglets
//        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
//            tabbedPane.setEnabledAt(i, true);
//        }
//        tabbedPane.setEnabledAt(0, false);
//    }
//
//
//
//
//
//
//    private void initializeUI() {
//        // Create main container
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setBackground(Color.WHITE);
//
//        // Header
//        JPanel header = createHeader();
//        mainPanel.add(header, BorderLayout.NORTH);
//
//        // Tabbed pane
//        tabbedPane = new JTabbedPane();
//        tabbedPane.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
//        tabbedPane.setBackground(Color.WHITE);
//
//        tabbedPane.addTab("  📝 Registration  ", createRegistrationPanel());
//        tabbedPane.addTab("  👤 Profile  ", createProfilePanel());
//        tabbedPane.addTab("  🍽️ Menu  ", createMenuPanel());
//        tabbedPane.addTab("  🛒 Cart  ", createCartPanel());
//        tabbedPane.addTab("  📋 My Orders  ", createOrdersPanel());
//
//        mainPanel.add(tabbedPane, BorderLayout.CENTER);
//
//        // Footer
//        JPanel footer = createFooter();
//        mainPanel.add(footer, BorderLayout.SOUTH);
//
//        add(mainPanel);
//    }
//
//    // ==========================================
//    // HEADER
//    // ==========================================
//    private JPanel createHeader() {
//        JPanel header = new JPanel(new BorderLayout());
//        header.setBackground(PRIMARY_COLOR);
//        header.setBorder(new EmptyBorder(15, 20, 15, 20));
////        JLabel title = new JLabel("🗂️ Restaurant Management System");
//        JLabel title = new JLabel("🍔 Restaurant Management System");
////        JLabel title = new JLabel("  🍽️ Restaurant Management System");
//        title.setFont(new Font("Segoe UI icontest", Font.BOLD, 24));
//        title.setForeground(Color.WHITE);
//
//        JLabel subtitle = new JLabel("Powered by RSI - Faculty of Sciences and Technology");
//        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//        subtitle.setForeground(new Color(255, 255, 255, 180));
//
//        JPanel textPanel = new JPanel(new GridLayout(2, 1));
//        textPanel.setOpaque(false);
//        textPanel.add(title);
//        textPanel.add(subtitle);
//
//
//
//
//
//
//
//        // Cart badge
////        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
////        rightPanel.setOpaque(false);
//
////        lblCartBadge = new JLabel("🛒 0");
////        lblCartBadge.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
////        lblCartBadge.setForeground(Color.WHITE);
////        lblCartBadge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
////        lblCartBadge.addMouseListener(new MouseAdapter() {
////            @Override
////            public void mouseClicked(MouseEvent e) {
////                tabbedPane.setSelectedIndex(3); // Switch to cart tab
////            }
////        });
////
////        rightPanel.add(lblCartBadge);
//
//
//        header.add(textPanel, BorderLayout.WEST);
////        header.add(rightPanel, BorderLayout.EAST);
//
//        return header;
//    }
//
//
//
//    private void handleLogout() {
//        int confirm = JOptionPane.showConfirmDialog(this,
//                "Are you sure you want to logout?",
//                "Confirm Logout",
//                JOptionPane.YES_NO_OPTION);
//
//        if (confirm == JOptionPane.YES_OPTION) {
//            // Reset current client
//            currentClientId = null;
//
//            // Clear cart
//            cartItems.clear();
//            updateCartUI();
//
//            // Reset profile display
//            lblProfName.setText("--- ---");
//            lblProfEmail.setText("email@example.com");
//            lblProfTel.setText("Phone: ---");
//            lblProfAdresse.setText("Address: ---");
//            lblProfPoints.setText("0 pts");
//
//            // Clear orders panel
//            ordersPanel.removeAll();
//            ordersPanel.revalidate();
//            ordersPanel.repaint();
//
//            // Hide all tabs except registration
//            hideAllTabsExceptRegistration();
//
//            // Switch to registration tab
//            tabbedPane.setSelectedIndex(0);
//
//            JOptionPane.showMessageDialog(this,
//                    "Logged out successfully!",
//                    "Info",
//                    JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//
//
//
//    // ==========================================
//    // FOOTER
//    // ==========================================
//    private JPanel createFooter() {
//        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        footer.setBackground(LIGHT_GRAY);
//        footer.setBorder(new EmptyBorder(10, 10, 10, 10));
//
//        JLabel footerLabel = new JLabel("© 2024 FST Restaurant System | Version 2.0");
//        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
//        footerLabel.setForeground(Color.GRAY);
//
//        footer.add(footerLabel);
//        return footer;
//    }
//
//    // ==========================================
//    // 1. REGISTRATION PANEL
//    // ==========================================
//    private JPanel createRegistrationPanel() {
//        JPanel mainPanel = new JPanel(new GridBagLayout());
//        mainPanel.setBackground(Color.WHITE);
//
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        formPanel.setBackground(Color.WHITE);
//        formPanel.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(LIGHT_GRAY, 1),
//                new EmptyBorder(30, 40, 30, 40)
//        ));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        // Title
//        JLabel title = new JLabel("New Client Registration");
//        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
//        title.setForeground(PRIMARY_COLOR);
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 2;
//        gbc.insets = new Insets(0, 0, 30, 0);
//        formPanel.add(title, gbc);
//
//        gbc.gridwidth = 1;
//        gbc.insets = new Insets(8, 10, 8, 10);
//
//        // Form fields
//        regNom = addStyledFormRow(formPanel, "Last Name *", 1, gbc);
//        regPrenom = addStyledFormRow(formPanel, "First Name *", 2, gbc);
//        regEmail = addStyledFormRow(formPanel, "Email *", 3, gbc);
//        regTel = addStyledFormRow(formPanel, "Phone Number *", 4, gbc);
//
//        // Password field
//        gbc.gridx = 0;
//        gbc.gridy = 5;
//        JLabel lblPass = new JLabel("Password *");
//        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
//        formPanel.add(lblPass, gbc);
//
//        regPass = new JPasswordField(20);
//        regPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        regPass.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(Color.LIGHT_GRAY, 1),
//                new EmptyBorder(8, 10, 8, 10)
//        ));
//        gbc.gridx = 1;
//        formPanel.add(regPass, gbc);
//
//        regAdresse = addStyledFormRow(formPanel, "Address", 6, gbc);
//        regPref = addStyledFormRow(formPanel, "Food Preferences", 7, gbc);
//
//        // Submit button
//        btnRegister = new JButton("Create Account");
//        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 15));
//        btnRegister.setBackground(SUCCESS_COLOR);
//        btnRegister.setForeground(Color.WHITE);
//        btnRegister.setFocusPainted(false);
//        btnRegister.setBorder(new EmptyBorder(12, 30, 12, 30));
//        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnRegister.setContentAreaFilled(false); // IMPORTANT !
//        btnRegister.setOpaque(true); // IMPORTANT !
//        btnRegister.addActionListener(e -> handleRegistration());
//
//        gbc.gridx = 0;
//        gbc.gridy = 8;
//        gbc.gridwidth = 2;
//        gbc.insets = new Insets(25, 10, 10, 10);
//        formPanel.add(btnRegister, gbc);
//
//        // Required fields note
//        JLabel note = new JLabel("* Required fields");
//        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
//        note.setForeground(Color.GRAY);
//        gbc.gridy = 9;
//        gbc.insets = new Insets(10, 10, 0, 10);
//        formPanel.add(note, gbc);
//
//        mainPanel.add(formPanel);
//        return mainPanel;
//    }
//
//    private JTextField addStyledFormRow(JPanel panel, String label, int row, GridBagConstraints gbc) {
//        gbc.gridx = 0;
//        gbc.gridy = row;
//        JLabel lbl = new JLabel(label);
//        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
//        panel.add(lbl, gbc);
//
//        JTextField field = new JTextField(20);
//        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        field.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(Color.LIGHT_GRAY, 1),
//                new EmptyBorder(8, 10, 8, 10)
//        ));
//        gbc.gridx = 1;
//        panel.add(field, gbc);
//
//        return field;
//    }
//
//    // ==========================================
//    // 2. PROFILE PANEL
//    // ==========================================
//    private JPanel createProfilePanel() {
//        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
//        mainPanel.setBackground(Color.WHITE);
//        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
//
//        // Search bar
//        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
//        searchPanel.setBackground(Color.WHITE);
//
//
//    // hadchi bach nhyyd dik fonction de recherche client mn inteface client
//
//
////        JLabel lblSearch = new JLabel("Client ID:");
////        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
//
////        txtSearchId = new JTextField(12);
////        txtSearchId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
////        txtSearchId.setBorder(BorderFactory.createCompoundBorder(
////                new LineBorder(Color.LIGHT_GRAY, 1),
////                new EmptyBorder(8, 10, 8, 10)
////        ));
//
////        btnLoadProfile = new JButton("Load Profile");
////        btnLoadProfile.setFont(new Font("Segoe UI", Font.BOLD, 13));
////        btnLoadProfile.setBackground(PRIMARY_COLOR);
////        btnLoadProfile.setForeground(Color.WHITE);
////        btnLoadProfile.setFocusPainted(false);
////        btnLoadProfile.setBorder(new EmptyBorder(10, 20, 10, 20));
////        btnLoadProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
////        btnLoadProfile.addActionListener(e -> handleLoadProfile());
//
////        searchPanel.add(lblSearch);
////        searchPanel.add(txtSearchId);
////        searchPanel.add(btnLoadProfile);
//
//
//        //##########################
//        //#       DECONECTION     ##
//        // Logout button
//        JButton btnLogout = new JButton("Logout");
//        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
//        btnLogout.setBackground(DANGER_COLOR);
//        btnLogout.setForeground(Color.WHITE);
//        btnLogout.setFocusPainted(false);
//        btnLogout.setBorder(new EmptyBorder(5, 15, 5, 15));
//        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//        btnLogout.setContentAreaFilled(false); // IMPORTANT !
//        btnLogout.setOpaque(true); // IMPORTANT !
//
//        btnLogout.addActionListener(e -> handleLogout());
//
//
//        // Panel pour le bouton de logout (aligné à droite)
//        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        logoutPanel.setBackground(Color.WHITE);
//        logoutPanel.add(btnLogout);
//
//        searchPanel.add(logoutPanel, BorderLayout.EAST);
//
//
//
//        // Profile card
//        JPanel profileCard = new JPanel(new BorderLayout(20, 20));
//        profileCard.setBackground(Color.WHITE);
//        profileCard.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(LIGHT_GRAY, 2),
//                new EmptyBorder(30, 30, 30, 30)
//        ));
//
//        // Client info
//        JPanel infoPanel = new JPanel(new GridBagLayout());
//        infoPanel.setBackground(Color.WHITE);
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.insets = new Insets(8, 0, 8, 0);
//
//        lblProfName = new JLabel("--- ---");
//        lblProfName.setFont(new Font("Segoe UI", Font.BOLD, 28));
//        lblProfName.setForeground(PRIMARY_COLOR);
//
//        lblProfEmail = new JLabel("email@example.com");
//        lblProfEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        lblProfEmail.setForeground(Color.GRAY);
//
//        lblProfTel = new JLabel("Phone: ---");
//        lblProfTel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//
//        lblProfAdresse = new JLabel("Address: ---");
//        lblProfAdresse.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        infoPanel.add(lblProfName, gbc);
//        gbc.gridy = 1;
//        infoPanel.add(lblProfEmail, gbc);
//        gbc.gridy = 2;
//        gbc.insets = new Insets(15, 0, 5, 0);
//        infoPanel.add(lblProfTel, gbc);
//        gbc.gridy = 3;
//        gbc.insets = new Insets(5, 0, 8, 0);
//        infoPanel.add(lblProfAdresse, gbc);
//
//        // Loyalty points panel
//        JPanel loyaltyPanel = new JPanel(new BorderLayout(10, 10));
//        loyaltyPanel.setBackground(LIGHT_GRAY);
//        loyaltyPanel.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(WARNING_COLOR, 2),
//                new EmptyBorder(20, 20, 20, 20)
//        ));
//
//        JLabel lblLoyaltyTitle = new JLabel("💎 Loyalty Points");
//        lblLoyaltyTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
//
//        lblProfPoints = new JLabel("0 pts");
//        lblProfPoints.setFont(new Font("Segoe UI", Font.BOLD, 42));
//        lblProfPoints.setForeground(WARNING_COLOR);
//
//        JPanel usePointsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
//        usePointsPanel.setOpaque(false);
//
//        JLabel lblUse = new JLabel("Use points:");
//        lblUse.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//
//        txtUsePoints = new JTextField(8);
//        txtUsePoints.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        txtUsePoints.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(Color.LIGHT_GRAY, 1),
//                new EmptyBorder(6, 8, 6, 8)
//        ));
//
//        btnUsePoints = new JButton("Redeem");
//        btnUsePoints.setFont(new Font("Segoe UI", Font.BOLD, 12));
//        btnUsePoints.setBackground(WARNING_COLOR);
//        btnUsePoints.setForeground(Color.WHITE);
//        btnUsePoints.setFocusPainted(false);
//        btnUsePoints.setBorder(new EmptyBorder(8, 15, 8, 15));
//        btnUsePoints.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnUsePoints.setContentAreaFilled(false); // IMPORTANT !
//        btnUsePoints.setOpaque(true); // IMPORTANT !
//        btnUsePoints.addActionListener(e -> handleUsePoints());
//
//        usePointsPanel.add(lblUse);
//        usePointsPanel.add(txtUsePoints);
//        usePointsPanel.add(btnUsePoints);
//
//        JPanel loyaltyContent = new JPanel(new BorderLayout(10, 10));
//        loyaltyContent.setOpaque(false);
//        loyaltyContent.add(lblLoyaltyTitle, BorderLayout.NORTH);
//        loyaltyContent.add(lblProfPoints, BorderLayout.CENTER);
//        loyaltyContent.add(usePointsPanel, BorderLayout.SOUTH);
//
//        loyaltyPanel.add(loyaltyContent);
//
//        profileCard.add(infoPanel, BorderLayout.WEST);
//        profileCard.add(loyaltyPanel, BorderLayout.EAST);
//        mainPanel.add(searchPanel, BorderLayout.NORTH);
//        mainPanel.add(profileCard, BorderLayout.CENTER);
//
//        return mainPanel;
//    }
//
//    // ==========================================
//    // 3. MENU PANEL
//    // ==========================================
//    private JPanel createMenuPanel() {
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBackground(Color.WHITE);
//        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
//
//        // Toolbar
//        JPanel toolbar = new JPanel(new BorderLayout(10, 10));
//        toolbar.setBackground(Color.WHITE);
//        toolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
//
//        // Left side - filters
//        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
//        filtersPanel.setBackground(Color.WHITE);
//
//        JLabel lblFilter = new JLabel("Category:");
//        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));
//
//        cmbCategories = new JComboBox<>(new String[]{"All Categories"});
//        cmbCategories.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        cmbCategories.setPreferredSize(new Dimension(180, 35));
//        cmbCategories.addActionListener(e -> filterPlatsByCategory());
//
//        JLabel lblSearch = new JLabel("Search:");
//        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
//
//        txtSearch = new JTextField(15);
//        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        txtSearch.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(Color.LIGHT_GRAY, 1),
//                new EmptyBorder(6, 10, 6, 10)
//        ));
//
//        btnSearchMenu = new JButton("🔍 Search");
//        btnSearchMenu.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
//        btnSearchMenu.setBackground(PRIMARY_COLOR);
//        btnSearchMenu.setForeground(Color.WHITE);
//        btnSearchMenu.setFocusPainted(false);
//        btnSearchMenu.setBorder(new EmptyBorder(8, 15, 8, 15));
//        btnSearchMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnSearchMenu.setContentAreaFilled(false); // IMPORTANT !
//        btnSearchMenu.setOpaque(true); // IMPORTANT !
//        btnSearchMenu.addActionListener(e -> handleSearchMenu());
//
//        btnRefreshMenu = new JButton("↻ Refresh");
//        btnRefreshMenu.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
//        btnRefreshMenu.setBackground(SUCCESS_COLOR);
//        btnRefreshMenu.setForeground(Color.WHITE);
//        btnRefreshMenu.setFocusPainted(false);
//        btnRefreshMenu.setBorder(new EmptyBorder(8, 15, 8, 15));
//        btnRefreshMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnRefreshMenu.setContentAreaFilled(false); // IMPORTANT !
//        btnRefreshMenu.setOpaque(true); // IMPORTANT !
//        btnRefreshMenu.addActionListener(e -> handleLoadMenu());
//
//
//        // Right side - count + cart badge
//        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
//        rightPanel.setBackground(Color.WHITE);
//
//
//
//        filtersPanel.add(lblFilter);
//        filtersPanel.add(cmbCategories);
//        filtersPanel.add(Box.createHorizontalStrut(10));
//        filtersPanel.add(lblSearch);
//        filtersPanel.add(txtSearch);
//        filtersPanel.add(btnSearchMenu);
//        filtersPanel.add(btnRefreshMenu);
//
//        // Right side - count
//        lblPlatsCount = new JLabel("0 dishes");
//        lblPlatsCount.setFont(new Font("Segoe UI  Emoji", Font.BOLD, 13));
//        lblPlatsCount.setForeground(Color.GRAY);
//        rightPanel.add(lblPlatsCount);
//
//        rightPanel.add(Box.createHorizontalStrut(15));
//
//
//        // Count selected dishes
//        lblCartBadge = new JLabel("🛒 0");
//        lblCartBadge.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
//        lblCartBadge.setForeground(PRIMARY_COLOR);
//        lblCartBadge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        lblCartBadge.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (currentClientId != null) {
//                    tabbedPane.setSelectedIndex(3); // Switch to cart tab
//                } else {
//                    JOptionPane.showMessageDialog(RestaurantAppUI.this,
//                            "Please register first!",
//                            "Warning",
//                            JOptionPane.WARNING_MESSAGE);
//                }
//            }
//        });
//
//        rightPanel.add(lblCartBadge);
//
//
//
//
//        toolbar.add(filtersPanel, BorderLayout.WEST);
//        toolbar.add(rightPanel, BorderLayout.EAST);
//
//
//
//
//
//
//        // Grid panel for dishes
//        platsGridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
//        platsGridPanel.setBackground(Color.WHITE);
//
//        JScrollPane scrollPane = new JScrollPane(platsGridPanel);
//        scrollPane.setBorder(null);
//        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
//
//        mainPanel.add(toolbar, BorderLayout.NORTH);
//        mainPanel.add(scrollPane, BorderLayout.CENTER);
//
//        // Auto-load menu on startup
//        SwingUtilities.invokeLater(this::handleLoadMenu);
//
//        return mainPanel;
//    }
//
//    // ==========================================
//    // 4. CART PANEL
//    // ==========================================
//    private JPanel createCartPanel() {
//        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
//        mainPanel.setBackground(Color.WHITE);
//        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
//
//        // Title
//        JLabel title = new JLabel("🛒 Shopping Cart");
//        title.setFont(new Font("Segoe UI  Emoji", Font.BOLD, 26));
//        title.setForeground(PRIMARY_COLOR);
//
//        // Cart table
//        String[] columns = {"Dish", "Price", "Quantity", "Subtotal", "Actions"};
//        cartTableModel = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return column == 4; // Only actions column
//            }
//        };
//
//        cartTable = new JTable(cartTableModel);
//        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        cartTable.setRowHeight(40);
//        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
//        cartTable.getTableHeader().setBackground(LIGHT_GRAY);
//
//        // Add button renderer for actions
//        cartTable.getColumn("Actions").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
//            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
//
//
//            JButton btnRemove = new JButton("❌");
//            btnRemove.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
//            btnRemove.setFocusPainted(false);
//            panel.add(btnRemove);
//            return panel;
//        });
//
//        cartTable.getColumn("Actions").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
//            @Override
//            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
//                JButton btnRemove = new JButton("❌");
//                btnRemove.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
//                btnRemove.setFocusPainted(false);
//                btnRemove.addActionListener(e -> {
//                    removeFromCart(row);
//                    fireEditingStopped();
//                });
//                panel.add(btnRemove);
//                return panel;
//            }
//        });
//
//        JScrollPane tableScroll = new JScrollPane(cartTable);
//        tableScroll.setBorder(new LineBorder(LIGHT_GRAY, 1));
//
//        // Order details panel
//        JPanel detailsPanel = new JPanel(new GridBagLayout());
//        detailsPanel.setBackground(Color.WHITE);
//        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(LIGHT_GRAY, 1),
//                new EmptyBorder(20, 20, 20, 20)
//        ));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(8, 8, 8, 8);
//
//        // Order type
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        JLabel lblOrderType = new JLabel("Order Type:");
//        lblOrderType.setFont(new Font("Segoe UI", Font.BOLD, 13));
//        detailsPanel.add(lblOrderType, gbc);
//
//        gbc.gridx = 1;
//        cmbOrderType = new JComboBox<>(new String[]{"LIVRAISON", "A_EMPORTER"});
//        cmbOrderType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        detailsPanel.add(cmbOrderType, gbc);
//
//        // Special instructions
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        JLabel lblInstructions = new JLabel("Special Instructions:");
//        lblInstructions.setFont(new Font("Segoe UI", Font.BOLD, 13));
//        detailsPanel.add(lblInstructions, gbc);
//
//        gbc.gridx = 1;
//        gbc.gridwidth = 2;
//        txtSpecialInstructions = new JTextArea(3, 20);
//        txtSpecialInstructions.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        txtSpecialInstructions.setLineWrap(true);
//        txtSpecialInstructions.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(Color.LIGHT_GRAY, 1),
//                new EmptyBorder(5, 5, 5, 5)
//        ));
//        JScrollPane instructionsScroll = new JScrollPane(txtSpecialInstructions);
//        detailsPanel.add(instructionsScroll, gbc);
//
//        // Total
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.gridwidth = 1;
//        JLabel lblTotalLabel = new JLabel("Total:");
//        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        detailsPanel.add(lblTotalLabel, gbc);
//
//        gbc.gridx = 1;
//        lblCartTotal = new JLabel("0.00 DH");
//        lblCartTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
//        lblCartTotal.setForeground(SUCCESS_COLOR);
//        detailsPanel.add(lblCartTotal, gbc);
//
//        // Buttons
//        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
//        buttonsPanel.setOpaque(false);
//
//        btnClearCart = new JButton("Clear Cart");
//        btnClearCart.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        btnClearCart.setBackground(DANGER_COLOR);
//        btnClearCart.setForeground(Color.WHITE);
//        btnClearCart.setFocusPainted(false);
//        btnClearCart.setBorder(new EmptyBorder(10, 20, 10, 20));
//        btnClearCart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnClearCart.setContentAreaFilled(false); // IMPORTANT !
//        btnClearCart.setOpaque(true); // IMPORTANT !
//        btnClearCart.addActionListener(e -> handleClearCart());
//
//        btnPlaceOrder = new JButton("Place Order");
//        btnPlaceOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        btnPlaceOrder.setBackground(SUCCESS_COLOR);
//        btnPlaceOrder.setForeground(Color.WHITE);
//        btnPlaceOrder.setFocusPainted(false);
//        btnPlaceOrder.setBorder(new EmptyBorder(10, 30, 10, 30));
//        btnPlaceOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnPlaceOrder.setContentAreaFilled(false); // IMPORTANT !
//        btnPlaceOrder.setOpaque(true); // IMPORTANT !
//        btnPlaceOrder.addActionListener(e -> handlePlaceOrder());
//
//        buttonsPanel.add(btnClearCart);
//        buttonsPanel.add(btnPlaceOrder);
//
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.gridwidth = 2;
//        gbc.insets = new Insets(20, 8, 8, 8);
//        detailsPanel.add(buttonsPanel, gbc);
//
//        // Layout
//        mainPanel.add(title, BorderLayout.NORTH);
//        mainPanel.add(tableScroll, BorderLayout.CENTER);
//        mainPanel.add(detailsPanel, BorderLayout.SOUTH);
//
//        return mainPanel;
//    }
//
//    // ==========================================
//    // 5. ORDERS PANEL
//    // ==========================================
//    private JPanel createOrdersPanel() {
//        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
//        mainPanel.setBackground(Color.WHITE);
//        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
//
//        // Header
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        headerPanel.setBackground(Color.WHITE);
//
//        JLabel title = new JLabel("📋 My Orders");
//        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
//        title.setForeground(PRIMARY_COLOR);
//
//        btnLoadOrders = new JButton("↻ Refresh Orders");
//        btnLoadOrders.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
//        btnLoadOrders.setBackground(PRIMARY_COLOR);
//        btnLoadOrders.setForeground(Color.WHITE);
//        btnLoadOrders.setFocusPainted(false);
//        btnLoadOrders.setBorder(new EmptyBorder(10, 20, 10, 20));
//        btnLoadOrders.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnLoadOrders.setContentAreaFilled(false); // IMPORTANT !
//        btnLoadOrders.setOpaque(true); // IMPORTANT !
//        btnLoadOrders.addActionListener(e -> handleLoadOrders());
//
//        headerPanel.add(title, BorderLayout.WEST);
//        headerPanel.add(btnLoadOrders, BorderLayout.EAST);
//
//        // Orders container
//        ordersPanel = new JPanel();
//        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
//        ordersPanel.setBackground(Color.WHITE);
//
//        JScrollPane scrollPane = new JScrollPane(ordersPanel);
//        scrollPane.setBorder(null);
//        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
//
//        mainPanel.add(headerPanel, BorderLayout.NORTH);
//        mainPanel.add(scrollPane, BorderLayout.CENTER);
//
//        return mainPanel;
//    }
//
//    // ==========================================
//    // HANDLER METHODS
//    // ==========================================
//
//    /**
//     * Handle client registration
//     */
//    private void handleRegistration() {
//        try {
//            // Validate required fields
//            if (regNom.getText().trim().isEmpty() || regPrenom.getText().trim().isEmpty() ||
//                    regEmail.getText().trim().isEmpty() || regTel.getText().trim().isEmpty() ||
//                    new String(regPass.getPassword()).trim().isEmpty()) {
//                JOptionPane.showMessageDialog(this,
//                        "Please fill all required fields (*)",
//                        "Validation Error",
//                        JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            // Create DTO
//            ClientRequestDTO dto = ClientRequestDTO.builder()
//                    .nom(regNom.getText().trim())
//                    .prenom(regPrenom.getText().trim())
//                    .email(regEmail.getText().trim())
//                    .telephone(regTel.getText().trim())
//                    .password(new String(regPass.getPassword()))
//                    .adresse(regAdresse.getText().trim())
//                    .preferences(regPref.getText().trim())
//                    .build();
//
//            // Call API
//            ClientResponseDTO response = clientService.createClient(dto);
//
//            // Success
//            JOptionPane.showMessageDialog(this,
//                    "Registration successful!\nYour Client ID: " + response.getIdClient(),
//                    "Success",
//                    JOptionPane.INFORMATION_MESSAGE);
//
//            // Clear form
//            regNom.setText("");
//            regPrenom.setText("");
//            regEmail.setText("");
//            regTel.setText("");
//            regPass.setText("");
//            regAdresse.setText("");
//            regPref.setText("");
//
//            // Auto-load profile
//            currentClientId = response.getIdClient();
//            enableAllTabs();
////            txtSearchId.setText(String.valueOf(currentClientId));
//            handleLoadProfile();
//            tabbedPane.setSelectedIndex(1); // Switch to profile tab
//
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this,
//                    "Registration failed: " + ex.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    /**
//     * Load client profile
//     */
//    private void handleLoadProfile() {
//        try {
////            String idText = txtSearchId.getText().trim();
////            if (idText.isEmpty()) {
////                JOptionPane.showMessageDialog(this,
////                        "Please enter a Client ID",
////                        "Validation Error",
////                        JOptionPane.WARNING_MESSAGE);
////                return;
////            }
//
//            if (currentClientId == null) {
//                JOptionPane.showMessageDialog(this,
//                        "Please register first!",
//                        "Warning",
//                        JOptionPane.WARNING_MESSAGE);
//                tabbedPane.setSelectedIndex(0); // Retour à l'inscription
//                return;
//            }
//
////            Long clientId = currentClientId; //Long.parseLong(idText);
//            ClientResponseDTO client = clientService.getClientById(currentClientId);
//
//            // Update UI
////            currentClientId = clientId;
//            lblProfName.setText(client.getPrenom() + " " + client.getNom());
//            lblProfEmail.setText(client.getEmail());
//            lblProfTel.setText("Phone: " + client.getTelephone());
//            lblProfAdresse.setText("Address: " + (client.getAdresse() != null ? client.getAdresse() : "N/A"));
//            lblProfPoints.setText(client.getPointsFidelite() + " pts");
//
//            JOptionPane.showMessageDialog(this,
//                    "Profile loaded successfully!",
//                    "Success",
//                    JOptionPane.INFORMATION_MESSAGE);
//
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(this,
//                    "Invalid Client ID format",
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this,
//                    "Failed to load profile: " + ex.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    /**
//     * Use loyalty points
//     */
//    private void handleUsePoints() {
//        try {
//            if (currentClientId == null) {
//                JOptionPane.showMessageDialog(this,
//                        "Please register first!",
//                        "Warning",
//                        JOptionPane.WARNING_MESSAGE);
//                tabbedPane.setSelectedIndex(0); // Retour à l'inscription
//                return;
//            }
//
//            String pointsText = txtUsePoints.getText().trim();
//            if (pointsText.isEmpty()) {
//                JOptionPane.showMessageDialog(this,
//                        "Please enter points amount",
//                        "Validation Error",
//                        JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            Integer points = Integer.parseInt(pointsText);
//            clientService.utiliserPoints(currentClientId, points);
//
//            // Refresh profile
//            handleLoadProfile();
//
//            JOptionPane.showMessageDialog(this,
//                    "Points redeemed successfully!",
//                    "Success",
//                    JOptionPane.INFORMATION_MESSAGE);
//
//            txtUsePoints.setText("");
//
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(this,
//                    "Invalid points format",
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this,
//                    "Failed to use points: " + ex.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    /**
//     * Load menu dishes
//     */
//    private void handleLoadMenu() {
//        try {
//
//
//            allPlats = platService.getPlatsDisponibles();
//            displayPlats(allPlats);
//
//            // Load categories
//            List<String> categories = allPlats.stream()
//                    .map(PlatDTO::getCategorie)
//                    .distinct()
//                    .sorted()
//                    .toList();
//
//            cmbCategories.removeAllItems();
//            cmbCategories.addItem("All Categories");
//            categories.forEach(cmbCategories::addItem);
//
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this,
//                    "Failed to load menu: " + ex.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    /**
//     * Display dishes in grid
//     */
//    private void displayPlats(List<PlatDTO> plats) {
//
//
//        platsGridPanel.removeAll();
//
//        for (PlatDTO plat : plats) {
//            if (plat.getDisponible()) {
//                JPanel platCard = createPlatCard(plat);
//                platsGridPanel.add(platCard);
//            }
//        }
//
//        lblPlatsCount.setText(plats.size() + " dish(es)");
//        platsGridPanel.revalidate();
//        platsGridPanel.repaint();
//    }
//
//    /**
//     * Create a dish card
//     */
//    private JPanel createPlatCard(PlatDTO plat) {
//        JPanel card = new JPanel(new BorderLayout(10, 10));
//        card.setPreferredSize(new Dimension(280, 320));
//        card.setBackground(Color.WHITE);
//        card.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(LIGHT_GRAY, 1),
//                new EmptyBorder(15, 15, 15, 15)
//        ));
//
//        // Image placeholder
//        JPanel imagePanel = new JPanel();
//        imagePanel.setPreferredSize(new Dimension(250, 150));
//        imagePanel.setBackground(LIGHT_GRAY);
//        JLabel imgLabel = new JLabel("🍽️", SwingConstants.CENTER);
//        imgLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 60));
//        imagePanel.add(imgLabel);
//
//        // Info panel
//        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
//        infoPanel.setBackground(Color.WHITE);
//
//        JLabel lblName = new JLabel(plat.getNom());
//        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        lblName.setForeground(PRIMARY_COLOR);
//
//        JLabel lblCategory = new JLabel("Category: " + plat.getCategorie());
//        lblCategory.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//        lblCategory.setForeground(Color.GRAY);
//
//        JLabel lblPrice = new JLabel(plat.getPrix() + " DH");
//        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblPrice.setForeground(SUCCESS_COLOR);
//
//        JButton btnAdd = new JButton("➕ Add to Cart");
//        btnAdd.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
//        btnAdd.setBackground(PRIMARY_COLOR);
//        btnAdd.setForeground(Color.WHITE);
//        btnAdd.setFocusPainted(false);
//        btnAdd.setBorder(new EmptyBorder(8, 15, 8, 15));
//        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnAdd.setContentAreaFilled(false); // IMPORTANT !
//        btnAdd.setOpaque(true); // IMPORTANT !
//        btnAdd.addActionListener(e -> addToCart(plat));
//
//        infoPanel.add(lblName);
//        infoPanel.add(lblCategory);
//        infoPanel.add(lblPrice);
//        infoPanel.add(btnAdd);
//
//        card.add(imagePanel, BorderLayout.NORTH);
//        card.add(infoPanel, BorderLayout.CENTER);
//
//        return card;
//    }
//
//    /**
//     * Filter dishes by category
//     */
//    private void filterPlatsByCategory() {
//        if (allPlats == null) return;
//
//        String selectedCategory = (String) cmbCategories.getSelectedItem();
//        List<PlatDTO> filtered;
//
//        if ("All Categories".equals(selectedCategory)) {
//            filtered = allPlats;
//        } else {
//            filtered = allPlats.stream()
//                    .filter(p -> p.getCategorie().equals(selectedCategory))
//                    .collect(Collectors.toList());
//        }
//
//        displayPlats(filtered);
//    }
//
//    /**
//     * Search dishes by name
//     */
//    private void handleSearchMenu() {
//        if (allPlats == null) return;
//
//        String searchText = txtSearch.getText().trim().toLowerCase();
//
//        if (searchText.isEmpty()) {
//            displayPlats(allPlats);
//            return;
//        }
//
//        List<PlatDTO> filtered = allPlats.stream()
//                .filter(p -> p.getNom().toLowerCase().contains(searchText) ||
//                        p.getCategorie().toLowerCase().contains(searchText))
//                .collect(Collectors.toList());
//
//        displayPlats(filtered);
//    }
//
//    /**
//     * Add dish to cart
//     */
//    private void addToCart(PlatDTO plat) {
//        // Check if item already exists in cart
//        CartItem existingItem = cartItems.stream()
//                .filter(item -> item.getPlat().getIdPlat().equals(plat.getIdPlat()))
//                .findFirst()
//                .orElse(null);
//
//        if (existingItem != null) {
//            existingItem.incrementQuantity();
//        } else {
//            cartItems.add(new CartItem(plat));
//        }
//
//        updateCartUI();
//
//        // Show feedback
//        JOptionPane.showMessageDialog(this,
//                plat.getNom() + " added to cart!",
//                "Success",
//                JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    /**
//     * Remove item from cart
//     */
//    private void removeFromCart(int index) {
//        if (index >= 0 && index < cartItems.size()) {
//            CartItem item = cartItems.get(index);
//            cartItems.remove(index);
//            updateCartUI();
//
//            JOptionPane.showMessageDialog(this,
//                    item.getPlat().getNom() + " removed from cart",
//                    "Info",
//                    JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    /**
//     * Update cart UI
//     */
//    private void updateCartUI() {
//        // Clear table
//        cartTableModel.setRowCount(0);
//
//        // Add items
//        BigDecimal total = BigDecimal.ZERO;
//        for (CartItem item : cartItems) {
//            Object[] row = {
//                    item.getPlat().getNom(),
//                    item.getPlat().getPrix() + " DH",
//                    item.getQuantity(),
//                    item.getSousTotal() + " DH",
//                    "Remove"
//            };
//            cartTableModel.addRow(row);
//            total = total.add(item.getSousTotal());
//        }
//
//        // Update total
//        lblCartTotal.setText(total + " DH");
//
//        // Update badge
//        int totalItems = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
//        lblCartBadge.setText("🛒 " + totalItems);
//    }
//
//    /**
//     * Clear cart
//     */
//    private void handleClearCart() {
//        int confirm = JOptionPane.showConfirmDialog(this,
//                "Are you sure you want to clear the cart?",
//                "Confirm",
//                JOptionPane.YES_NO_OPTION);
//
//        if (confirm == JOptionPane.YES_OPTION) {
//            cartItems.clear();
//            updateCartUI();
//
//            JOptionPane.showMessageDialog(this,
//                    "Cart cleared",
//                    "Info",
//                    JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    /**
//     * Place order
//     */
//    private void handlePlaceOrder() {
//        try {
//            // Validate client
//            if (currentClientId == null) {
//                JOptionPane.showMessageDialog(this,
//                        "Please load your profile first (Profile tab)",
//                        "Warning",
//                        JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            // Validate cart
//            if (cartItems.isEmpty()) {
//                JOptionPane.showMessageDialog(this,
//                        "Your cart is empty",
//                        "Warning",
//                        JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            // Build order lines
//            List<LigneCommandeRequestDTO> lignes = cartItems.stream()
//                    .map(item -> LigneCommandeRequestDTO.builder()
//                            .idPlat(item.getPlat().getIdPlat())
//                            .quantite(item.getQuantity())
//                            .options(item.getOptions())
//                            .build())
//                    .collect(Collectors.toList());
//
//            // Build order request
//            CommandeRequestDTO request = CommandeRequestDTO.builder()
//                    .idClient(currentClientId)
//                    .typeCommande(com.fst.rsi.model.TypeCommande.valueOf((String) cmbOrderType.getSelectedItem()))
//                    .lignesCommande(lignes)
//                    .instructionsSpeciales(txtSpecialInstructions.getText().trim())
//                    .build();
//
//            // Create order
//            CommandeResponseDTO response = commandeService.createCommande(request);
//
//            // Success
//            JOptionPane.showMessageDialog(this,
//                    "Order placed successfully!\nOrder ID: " + response.getIdCommande() +
//                            "\nTotal: " + response.getMontantTotal() + " DH",
//                    "Success",
//                    JOptionPane.INFORMATION_MESSAGE);
//
//            // Clear cart
//            cartItems.clear();
//            updateCartUI();
//            txtSpecialInstructions.setText("");
//
//            // Refresh orders
//            handleLoadOrders();
//            tabbedPane.setSelectedIndex(4); // Switch to orders tab
//
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this,
//                    "Failed to place order: " + ex.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    /**
//     * Load client orders
//     */
//    private void handleLoadOrders() {
//        try {
//            if (currentClientId == null) {
//                JOptionPane.showMessageDialog(this,
//                        "Please load your profile first",
//                        "Warning",
//                        JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            List<CommandeResponseDTO> orders = commandeService.getCommandesByClient(currentClientId);
//
//            // Clear panel
//            ordersPanel.removeAll();
//
//            if (orders.isEmpty()) {
//                JLabel noOrders = new JLabel("No orders found");
//                noOrders.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//                noOrders.setForeground(Color.GRAY);
//                ordersPanel.add(noOrders);
//            } else {
//                // Display orders
//                for (CommandeResponseDTO order : orders) {
//                    JPanel orderCard = createOrderCard(order);
//                    ordersPanel.add(orderCard);
//                    ordersPanel.add(Box.createVerticalStrut(15));
//                }
//            }
//
//            ordersPanel.revalidate();
//            ordersPanel.repaint();
//
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this,
//                    "Failed to load orders: " + ex.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    /**
//     * Create order card
//     */
//    private JPanel createOrderCard(CommandeResponseDTO order) {
//        JPanel card = new JPanel(new BorderLayout(15, 15));
//        card.setBackground(Color.WHITE);
//        card.setBorder(BorderFactory.createCompoundBorder(
//                new LineBorder(LIGHT_GRAY, 1),
//                new EmptyBorder(20, 20, 20, 20)
//        ));
//        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
//
//        // Header
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        headerPanel.setBackground(Color.WHITE);
//
//        JLabel lblOrderId = new JLabel("Order #" + order.getIdCommande());
//        lblOrderId.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblOrderId.setForeground(PRIMARY_COLOR);
//
//        JLabel lblStatus = new JLabel(order.getStatut().toString());
//        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        lblStatus.setOpaque(true);
//        lblStatus.setBorder(new EmptyBorder(5, 15, 5, 15));
//
//        // Status colors
//        switch (order.getStatut().toString()) {
//            case "EN_ATTENTE":
//                lblStatus.setBackground(WARNING_COLOR);
//                break;
//            case "EN_PREPARATION":
//                lblStatus.setBackground(new Color(52, 152, 219));
//                break;
//            case "PRET":
//                lblStatus.setBackground(SUCCESS_COLOR);
//                break;
//            case "LIVRE":
//                lblStatus.setBackground(new Color(149, 165, 166));
//                break;
//            case "ANNULE":
//                lblStatus.setBackground(DANGER_COLOR);
//                break;
//            default:
//                lblStatus.setBackground(Color.GRAY);
//        }
//        lblStatus.setForeground(Color.WHITE);
//
//        headerPanel.add(lblOrderId, BorderLayout.WEST);
//        headerPanel.add(lblStatus, BorderLayout.EAST);
//
//        // Details
//        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
//        detailsPanel.setBackground(Color.WHITE);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//        JLabel lblDate = new JLabel("📅 Date: " + order.getDateCommande().format(formatter));
//        lblDate.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
//
//        JLabel lblType = new JLabel("📦 Type: " + order.getTypeCommande());
//        lblType.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
//
//        JLabel lblTotal = new JLabel("💰 Total: " + order.getMontantTotal() + " DH");
//        lblTotal.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
//        lblTotal.setForeground(SUCCESS_COLOR);
//
//        JLabel lblItems = new JLabel("🍽️ Items: " + order.getLignesCommande().size() + " dish(es)");
//        lblItems.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
//
//        detailsPanel.add(lblDate);
//        detailsPanel.add(lblType);
//        detailsPanel.add(lblItems);
//        detailsPanel.add(lblTotal);
//
//        // Items list
//        JPanel itemsPanel = new JPanel();
//        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
//        itemsPanel.setBackground(LIGHT_GRAY);
//        itemsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//
//        for (LigneCommandeDTO ligne : order.getLignesCommande()) {
//            JLabel lblItem = new JLabel("• " + ligne.getPlatNom() + " x" + ligne.getQuantite() + " - " + ligne.getSousTotal() + " DH");
//            lblItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//            itemsPanel.add(lblItem);
//        }
//
//        card.add(headerPanel, BorderLayout.NORTH);
//        card.add(detailsPanel, BorderLayout.CENTER);
//        card.add(itemsPanel, BorderLayout.SOUTH);
//
//        return card;
//    }
//
//    // ==========================================
//    // MAIN METHOD
//    // ==========================================
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            RestaurantAppUI frame = new RestaurantAppUI();
//            frame.setVisible(true);
//        });
//    }
//}