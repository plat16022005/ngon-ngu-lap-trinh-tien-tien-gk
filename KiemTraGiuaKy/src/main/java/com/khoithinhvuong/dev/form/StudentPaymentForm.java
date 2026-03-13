package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.*;
import com.khoithinhvuong.dev.service.*;

import javax.swing.*;

public class StudentPaymentForm {
    private JTextField idTxt;
    private JTextField courseEnrollTxt;
    private JTextField classEnrollTxt;
    private JTextField feeTxt;
    private JComboBox<String> tylePayCbx;
    private JButton payBtn;
    private JButton seeInvoiceBtn;
    private JPanel PaymentForm;
    private JTextField stateTxt;

    private Long logginUserId;

    public JPanel getPaymentForm() {
        return PaymentForm;
    }

    private  Long enrolmentId;
    private Long paymentId;
    public StudentPaymentForm(Long logginUserId, Long enrolmentId) {
        this.logginUserId = logginUserId;
        this.enrolmentId = enrolmentId;
        // 1. Khởi tạo cấu hình giao diện ban đầu
        initComponents();

        // 2. Cài đặt các sự kiện click chuột
        setupListeners();
    }

    private void initComponents() {
        seeInvoiceBtn.setEnabled(false);

        EnrollmentService enrollmentService = new EnrollmentService();


        ClazzService clazzService = new ClazzService();
        Clazz clazz = clazzService.getClassById(enrollmentService.getEnrollmentByStudent(logginUserId).getClassId());




        // Khóa các ô Text không cho người dùng tự ý sửa đổi thông tin thanh toán
        idTxt.setEditable(false);
        courseEnrollTxt.setEditable(false);
        classEnrollTxt.setEditable(false);
        feeTxt.setEditable(false);
        stateTxt.setEditable(false);

        // Thêm các hình thức thanh toán vào ComboBox
        tylePayCbx.addItem("Chuyển khoản");
        tylePayCbx.addItem("Tiền mặt");
        tylePayCbx.addItem("Quẹt thẻ");

        // Thiết lập trạng thái mặc định
        stateTxt.setText("CHỜ THANH TOÁN");
        idTxt.setText(enrolmentId.toString());
        courseEnrollTxt.setText(clazz.getCourse().getCourseName().toString());
        classEnrollTxt.setText(clazz.getClassName().toString());
        feeTxt.setText(clazz.getCourse().getFee().toString());
    }

    private void setupListeners() {
        // Sự kiện: Khi bấm nút "Thanh toán"
        payBtn.addActionListener(e -> {
            try {
                PaymentService paymentService = new PaymentService();

                // 2. Gom dữ liệu để tạo đối tượng Payment
                Payment newPayment = new Payment();

                Student student = new Student();
                student.setStudentId(logginUserId);
                newPayment.setStudent(student);


                Enrollment enrollment = new Enrollment();
                Long enrollmentId = Long.parseLong(idTxt.getText().trim());
                enrollment.setEnrollmentId(enrollmentId);
                newPayment.setEnrollment(enrollment);


                double amount = Double.parseDouble(feeTxt.getText().replace(",", "").replace(" VNĐ", "").trim());
                newPayment.setAmount(amount);

                // Gắn Hình thức thanh toán (Lấy từ ComboBox)
                String method = tylePayCbx.getSelectedItem().toString();
                newPayment.setPaymentMethod(method);

                // Gắn Trạng thái
                newPayment.setStatus("Thành công");

                // 3. Gọi Service đẩy xuống Database
                paymentService.createPayment(newPayment);

                paymentId = newPayment.getPaymentId();

                EnrollmentService enrollmentService = new EnrollmentService();
                enrollmentService.updateState(enrollmentId,"Đã thanh toán");

                InvoiceService invoiceService = new InvoiceService();
                Invoice newInvoice = new Invoice();

                newInvoice.setStudent(student); // Dùng lại thông tin học sinh ở trên
                newInvoice.setPayment(newPayment); // Truyền nguyên cái payment vừa tạo thành công vào đây
                newInvoice.setTotalAmount(amount); // Số tiền trên hóa đơn bằng số tiền thanh toán
                newInvoice.setStatus("Đã xuất"); // Trạng thái hóa đơn

                // Đẩy Invoice xuống Database
                invoiceService.createInvoice(newInvoice);

                JOptionPane.showMessageDialog(PaymentForm, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                seeInvoiceBtn.setEnabled(true);
                payBtn.setEnabled(false);
                stateTxt.setText("ĐÃ THANH TOÁN");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PaymentForm, "Lỗi dữ liệu đầu vào (Mã ghi danh hoặc Học phí không đúng định dạng số)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (RuntimeException ex) {
                // Chụp chính xác cái thông báo lỗi "Enrollment này đã thanh toán!" từ Service của bạn
                JOptionPane.showMessageDialog(PaymentForm, ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PaymentForm, "Có lỗi xảy ra trong quá trình thanh toán: " + ex.getMessage(), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
            }
        });

        seeInvoiceBtn.addActionListener(e -> {
            // Mở cửa sổ hiển thị hóa đơn (StudentInvoiceForm)
            JFrame invoiceFrame = new JFrame("Chi tiết hóa đơn");
            StudentInvoiceForm invoiceForm = new StudentInvoiceForm(logginUserId,paymentId);
            invoiceFrame.setContentPane(invoiceForm.getInvoicePanel()); // Tên Panel tùy bạn đặt bên form hóa đơn
            invoiceFrame.setSize(500, 600);
            invoiceFrame.setLocationRelativeTo(null);
            invoiceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            invoiceFrame.setVisible(true);
        });
    }
}