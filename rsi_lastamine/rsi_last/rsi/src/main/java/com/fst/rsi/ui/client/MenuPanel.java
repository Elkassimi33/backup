package com.fst.rsi.ui.client;


import com.fst.rsi.dtos.PlatDTO;
import com.fst.rsi.service.PlatApiService;
import com.fst.rsi.service.TokenManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class MenuPanel extends JPanel {
    private final RestaurantAppUI app;
    private final PlatApiService platService;

    private JPanel platsGridPanel;
    private JComboBox<String> cmbCategories;
    private JTextField txtSearch;
    private JButton btnRefreshMenu, btnSearchMenu;
    private JLabel lblPlatsCount, lblCartBadge;

    private List<PlatDTO> allPlats;

    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);

    public MenuPanel(RestaurantAppUI app,TokenManager tokenManager) {
        this.app = app;
        this.platService = new PlatApiService(tokenManager);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Toolbar
        JPanel toolbar = createToolbar();

        // Grid panel for dishes
        platsGridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        platsGridPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(platsGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(toolbar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Auto-load menu
        SwingUtilities.invokeLater(this::handleLoadMenu);
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Left side - filters
        JPanel filtersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        filtersPanel.setBackground(Color.WHITE);

        JLabel lblFilter = new JLabel("Category:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));

        cmbCategories = new JComboBox<>(new String[]{"All Categories"});
        cmbCategories.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbCategories.setPreferredSize(new Dimension(180, 35));
        cmbCategories.addActionListener(e -> filterPlatsByCategory());

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));

        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));

        btnSearchMenu = new JButton("🔍 Search");
        btnSearchMenu.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnSearchMenu.setBackground(PRIMARY_COLOR);
        btnSearchMenu.setForeground(Color.WHITE);
        btnSearchMenu.setFocusPainted(false);
        btnSearchMenu.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnSearchMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSearchMenu.setContentAreaFilled(false);
        btnSearchMenu.setOpaque(true);
        btnSearchMenu.addActionListener(e -> handleSearchMenu());

        btnRefreshMenu = new JButton("↻ Refresh");
        btnRefreshMenu.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnRefreshMenu.setBackground(SUCCESS_COLOR);
        btnRefreshMenu.setForeground(Color.WHITE);
        btnRefreshMenu.setFocusPainted(false);
        btnRefreshMenu.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnRefreshMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefreshMenu.setContentAreaFilled(false);
        btnRefreshMenu.setOpaque(true);
        btnRefreshMenu.addActionListener(e -> handleLoadMenu());

        filtersPanel.add(lblFilter);
        filtersPanel.add(cmbCategories);
        filtersPanel.add(Box.createHorizontalStrut(10));
        filtersPanel.add(lblSearch);
        filtersPanel.add(txtSearch);
        filtersPanel.add(btnSearchMenu);
        filtersPanel.add(btnRefreshMenu);

        // Right side - count + cart badge
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);

        lblPlatsCount = new JLabel("0 dishes");
        lblPlatsCount.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblPlatsCount.setForeground(Color.GRAY);
        rightPanel.add(lblPlatsCount);

        rightPanel.add(Box.createHorizontalStrut(15));

        lblCartBadge = new JLabel("🛒 0");
        lblCartBadge.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        lblCartBadge.setForeground(PRIMARY_COLOR);
        lblCartBadge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblCartBadge.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (app.getCurrentClient() != null) {
                    app.getTabbedPane().setSelectedIndex(3);
                } else {
                    JOptionPane.showMessageDialog(app,
                            "Please register first!",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        rightPanel.add(lblCartBadge);

        toolbar.add(filtersPanel, BorderLayout.WEST);
        toolbar.add(rightPanel, BorderLayout.EAST);

        return toolbar;
    }

    public void handleLoadMenu() {
        try {
            allPlats = platService.getPlatsDisponibles();
            displayPlats(allPlats);

            List<String> categories = allPlats.stream()
                    .map(PlatDTO::getCategorie)
                    .distinct()
                    .sorted()
                    .toList();

            cmbCategories.removeAllItems();
            cmbCategories.addItem("All Categories");
            categories.forEach(cmbCategories::addItem);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(app,
                    "Failed to load menu: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayPlats(List<PlatDTO> plats) {
        platsGridPanel.removeAll();

        for (PlatDTO plat : plats) {
            if (plat.getDisponible()) {
                JPanel platCard = createPlatCard(plat);
                platsGridPanel.add(platCard);
            }
        }

        lblPlatsCount.setText(plats.size() + " dish(es)");
        updateCartBadge();
        platsGridPanel.revalidate();
        platsGridPanel.repaint();
    }

    private JPanel createPlatCard(PlatDTO plat) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(280, 320));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Image placeholder
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(250, 150));
        imagePanel.setBackground(LIGHT_GRAY);
        JLabel imgLabel = new JLabel("🍽️", SwingConstants.CENTER);
        imgLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 60));
        imagePanel.add(imgLabel);

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);

        JLabel lblName = new JLabel(plat.getNom());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblName.setForeground(PRIMARY_COLOR);

        JLabel lblCategory = new JLabel("Category: " + plat.getCategorie());
        lblCategory.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCategory.setForeground(Color.GRAY);

        JLabel lblPrice = new JLabel(plat.getPrix() + " DH");
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPrice.setForeground(SUCCESS_COLOR);

        JButton btnAdd = new JButton("➕ Add to Cart");
        btnAdd.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnAdd.setBackground(PRIMARY_COLOR);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAdd.setContentAreaFilled(false);
        btnAdd.setOpaque(true);
        btnAdd.addActionListener(e -> addToCart(plat));

        infoPanel.add(lblName);
        infoPanel.add(lblCategory);
        infoPanel.add(lblPrice);
        infoPanel.add(btnAdd);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private void filterPlatsByCategory() {
        if (allPlats == null) return;

        String selectedCategory = (String) cmbCategories.getSelectedItem();
        List<PlatDTO> filtered;

        if ("All Categories".equals(selectedCategory)) {
            filtered = allPlats;
        } else {
            filtered = allPlats.stream()
                    .filter(p -> p.getCategorie().equals(selectedCategory))
                    .collect(Collectors.toList());
        }

        displayPlats(filtered);
    }

    private void handleSearchMenu() {
        if (allPlats == null) return;

        String searchText = txtSearch.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            displayPlats(allPlats);
            return;
        }

        List<PlatDTO> filtered = allPlats.stream()
                .filter(p -> p.getNom().toLowerCase().contains(searchText) ||
                        p.getCategorie().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        displayPlats(filtered);
    }

    private void addToCart(PlatDTO plat) {
        CartPanel cartPanel = app.getCartPanel();
        cartPanel.addToCart(plat);

        updateCartBadge();

        JOptionPane.showMessageDialog(app,
                plat.getNom() + " added to cart!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateCartBadge() {
        CartPanel cartPanel = app.getCartPanel();
        int totalItems = cartPanel.getTotalItems();
        lblCartBadge.setText("🛒 " + totalItems);
    }
}