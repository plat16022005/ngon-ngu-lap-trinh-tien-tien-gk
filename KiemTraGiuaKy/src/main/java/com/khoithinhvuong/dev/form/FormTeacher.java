package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.UserAccount;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class FormTeacher {
    private JPanel mainPanel;
    private JPanel MenuPanel;
    private JButton myClassesButton;
    private JButton myScheduleButton;
    private JButton attendanceButton;
    private JButton enterResultButton;
    private JButton logoutButton;
    private JScrollPane  contentPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private final Long loginUserId;

    public FormTeacher(UserAccount userAccount){
        this.loginUserId = userAccount.getRelatedId();

        TeacherScheduleForm teacherScheduleForm = new TeacherScheduleForm(loginUserId);
        showPanel(teacherScheduleForm.getMainPanel());
    }

    private void showPanel(JPanel newPanel) {
        contentPanel.setViewportView(newPanel);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

}
