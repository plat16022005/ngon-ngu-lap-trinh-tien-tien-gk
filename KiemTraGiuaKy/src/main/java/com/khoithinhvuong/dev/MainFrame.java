package com.khoithinhvuong.dev;

import com.khoithinhvuong.dev.form.FormAdmin;
import javax.swing.*;

public class MainFrame extends JFrame {
    // Biến static giúp các Form con (Course, Class) gọi điều hướng dễ dàng
    private static FormAdmin adminPanel;

    public MainFrame() {
        setTitle("Giao diện Test Điều hướng - Admin Mode");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Nạp FormAdmin vào Frame chính
        adminPanel = new FormAdmin();
        setContentPane(adminPanel.getMainPanel());
    }

    // Cầu nối điều hướng tĩnh
    public static void showClassOfCourse(Long courseId) {
        if (adminPanel != null) adminPanel.showClassOfCourse(courseId);
    }

    public static void showScheduleOfClass(Long classId) {
        if (adminPanel != null) adminPanel.showScheduleOfClass(classId);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}