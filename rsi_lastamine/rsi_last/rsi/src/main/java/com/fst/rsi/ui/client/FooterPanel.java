package com.fst.rsi.ui.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FooterPanel extends JPanel {
    private static final Color LIGHT_GRAY = new Color(236, 240, 241);

    public FooterPanel() {
        super(new FlowLayout(FlowLayout.CENTER));
        setBackground(LIGHT_GRAY);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel footerLabel = new JLabel("© 2024 FST Restaurant System | Version 2.0");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(Color.GRAY);

        add(footerLabel);
    }
}