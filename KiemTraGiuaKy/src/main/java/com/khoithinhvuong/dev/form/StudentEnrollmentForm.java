package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.form.StudentPaymentForm;
import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.model.Course;
import com.khoithinhvuong.dev.model.Enrollment;
import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.service.ClazzService;
import com.khoithinhvuong.dev.service.CourseService;
import com.khoithinhvuong.dev.service.EnrollmentService;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.List;



public class StudentEnrollmentForm {
    private JComboBox<String> courseCbx;
    private JComboBox<String> classCbx;
    private JTextField feeTxt;
    private JTextField capacityTxt;
    private JTextField dateTxt;
    private JButton signInAndPayBtn;
    private JPanel enrollmentPanel;
    private JTextField durationTxt;

    private java.util.Map<String, Long> courseDictionary = new java.util.HashMap<>();

    private java.util.Map<String, Long> classDictionary = new java.util.HashMap<>();

    private final EnrollmentService enrollmentService;
    private final Long loggedInStudentId; // ID của học sinh đang đăng nhập vào hệ thống
    private final ClazzService clazzService;
    private final CourseService courseService ;


    // Truyền ID học sinh đang đăng nhập vào Form này
    public StudentEnrollmentForm(Long loggedInStudentId) {
        this.loggedInStudentId = loggedInStudentId;
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService();
        this.clazzService = new ClazzService();

        initComponents();
        setupListeners();
        loadInitialData();
    }

    // Hàm trả về JPanel gốc để gắn vào JFrame
    public JPanel getEnrollmentPanel() {
        return enrollmentPanel;
    }

    private void initComponents() {
        feeTxt.setEditable(false);
        capacityTxt.setEditable(false);
        dateTxt.setEditable(false);

        // Chỉnh cho chữ nằm giữa cho đẹp
        feeTxt.setHorizontalAlignment(JTextField.CENTER);
        capacityTxt.setHorizontalAlignment(JTextField.CENTER);
        dateTxt.setHorizontalAlignment(JTextField.CENTER);
    }

    private void loadInitialData() {
        // TODO: Gọi Database (CourseRepository) để lấy danh sách khóa học
        List<Course> courses = courseService.getAllCourses();

        courseCbx.addItem("--- Chọn Khóa Học ---");
        for (Course course: courses) {
            courseCbx.addItem(course.getCourseName());
            courseDictionary.put(course.getCourseName(), course.getCourseId());
        }
    }

    private void setupListeners() {
        courseCbx.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // Xóa dữ liệu UI của các ô bên dưới trước khi load mới
                classCbx.removeAllItems();
                clearDetails();

                String selectedCourseName = (String) courseCbx.getSelectedItem();

                if (selectedCourseName != null && !selectedCourseName.equals("--- Chọn Khóa Học ---")) {
                    classCbx.addItem("--- Chọn Lớp Học ---");

                    Long courseId = courseDictionary.get(selectedCourseName);

                    if (courseId != null) {
                        feeTxt.setText(courseService.getCourseById(courseId).getFee().toString() + " VNĐ");
                        durationTxt.setText(courseService.getCourseById(courseId).getDuration().toString());
                        List<Clazz> courseClass = clazzService.getClassByCourse(courseId);
                        for (Clazz clazz : courseClass) {
                            classCbx.addItem(clazz.getClassName());
                            classDictionary.put(clazz.getClassName(), clazz.getClassId());
                        }
                    }
                }
            }
        });


        classCbx.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedClass = (String) classCbx.getSelectedItem();

                if (selectedClass != null && !selectedClass.equals("--- Chọn Lớp Học ---")) {
                    Long classId = classDictionary.get(selectedClass);

                    if (classId != null) {
                        Clazz clazz = clazzService.getClassById(classId);
                        Long cap = enrollmentService.countAccurateStudent(classId);
                        capacityTxt.setText( cap.toString() + "/" + clazz.getMaxStudent().toString() + " Học sinh");
                        dateTxt.setText(clazz.getStartDate().toString() + " đến " + clazz.getEndDate().toString());
                    }
                } else {
                    capacityTxt.setText("");
                    dateTxt.setText("");
                }
            }
        });

        signInAndPayBtn.addActionListener(e -> {
            try {
                if (classCbx.getSelectedIndex() <= 0) {
                    JOptionPane.showMessageDialog(enrollmentPanel, "Vui lòng chọn lớp học trước khi đăng ký!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String selectedClassName = (String) classCbx.getSelectedItem();
                Long finalClassId = classDictionary.get(selectedClassName);

                Clazz clazz = clazzService.getClassById(finalClassId);
                Long cap = enrollmentService.countAccurateStudent(finalClassId);
                if (cap >= clazz.getMaxStudent().longValue() ){
                    JOptionPane.showMessageDialog(enrollmentPanel, "Vui lòng chọn lớp học khác vì đã full!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Enrollment enrollment = new Enrollment();

                Student currentStudent = new Student();
                currentStudent.setStudentId(loggedInStudentId);
                enrollment.setStudent(currentStudent);

                enrollment.setClassId(finalClassId);

                enrollment.setStatus("Waiting");

                enrollmentService.register(enrollment);


                JOptionPane.showMessageDialog(enrollmentPanel, "Ghi danh thành công! Đang chuyển sang cổng thanh toán...", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                JFrame paymentFrame = new JFrame("Cổng Thanh Toán Học Phí");
                StudentPaymentForm paymentForm = new StudentPaymentForm(loggedInStudentId,enrollment.getEnrollmentId());

                paymentFrame.setContentPane(paymentForm.getPaymentForm());

                paymentFrame.setSize(600, 500);
                paymentFrame.setLocationRelativeTo(null);

                paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                paymentFrame.setVisible(true);

            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(enrollmentPanel, ex.getMessage(), "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(enrollmentPanel, "Đã xảy ra lỗi hệ thống, vui lòng thử lại sau.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void clearDetails() {
        feeTxt.setText("");
        capacityTxt.setText("");
        dateTxt.setText("");
    }
}