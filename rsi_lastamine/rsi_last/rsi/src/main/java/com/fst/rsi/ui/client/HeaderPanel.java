package com.fst.rsi.ui.client;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);

    public HeaderPanel() {
        super(new BorderLayout());
        setBackground(PRIMARY_COLOR);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel(" Restaurant Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Powered by RSI - Faculty of Sciences and Technology");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(255, 255, 255, 180));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(subtitle);

        add(textPanel, BorderLayout.WEST);
    }
}