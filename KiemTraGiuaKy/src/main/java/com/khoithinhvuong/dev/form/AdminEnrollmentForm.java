package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.model.Course;
import com.khoithinhvuong.dev.model.Enrollment;
import com.khoithinhvuong.dev.service.ClazzService;
import com.khoithinhvuong.dev.service.CourseService;
import com.khoithinhvuong.dev.service.EnrollmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminEnrollmentForm {
    private JPanel adminEnrollmentForm;
    private JComboBox<String> courseCbx;
    private JComboBox<String> classCbx;
    private JTextField stateTxt;
    private JButton filterListBtn;
    private JTable listEnrollmentStudentTable;
    private JButton submitBtn;
    private JTextField capTxt;

    private DefaultTableModel tableModel;

    // Các Service
    private final CourseService courseService;
    private final ClazzService clazzService;
    private final EnrollmentService enrollmentService;

    // Dictionary để map tên hiển thị với ID
    private Map<String, Long> courseDictionary = new HashMap<>();
    private Map<String, Long> classDictionary = new HashMap<>();

    public JPanel getAdminEnrollmentForm() {
        return adminEnrollmentForm;
    }

    public AdminEnrollmentForm() {
        this.courseService = new CourseService();
        this.clazzService = new ClazzService();
        this.enrollmentService = new EnrollmentService();

        initComponents();
        setupListeners();
        loadInitialData();
    }

    private void initComponents() {
        String[] columns = {"Mã Ghi Danh", "Tên Học Sinh", "Khóa Học", "Lớp", "Ngày ĐK", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        listEnrollmentStudentTable.setModel(tableModel);
        listEnrollmentStudentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        capTxt.setEditable(false);
        capTxt.setHorizontalAlignment(JTextField.CENTER);

        stateTxt.setText("ĐÃ THANH TOÁN");

        submitBtn.setEnabled(false);
    }

    private void loadInitialData() {
        courseCbx.addItem("--- Chọn Khóa Học ---");
        List<Course> courses = courseService.getAllCourses();
        for (Course c : courses) {
            courseCbx.addItem(c.getCourseName());
            courseDictionary.put(c.getCourseName(), c.getCourseId());
        }
    }

    private void setupListeners() {
        // Sự kiện: Khi đổi Khóa học -> Load danh sách Lớp
        courseCbx.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                classCbx.removeAllItems();
                capTxt.setText("");
                String selectedCourse = (String) courseCbx.getSelectedItem();

                if (selectedCourse != null && !selectedCourse.equals("--- Chọn Khóa Học ---")) {
                    classCbx.addItem("--- Chọn Lớp Học ---");
                    Long courseId = courseDictionary.get(selectedCourse);

                    if (courseId != null) {
                        List<Clazz> classes = clazzService.getClassByCourse(courseId);
                        for (Clazz clazz : classes) {
                            classCbx.addItem(clazz.getClassName());
                            classDictionary.put(clazz.getClassName(), clazz.getClassId());
                        }
                    }
                }
            }
        });

        // Sự kiện: Khi đổi Lớp học -> Cập nhật Sĩ số
        classCbx.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedClass = (String) classCbx.getSelectedItem();

                if (selectedClass != null && !selectedClass.equals("--- Chọn Lớp Học ---")) {
                    Long classId = classDictionary.get(selectedClass);
                    if (classId != null) {
                        Clazz clazz = clazzService.getClassById(classId);
                        // Đếm số học sinh đã được duyệt chính thức (Status = ACTIVE/APPROVED)
                        Long currentStudent = enrollmentService.countAccurateStudent(classId);
                        capTxt.setText(currentStudent + " / " + clazz.getMaxStudent() + " Học sinh");
                    }
                } else {
                    capTxt.setText("");
                }
            }
        });

        // Sự kiện: Lọc danh sách
        filterListBtn.addActionListener(e -> {
            tableModel.setRowCount(0); // Xóa dữ liệu cũ trên bảng

            String selectedClass = (String) classCbx.getSelectedItem();
            String targetStatus = stateTxt.getText().trim();

            if (selectedClass == null || selectedClass.equals("--- Chọn Lớp Học ---")) {
                JOptionPane.showMessageDialog(adminEnrollmentForm, "Vui lòng chọn lớp học để lọc danh sách!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Long classId = classDictionary.get(selectedClass);

            // Lấy toàn bộ enrollment và dùng Stream để lọc ra đúng classId và status
            List<Enrollment> enrollments = enrollmentService.getAllEnrollmentsSorted()
                    .stream()
                    .filter(en -> en.getClassId().equals(classId) && en.getStatus().equalsIgnoreCase(targetStatus))
                    .toList();

            for (Enrollment en : enrollments) {
                Object[] row = {
                        en.getEnrollmentId(),
                        en.getStudent().getFullName(),
                        courseCbx.getSelectedItem(), // Lấy tên khóa đang chọn
                        selectedClass,               // Lấy tên lớp đang chọn
                        en.getEnrollmentDate(),
                        en.getStatus()
                };
                tableModel.addRow(row);
            }
        });


        listEnrollmentStudentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                submitBtn.setEnabled(listEnrollmentStudentTable.getSelectedRow() != -1);
            }
        });

        // Sự kiện: Nút Duyệt học sinh (Đổi status)
        submitBtn.addActionListener(e -> {
            int selectedRow = listEnrollmentStudentTable.getSelectedRow();
            if (selectedRow != -1) {
                Long enrollmentId = (Long) tableModel.getValueAt(selectedRow, 0);
                String studentName = (String) tableModel.getValueAt(selectedRow, 1);
                String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);

                if ("ĐÃ DUYỆT".equalsIgnoreCase(currentStatus) || "ACTIVE".equalsIgnoreCase(currentStatus)) {
                    JOptionPane.showMessageDialog(adminEnrollmentForm, "Học sinh này đã được duyệt vào lớp rồi!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(adminEnrollmentForm,
                        "Xác nhận duyệt học sinh: " + studentName + " vào lớp?",
                        "Xác nhận duyệt", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Gọi Repository để update trạng thái thành ĐÃ DUYỆT (hoặc ACTIVE tùy quy ước của bạn)
                        enrollmentService.updateState(enrollmentId, "ACTIVE");

                        JOptionPane.showMessageDialog(adminEnrollmentForm, "Duyệt học sinh thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                        // Tự động load lại danh sách và sĩ số
                        filterListBtn.doClick();

                        // Kích hoạt lại ItemListener của classCbx để nó tự update dòng text Sĩ số
                        ItemEvent fakeEvent = new ItemEvent(classCbx, ItemEvent.ITEM_STATE_CHANGED, classCbx.getSelectedItem(), ItemEvent.SELECTED);
                        for (java.awt.event.ItemListener listener : classCbx.getItemListeners()) {
                            listener.itemStateChanged(fakeEvent);
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(adminEnrollmentForm, "Lỗi khi duyệt: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}