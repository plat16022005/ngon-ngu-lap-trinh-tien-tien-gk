package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Invoice;
import com.khoithinhvuong.dev.model.Payment;
import com.khoithinhvuong.dev.service.InvoiceService;
import com.khoithinhvuong.dev.service.PaymentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StudentLogPaymentAndInvoiceForm {
    private JPanel PayAndInvoicceForm;
    private JScrollPane paymentJscrollPanel;
    private JScrollPane InvoiceJscrollPanel;
    private JTable invoiceLogTable;
    private JButton seeInvoiceBtn;
    private JButton payBtn;
    private JTable payLogTable;

    private Long loggedInStudentId;
    private PaymentService paymentService;
    private InvoiceService invoiceService;

    public JPanel getPayAndInvoiceForm() {
        return PayAndInvoicceForm;
    }

    public StudentLogPaymentAndInvoiceForm(Long loggedInStudentId) {
        this.loggedInStudentId = loggedInStudentId;
        this.paymentService = new PaymentService();
        this.invoiceService = new InvoiceService();

        loadPaymentTable();
        loadInvoiceTable();
        setupListeners();
    }

    private void loadPaymentTable() {
        // 1. Tạo cấu trúc cột cho bảng Thanh toán
        String[] columnNames = {"Mã Thanh Toán", "Mã Ghi Danh", "Số tiền", "Ngày TT", "Hình thức", "Trạng thái"};
        DefaultTableModel payModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 2. Gọi Service lôi dữ liệu lên
        List<Payment> payments = paymentService.getPaymentsByStudent(loggedInStudentId);

        for (Payment p : payments) {
            Object[] row = {
                    p.getPaymentId(),
                    p.getEnrollment().getEnrollmentId(),
                    p.getAmount(),
                    p.getPaymentDate(),
                    p.getPaymentMethod(),
                    p.getStatus()
            };
            payModel.addRow(row);
        }

        payLogTable.setModel(payModel);
    }

    private void loadInvoiceTable() {
        String[] columnNames = {"Mã Hóa Đơn", "Mã Thanh Toán", "Tổng tiền", "Ngày xuất", "Trạng thái"};
        DefaultTableModel invoiceModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Invoice> invoices = invoiceService.getInvoicesByStudent(loggedInStudentId);

        for (Invoice i : invoices) {
            Object[] row = {
                    i.getInvoiceId(),
                    i.getPayment().getPaymentId(),
                    i.getTotalAmount(),
                    i.getIssueDate(),
                    i.getStatus()
            };
            invoiceModel.addRow(row);
        }


        invoiceLogTable.setModel(invoiceModel);
    }

    private void setupListeners() {
        // Sự kiện: Nút "Thanh toán" (Xem lại hoặc xử lý giao dịch đang chọn)
        payBtn.addActionListener(e -> {
            int selectedRow = payLogTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(PayAndInvoicceForm, "Vui lòng chọn một dòng trong bảng Lịch sử!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String status = (String) payLogTable.getValueAt(selectedRow, 5);

            if ("ĐÃ THANH TOÁN".equalsIgnoreCase(status) || "Thành công".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(PayAndInvoicceForm, "Giao dịch này đã được thanh toán thành công! Bạn không thể thanh toán lại.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Lấy Mã Ghi Danh (cột số 1) của dòng đang chọn (nếu nó chưa thanh toán)
            Long enrollmentId = (Long) payLogTable.getValueAt(selectedRow, 1);

            // Mở lại form PaymentForm để học sinh tiến hành nộp tiền
            JFrame paymentFrame = new JFrame("Cổng Thanh Toán Học Phí");
            StudentPaymentForm paymentForm = new StudentPaymentForm(loggedInStudentId, enrollmentId);
            paymentFrame.setContentPane(paymentForm.getPaymentForm());
            paymentFrame.setSize(600, 500);
            paymentFrame.setLocationRelativeTo(null);
            paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            paymentFrame.setVisible(true);
        });

        // Sự kiện: Nút "Xem hóa đơn"
        seeInvoiceBtn.addActionListener(e -> {
            int selectedRow = invoiceLogTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(PayAndInvoicceForm, "Vui lòng chọn một dòng trong bảng Hóa đơn!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy Mã Thanh Toán (cột số 1) của dòng hóa đơn đang chọn
            Long paymentId = (Long) invoiceLogTable.getValueAt(selectedRow, 1);

            // Mở StudentInvoiceForm
            JFrame invoiceFrame = new JFrame("Chi tiết Hóa Đơn");
            StudentInvoiceForm invoiceForm = new StudentInvoiceForm(loggedInStudentId, paymentId);
            invoiceFrame.setContentPane(invoiceForm.getInvoicePanel());
            invoiceFrame.setSize(500, 600);
            invoiceFrame.setLocationRelativeTo(null);
            invoiceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            invoiceFrame.setVisible(true);
        });
    }
}