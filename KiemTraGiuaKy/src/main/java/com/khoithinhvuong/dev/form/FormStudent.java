package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.UserAccount;
import javax.swing.*;
import java.awt.*;

public class FormStudent {
    private JButton myCourseButton;
    private JButton enrollmentButton;
    private JButton myResultButton;
    private JButton paymentInvoiceButton;
    private JButton myScheduleButton;
    private JButton logoutButton;
    private JPanel menuPanel;
    private JPanel mainPanel;
    private JScrollPane contentPanel;

    private final Long loggedInStudentId;

    public FormStudent(UserAccount userAccount) {
        this.loggedInStudentId = userAccount.getRelatedId();
        setupListeners();
        StudentEnrollmentForm enrollmentForm = new StudentEnrollmentForm(loggedInStudentId);
        showPanel(enrollmentForm.getEnrollmentPanel());

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void showPanel(JPanel newPanel) {
        contentPanel.setViewportView(newPanel);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setupListeners() {
        enrollmentButton.addActionListener(e -> {
            StudentEnrollmentForm enrollmentForm = new StudentEnrollmentForm(this.loggedInStudentId);
            showPanel(enrollmentForm.getEnrollmentPanel());
        });

        paymentInvoiceButton.addActionListener(e -> {
            StudentLogPaymentAndInvoiceForm paymentForm = new StudentLogPaymentAndInvoiceForm(loggedInStudentId);
            showPanel(paymentForm.getPayAndInvoiceForm());
        });
        myResultButton.addActionListener(e -> {
            StudentResultsForm form = new StudentResultsForm(loggedInStudentId);
            showPanel(form.getMainPanel());
        });

        myCourseButton.addActionListener(e -> {
            StudentEnrollmentForm enrollmentForm = new StudentEnrollmentForm(loggedInStudentId);
            showPanel(enrollmentForm.getEnrollmentPanel());
        });
        myScheduleButton.addActionListener(e -> {

            StudentScheduleForm scheduleForm =
                    new StudentScheduleForm(loggedInStudentId);

            showPanel(scheduleForm.getMainPanel());
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
}