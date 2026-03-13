package com.khoithinhvuong.dev.form;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class FormStudent {
    private JButton myCourseButton;
    private JButton myAttendanceButton;
    private JButton myResultButton;
    private JButton paymentInvoiceButton;
    private JButton myScheduleButton;
    private JButton logoutButton;
    private JPanel menuPanel;
    private JPanel mainPanel;
    private JScrollPane contentPanel;

    public JScrollPane getContentPanel() {
        return contentPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
