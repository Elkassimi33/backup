package com.fst.rsi.ui.client;


import com.fst.rsi.dtos.ClientResponseDTO;
import com.fst.rsi.dtos.CommandeResponseDTO;
import com.fst.rsi.dtos.LigneCommandeDTO;
import com.fst.rsi.service.CommandeApiService;
import com.fst.rsi.service.TokenManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrdersPanel extends JPanel {
    private final RestaurantAppUI app;
    private final CommandeApiService commandeService;
    private TokenManager tokenManager;

    private JPanel ordersPanel;
    private JButton btnLoadOrders;
    private JButton annule;

    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(230, 126, 34);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);


    public OrdersPanel(TokenManager tokenManager, RestaurantAppUI app) {
        this.app = app;
        this.commandeService = new CommandeApiService(tokenManager);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("📋 My Orders");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        title.setForeground(PRIMARY_COLOR);

        btnLoadOrders = new JButton("↻ Refresh Orders");
        btnLoadOrders.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btnLoadOrders.setBackground(PRIMARY_COLOR);
        btnLoadOrders.setForeground(Color.WHITE);
        btnLoadOrders.setFocusPainted(false);
        btnLoadOrders.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnLoadOrders.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLoadOrders.setContentAreaFilled(false);
        btnLoadOrders.setOpaque(true);
        btnLoadOrders.addActionListener(e -> handleLoadOrders());

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnLoadOrders, BorderLayout.EAST);

        // Orders container
        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addEvent();
    }

    private void addEvent() {

//        annule.addActionListener(e -> {
//
//        });
    }

    public void handleLoadOrders() {
        try {
            ClientResponseDTO currentClient = app.getCurrentClient();
            if (currentClient == null) {
                JOptionPane.showMessageDialog(app,
                        "Please load your profile first",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<CommandeResponseDTO> orders = commandeService.getCommandesByClient(currentClient.getIdClient());

            ordersPanel.removeAll();

            if (orders.isEmpty()) {
                JLabel noOrders = new JLabel("No orders found");
                noOrders.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                noOrders.setForeground(Color.GRAY);
                ordersPanel.add(noOrders);
            } else {
                for (CommandeResponseDTO order : orders) {
                    JPanel orderCard = createOrderCard(order);
                    ordersPanel.add(orderCard);
                    ordersPanel.add(Box.createVerticalStrut(15));
                }
            }

            ordersPanel.revalidate();
            ordersPanel.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(app,
                    "Failed to load orders: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createOrderCard(CommandeResponseDTO order) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(LIGHT_GRAY, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel lblOrderId = new JLabel("Order #" + order.getIdCommande());
        lblOrderId.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblOrderId.setForeground(PRIMARY_COLOR);

        JLabel lblStatus = new JLabel(order.getStatut().toString());
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setOpaque(true);
        lblStatus.setBorder(new EmptyBorder(5, 15, 5, 15));

        annule = new JButton("Annule");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setOpaque(true);
        lblStatus.setBorder(new EmptyBorder(5, 15, 5, 15));


        switch (order.getStatut().toString()) {
            case "EN_ATTENTE":
                lblStatus.setBackground(WARNING_COLOR);
                break;
            case "EN_PREPARATION":
                lblStatus.setBackground(new Color(52, 152, 219));
                break;
            case "PRET":
                lblStatus.setBackground(SUCCESS_COLOR);
                break;
            case "LIVRE":
                lblStatus.setBackground(new Color(149, 165, 166));
                annule.setEnabled(false);
                break;
            case "ANNULE":
                lblStatus.setBackground(DANGER_COLOR);
                annule.setEnabled(false);
                break;
            default:
                lblStatus.setBackground(Color.GRAY);
                annule.setEnabled(false);
        }
        lblStatus.setForeground(Color.WHITE);
        annule.setBackground(Color.WHITE);
//        annule.addActionListener(e -> {
//            try {
//                commandeService.annulerCommande(order.getIdCommande());
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(ordersPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//        headerPanel.add(annule, BorderLayout.NORTH);
        headerPanel.add(lblOrderId, BorderLayout.WEST);
        headerPanel.add(lblStatus, BorderLayout.EAST);

        // Details
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        JLabel lblDate = new JLabel("📅 Date: " + order.getDateCommande().format(formatter));
        lblDate.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        JLabel lblType = new JLabel("📦 Type: " + order.getTypeCommande());
        lblType.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        JLabel lblTotal = new JLabel("💰 Total: " + order.getMontantTotal() + " DH");
        lblTotal.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        lblTotal.setForeground(SUCCESS_COLOR);

        JLabel lblItems = new JLabel("🍽️ Items: " + order.getLignesCommande().size() + " dish(es)");
        lblItems.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        detailsPanel.add(lblDate);
        detailsPanel.add(lblType);
        detailsPanel.add(lblItems);
        detailsPanel.add(lblTotal);


        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(LIGHT_GRAY);
        itemsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (LigneCommandeDTO ligne : order.getLignesCommande()) {
            JLabel lblItem = new JLabel("• " + ligne.getPlatNom() + " x" + ligne.getQuantite() + " - " + ligne.getSousTotal() + " DH");
            lblItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            itemsPanel.add(lblItem);
        }

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(itemsPanel, BorderLayout.SOUTH);

        return card;
    }
}