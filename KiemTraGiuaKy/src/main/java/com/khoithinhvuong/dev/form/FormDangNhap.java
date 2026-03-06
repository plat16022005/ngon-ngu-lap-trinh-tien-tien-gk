package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.config.ServiceFactory;
import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.service.AuthService;
import com.khoithinhvuong.dev.service.StudentService;

import javax.swing.*;
import java.io.Console;

public class FormDangNhap {
    private AuthService authService = new AuthService();
    private JPanel mainPanel;
    private JTextField usernameBox;
    private JTextField passwordBox;
    private JButton dangnhapButton;
    private JButton dangkyButton;

    public FormDangNhap() {
        dangkyButton.addActionListener(e -> {

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);

            JFrame dangKyFrame = new JFrame("Đăng ký");

            FormDangKy formDangKy = new FormDangKy();

            dangKyFrame.setContentPane(formDangKy.getMainPanel());
            dangKyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dangKyFrame.pack();
            dangKyFrame.setLocationRelativeTo(null);
            dangKyFrame.setVisible(true);

            frame.dispose(); // đóng form login
        });
        dangnhapButton.addActionListener(e -> {
            if (authService.login(usernameBox.getText(), passwordBox.getText()))
            {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);

                JFrame hocsinhFrame = new JFrame("Học sinh");

                FormHocSinh formHocSinh = new FormHocSinh();

                hocsinhFrame.setContentPane(formHocSinh.getMainPanel());
                hocsinhFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                hocsinhFrame.pack();
                hocsinhFrame.setLocationRelativeTo(null);
                hocsinhFrame.setVisible(true);

                frame.dispose();
            }
            else
            {
                System.out.println("Đăng nhập thất bại");
            }
        });
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }
}