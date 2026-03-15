package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.MainFrame;
import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.model.UserAccount;
import com.khoithinhvuong.dev.service.AuthService;
import com.khoithinhvuong.dev.service.StudentService;
import com.khoithinhvuong.dev.service.TeacherService;
import com.khoithinhvuong.dev.service.UserAccountService;

import javax.swing.*;

public class FormDangNhap {

    private JPanel mainPanel;
    private JTextField usernameBox;
    private JPasswordField passwordBox;
    private JButton dangnhapButton;
    private JButton dangkyButton;

    private AuthService authService = new AuthService();
    private UserAccountService userAccountService = new UserAccountService();
    private StudentService studentService = new StudentService();
    private TeacherService teacherService = new TeacherService();

    public FormDangNhap() {

        dangkyButton.addActionListener(e -> openRegisterForm());

        dangnhapButton.addActionListener(e -> login());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void openRegisterForm(){

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);

        JFrame dangKyFrame = new JFrame("Đăng ký");

        FormDangKy formDangKy = new FormDangKy();

        dangKyFrame.setContentPane(formDangKy.getMainPanel());
        dangKyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dangKyFrame.pack();
        dangKyFrame.setLocationRelativeTo(null);
        dangKyFrame.setVisible(true);

        frame.dispose();
    }

    private void login(){

        String username = usernameBox.getText();
        String password = new String(passwordBox.getPassword());

        if (!authService.login(username, password)) {

            JOptionPane.showMessageDialog(mainPanel, "Sai tài khoản hoặc mật khẩu!");
            return;
        }

        UserAccount user = userAccountService.getUserByUserName(username);

        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);

        switch (user.getRole()) {

            case STUDENT:

                Student student = studentService.findStudentById(user.getRelatedId());

                if (!"Active".equals(student.getStatus())) {
                    JOptionPane.showMessageDialog(mainPanel, "Tài khoản đã bị khóa!");
                    return;
                }

                openStudentForm(user, currentFrame);
                break;

            case TEACHER:

                Teacher teacher = teacherService.getTeacherById(user.getRelatedId());

                if (!"Active".equals(teacher.getStatus())) {
                    JOptionPane.showMessageDialog(mainPanel, "Tài khoản đã bị khóa!");
                    return;
                }

                openTeacherForm(user, currentFrame);
                break;

            case ADMIN:

                currentFrame.dispose();

                MainFrame mainFrame = new MainFrame(user.getRelatedId());
                mainFrame.setVisible(true);
                break;
        }
    }

    private void openStudentForm(UserAccount user, JFrame currentFrame){

        JFrame newFrame = new JFrame("Student");

        FormStudent formStudent = new FormStudent(user);

        newFrame.setContentPane(formStudent.getMainPanel());
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.pack();
        newFrame.setLocationRelativeTo(null);
        newFrame.setVisible(true);

        currentFrame.dispose();
    }

    private void openTeacherForm(UserAccount user, JFrame currentFrame){

        JFrame newFrame = new JFrame("Teacher");

        FormTeacher formTeacher = new FormTeacher(user);

        newFrame.setContentPane(formTeacher.getMainPanel());
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.pack();
        newFrame.setLocationRelativeTo(null);
        newFrame.setVisible(true);

        currentFrame.dispose();
    }
}