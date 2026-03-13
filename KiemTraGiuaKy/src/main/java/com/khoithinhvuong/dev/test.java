package com.khoithinhvuong.dev;

import com.khoithinhvuong.dev.form.StudentEnrollmentForm;

import javax.swing.*;

public class test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Học sinh Đăng ký Lớp");
            // Giả sử học sinh có ID = 1 đang đăng nhập
            frame.setContentPane(new StudentEnrollmentForm(1L).getEnrollmentPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 450);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
