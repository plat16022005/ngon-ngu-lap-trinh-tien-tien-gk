package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.model.Invoice;
import com.khoithinhvuong.dev.service.ClazzService;
import com.khoithinhvuong.dev.service.InvoiceService;

import javax.swing.*;
import java.awt.*;

public class StudentInvoiceForm {
    private JPanel invoiceForm;
    private JTextField nameTxt;
    private JTextField courseTxt;
    private JTextField datePaymentTxt;
    private JTextField feeTxt;
    private JTextField maHoaDonTxt;
    private JTextField textField1; // Ô này tương ứng với "Hình thức thanh toán" trên UI

    private Long loguserid;

    public JPanel getInvoicePanel() {
        return invoiceForm;
    }

    // Đã sửa lỗi gán biến và thêm tham số paymentId
    public StudentInvoiceForm(Long loginuserid, Long paymentId) {
        this.loguserid = loginuserid;

        initComponents();
        loadInvoiceData(paymentId);
    }

    private void initComponents() {
        maHoaDonTxt.setEditable(false);
        nameTxt.setEditable(false);
        courseTxt.setEditable(false);
        datePaymentTxt.setEditable(false);
        textField1.setEditable(false);
        feeTxt.setEditable(false);
    }

    private void loadInvoiceData(Long paymentId) {
        InvoiceService invoiceService = new InvoiceService();
        Invoice invoice = invoiceService.getInvoiceByPayment(paymentId);

        if (invoice != null) {
            maHoaDonTxt.setText( "EC-" + String.valueOf(invoice.getInvoiceId()));
            feeTxt.setText(String.valueOf(invoice.getTotalAmount()));
            datePaymentTxt.setText(invoice.getIssueDate().toString());


            if (invoice.getStudent() != null) {
                nameTxt.setText(invoice.getStudent().getFullName());
            }

            if (invoice.getPayment() != null) {
                textField1.setText(invoice.getPayment().getPaymentMethod());


                Long classId = invoice.getPayment().getEnrollment().getClassId();
                ClazzService clazzService = new ClazzService();
                Clazz clazz = clazzService.getClassById(classId);
                courseTxt.setText(clazz.getCourse().getCourseName());
            }
        } else {
            JOptionPane.showMessageDialog(invoiceForm, "Không tìm thấy dữ liệu hóa đơn cho giao dịch này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}