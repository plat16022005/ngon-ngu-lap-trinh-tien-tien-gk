package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.MainFrame;
import com.khoithinhvuong.dev.config.ServiceFactory;
import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.UserAccount;
import com.khoithinhvuong.dev.service.AuthService;
import com.khoithinhvuong.dev.service.StudentService;
import com.khoithinhvuong.dev.service.UserAccountService;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.Console;
import java.util.Locale;

public class FormDangNhap {
    private AuthService authService = new AuthService();
    private JPanel mainPanel;
    private JTextField usernameBox;
    private JTextField passwordBox;
    private JButton dangnhapButton;
    private JButton dangkyButton;
    private UserAccountService userAccountService = new UserAccountService();

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

            if (authService.login(usernameBox.getText(), passwordBox.getText())) {

                UserAccount user = userAccountService.getUserByUserName(usernameBox.getText());

                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                JFrame newFrame = new JFrame();

                switch (user.getRole()) {

                    case STUDENT:
                        newFrame.setTitle("Học sinh");
                        FormStudent formStudent = new FormStudent(user);
                        newFrame.setContentPane(formStudent.getMainPanel());
                        break;

                    case TEACHER:
                        newFrame.setTitle("Teacher");
                        FormTeacher formTeacher = new FormTeacher(user);
                        newFrame.setContentPane(formTeacher.getMainPanel());
                        break;

                    case ADMIN:
                        frame.dispose();

                        MainFrame mainFrame = new MainFrame();
                        mainFrame.setVisible(true);
                        return;
                }

                newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                newFrame.pack();
                newFrame.setLocationRelativeTo(null);
                newFrame.setVisible(true);

                frame.dispose();

            } else {
                JOptionPane.showMessageDialog(mainPanel, "Sai tài khoản hoặc mật khẩu!");
            }

        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}