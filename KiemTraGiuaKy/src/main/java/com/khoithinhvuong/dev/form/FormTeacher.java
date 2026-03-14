package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.UserAccount;

import javax.swing.*;
import java.awt.*;

public class FormTeacher {
    private JPanel mainPanel;
    private JPanel MenuPanel;
    private JButton myClassesButton;
    private JButton attendanceButton;
    private JButton enterResultButton;
    private JButton logoutButton;
    private JScrollPane contentPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private final Long loginUserId;

    public FormTeacher(UserAccount userAccount){
        this.loginUserId = userAccount.getRelatedId();

        // Màn hình mặc định
        TeacherScheduleForm teacherScheduleForm = new TeacherScheduleForm(loginUserId);
        showPanel(teacherScheduleForm.getMainPanel());

        // Nhấn My Classes
        myClassesButton.addActionListener(e -> {
            TeacherScheduleForm form = new TeacherScheduleForm(loginUserId);
            showPanel(form.getMainPanel());
        });

        // Nhấn Attendance
        attendanceButton.addActionListener(e -> {
            TeacherAttendanceForm form = new TeacherAttendanceForm(loginUserId);
            showPanel(form.getMainPanel());
        });
        enterResultButton.addActionListener(e -> {
            TeacherResultsForm form = new TeacherResultsForm(loginUserId);
            showPanel(form.getMainPanel());
        });
        logoutButton.addActionListener(e -> {

            Window window = SwingUtilities.getWindowAncestor(mainPanel);
            window.dispose(); // đóng FormTeacher

            JFrame frame = new JFrame("Đăng nhập");

            FormDangNhap form = new FormDangNhap();

            frame.setContentPane(form.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void showPanel(JPanel newPanel) {
        contentPanel.setViewportView(newPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}