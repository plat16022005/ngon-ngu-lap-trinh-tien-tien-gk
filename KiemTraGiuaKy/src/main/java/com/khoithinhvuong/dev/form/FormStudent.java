package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.UserAccount;
import javax.swing.*;

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


        myCourseButton.addActionListener(e -> {

        });


        logoutButton.addActionListener(e -> {

        });
    }
}