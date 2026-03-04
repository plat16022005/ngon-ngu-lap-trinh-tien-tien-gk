package com.khoithinhvuong.dev;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Java Swing Demo");
        setSize(400, 300);
        setLocationRelativeTo(null); // căn giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Hello Java Swing!", SwingConstants.CENTER);
        add(label);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}