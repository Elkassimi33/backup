package com.rsi.resto;

import com.rsi.resto.ui.LoginWindow;
import com.rsi.resto.ui.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginWindow loginWindow = new LoginWindow((authResponse, tokenManager) -> {
                // On successful login, open the main window
                MainWindow mainWindow = new MainWindow(authResponse, tokenManager);
                mainWindow.setVisible(true);
            });
            loginWindow.setVisible(true);
        });
    }
}