package com.khoithinhvuong.dev;

import com.khoithinhvuong.dev.form.FormDangNhap;

import javax.swing.*;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Đăng nhập");

            FormDangNhap form = new FormDangNhap();

            frame.setContentPane(form.getMainPanel());

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });

    }
}