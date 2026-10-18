package com.fst.rsi.ui;


import com.fst.rsi.dtos.PlatDTO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PlatCard extends JPanel {

    public PlatCard(PlatDTO plat, BufferedImage image) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 260)); // Taille fixe pour la grille
        setBorder(new LineBorder(new Color(230, 230, 230), 1, true));
        setBackground(Color.WHITE);

        // 1. Image (Haut)
        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(200, 140));
        lblImage.setBackground(new Color(245, 245, 245));
        lblImage.setOpaque(true);

        if (image != null) {
            // Redimensionner l'image pour qu'elle rentre
            Image scaled = image.getScaledInstance(180, 130, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaled));
        } else {
            lblImage.setText("No Image");
        }
        add(lblImage, BorderLayout.NORTH);

        // 2. Infos (Centre/Bas)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Catégorie
        JLabel lblCat = new JLabel(plat.getCategorie().toUpperCase());
        lblCat.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblCat.setForeground(Color.GRAY);

        // Nom
        JLabel lblNom = new JLabel(plat.getNom());
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Prix
        JLabel lblPrix = new JLabel(plat.getPrix() + " DH");
        lblPrix.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrix.setForeground(new Color(230, 126, 34)); // Orange
        lblPrix.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(lblCat);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lblNom);
        infoPanel.add(Box.createVerticalGlue()); // Pousse le prix vers le bas
        infoPanel.add(lblPrix);

        add(infoPanel, BorderLayout.CENTER);

        // Petit effet de survol (optionnel)
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}