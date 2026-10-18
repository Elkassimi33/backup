package com.fst.rsi.ui.client;

import com.fst.rsi.dtos.*;
import com.fst.rsi.model.CartItem;
import com.fst.rsi.model.TypeCommande;
import com.fst.rsi.service.CommandeApiService;
import com.fst.rsi.service.TokenManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartPanel extends JPanel {
    private final RestaurantAppUI app;
    private final CommandeApiService commandeService;
    private TokenManager tokenManager;

    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel lblCartTotal;
    private JTextArea txtSpecialInstructions;
    private JComboBox<String> cmbOrderType;
    private JButton btnPlaceOrder, btnClearCart;

    private List<CartItem> cartItems = new ArrayList<>();

    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);

    public CartPanel(RestaurantAppUI app,TokenManager tokenManager) {
        this.app = app;
        this.commandeService = new CommandeApiService(tokenManager);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("🛒 Shopping Cart");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        title.setForeground(PRIMARY_COLOR);

        // Cart table
        String[] columns = {"Dish", "Price", "Quantity", "Subtotal", "Actions"};
        cartTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        cartTable = new JTable(cartTableModel);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cartTable.setRowHeight(40);
        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        cartTable.getTableHeader().setBackground(LIGHT_GRAY);

        // Add button renderer for actions
        cartTable.getColumn("Actions").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            JButton btnRemove = new JButton("❌");
            btnRemove.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            btnRemove.setFocusPainted(false);
            panel.add(btnRemove);
            return panel;
        });

        cartTable.getColumn("Actions").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                JButton btnRemove = new JButton("❌");
                btnRemove.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                btnRemove.setFocusPainted(false);
                btnRemove.addActionListener(e -> {
                    removeFromCart(row);
                    fireEditingStopped();
                });
                panel.add(btnRemove);
                return panel;
            }
        });

        JScrollPane tableScroll = new JScrollPane(cartTable);
        tableScroll.setBorder(new LineBorder(LIGHT_GRAY, 1));

        // Order details panel
        JPanel detailsPanel = createDetailsPanel();

        // Layout
        add(title, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(LIGHT_GRAY, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Order type
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblOrderType = new JLabel("Order Type:");
        lblOrderType.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblOrderType, gbc);

        gbc.gridx = 1;
        cmbOrderType = new JComboBox<>(new String[]{"LIVRAISON", "A_EMPORTER"});
        cmbOrderType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(cmbOrderType, gbc);

        // Special instructions
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblInstructions = new JLabel("Special Instructions:");
        lblInstructions.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblInstructions, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        txtSpecialInstructions = new JTextArea(3, 20);
        txtSpecialInstructions.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSpecialInstructions.setLineWrap(true);
        txtSpecialInstructions.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane instructionsScroll = new JScrollPane(txtSpecialInstructions);
        panel.add(instructionsScroll, gbc);

        // Total
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel lblTotalLabel = new JLabel("Total:");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(lblTotalLabel, gbc);

        gbc.gridx = 1;
        lblCartTotal = new JLabel("0.00 DH");
        lblCartTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblCartTotal.setForeground(SUCCESS_COLOR);
        panel.add(lblCartTotal, gbc);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonsPanel.setOpaque(false);

        btnClearCart = new JButton("Clear Cart");
        btnClearCart.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClearCart.setBackground(DANGER_COLOR);
        btnClearCart.setForeground(Color.WHITE);
        btnClearCart.setFocusPainted(false);
        btnClearCart.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnClearCart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClearCart.setContentAreaFilled(false);
        btnClearCart.setOpaque(true);
        btnClearCart.addActionListener(e -> handleClearCart());

        btnPlaceOrder = new JButton("Place Order");
        btnPlaceOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPlaceOrder.setBackground(SUCCESS_COLOR);
        btnPlaceOrder.setForeground(Color.WHITE);
        btnPlaceOrder.setFocusPainted(false);
        btnPlaceOrder.setBorder(new EmptyBorder(10, 30, 10, 30));
        btnPlaceOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPlaceOrder.setContentAreaFilled(false);
        btnPlaceOrder.setOpaque(true);
        btnPlaceOrder.addActionListener(e -> handlePlaceOrder());

        buttonsPanel.add(btnClearCart);
        buttonsPanel.add(btnPlaceOrder);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        panel.add(buttonsPanel, gbc);

        return panel;
    }

    public void addToCart(com.fst.rsi.dtos.PlatDTO plat) {
        CartItem existingItem = cartItems.stream()
                .filter(item -> item.getPlat().getIdPlat().equals(plat.getIdPlat()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.incrementQuantity();
        } else {
            cartItems.add(new CartItem(plat));
        }

        updateCartUI();
    }

    private void removeFromCart(int index) {
        if (index >= 0 && index < cartItems.size()) {
            CartItem item = cartItems.get(index);
            cartItems.remove(index);
            updateCartUI();

            JOptionPane.showMessageDialog(app,
                    item.getPlat().getNom() + " removed from cart",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void updateCartUI() {
        cartTableModel.setRowCount(0);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Object[] row = {
                    item.getPlat().getNom(),
                    item.getPlat().getPrix() + " DH",
                    item.getQuantity(),
                    item.getSousTotal() + " DH",
                    "Remove"
            };
            cartTableModel.addRow(row);
            total = total.add(item.getSousTotal());
        }

        lblCartTotal.setText(total + " DH");

        // Update badge in menu panel
        app.getMenuPanel().updateCartBadge();
    }

    private void handleClearCart() {
        int confirm = JOptionPane.showConfirmDialog(app,
                "Are you sure you want to clear the cart?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cartItems.clear();
            updateCartUI();

            JOptionPane.showMessageDialog(app,
                    "Cart cleared",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handlePlaceOrder() {
        try {
            Long currentClientId = app.getCurrentClient().getIdClient();
            if (currentClientId == null) {
                JOptionPane.showMessageDialog(app,
                        "Please load your profile first (Profile tab)",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (cartItems.isEmpty()) {
                JOptionPane.showMessageDialog(app,
                        "Your cart is empty",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<LigneCommandeRequestDTO> lignes = cartItems.stream()
                    .map(item -> LigneCommandeRequestDTO.builder()
                            .idPlat(item.getPlat().getIdPlat())
                            .quantite(item.getQuantity())
                            .options(item.getOptions())
                            .build())
                    .collect(Collectors.toList());

            CommandeRequestDTO request = CommandeRequestDTO.builder()
                    .idClient(currentClientId)
                    .typeCommande(TypeCommande.valueOf((String) cmbOrderType.getSelectedItem()))
                    .lignesCommande(lignes)
                    .instructionsSpeciales(txtSpecialInstructions.getText().trim())
                    .build();

            CommandeResponseDTO response = commandeService.createCommande(request);

            JOptionPane.showMessageDialog(app,
                    "Order placed successfully!\nOrder ID: " + response.getIdCommande() +
                            "\nTotal: " + response.getMontantTotal() + " DH",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            cartItems.clear();
            updateCartUI();
            txtSpecialInstructions.setText("");

            app.getOrdersPanel().handleLoadOrders();
            app.getTabbedPane().setSelectedIndex(4);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(app,
                    "Failed to place order: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getTotalItems() {
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }
}