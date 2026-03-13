package com.khoithinhvuong.dev.form;

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
    private JPanel contentPanel;

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
