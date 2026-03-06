package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.config.ServiceFactory;
import com.khoithinhvuong.dev.service.AuthService;

import javax.swing.*;
import java.time.LocalDate;

public class FormDangKy {
    private AuthService authService = new AuthService();
    private JPanel mainPanel;
    private JTextField usernameBox;
    private JPasswordField passwordBox;
    private JPasswordField rePasswordBox;
    private JTextField hovatenBox;
    private JTextField emailBox;
    private JTextField diachiBox;
    private JSpinner ngaySpin;
    private JSpinner thangSpin;
    private JSpinner namSpin;
    private JTextField sodienthoaiBox;
    private JComboBox gioitinhCombo;
    private JButton dangkyButton;
    private JButton quaylaiButton;
    public FormDangKy()
    {
        quaylaiButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);

            JFrame dangnhapFrame = new JFrame("Đăng nhập");

            FormDangNhap formDangNhap = new FormDangNhap();

            dangnhapFrame.setContentPane(formDangNhap.getMainPanel());
            dangnhapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dangnhapFrame.pack();
            dangnhapFrame.setLocationRelativeTo(null);
            dangnhapFrame.setVisible(true);

            frame.dispose(); // đóng form login
        });
        dangkyButton.addActionListener(e -> {

            String username = usernameBox.getText();
            String password = new String(passwordBox.getPassword());
            String rePassword = new String(rePasswordBox.getPassword());

            String fullName = hovatenBox.getText();
            String email = emailBox.getText();
            String address = diachiBox.getText();
            String phone = sodienthoaiBox.getText();

            String gender = gioitinhCombo.getSelectedItem().toString();

            LocalDate dob = LocalDate.of(
                    (Integer) namSpin.getValue(),
                    (Integer) thangSpin.getValue(),
                    (Integer) ngaySpin.getValue()
            );

            if (authService.register(
                    fullName,
                    dob,
                    gender,
                    phone,
                    email,
                    address,
                    LocalDate.now(),
                    "Active",
                    username,
                    password,
                    rePassword
            )) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);

                JFrame dangnhapFrame = new JFrame("Đăng nhập");

                FormDangNhap formDangNhap = new FormDangNhap();

                dangnhapFrame.setContentPane(formDangNhap.getMainPanel());
                dangnhapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dangnhapFrame.pack();
                dangnhapFrame.setLocationRelativeTo(null);
                dangnhapFrame.setVisible(true);

                frame.dispose(); // đóng form login
            } else {
                System.out.println("Đăng ký thất bại");
            }
        });
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
