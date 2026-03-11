package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.model.Course;
import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.service.ClazzService;
import com.khoithinhvuong.dev.service.CourseService;
import com.khoithinhvuong.dev.service.ScheduleService;
import com.khoithinhvuong.dev.service.TeacherService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class AdminClazzForm extends JPanel {
    // Khai báo các Service cần thiết
    private final ClazzService clazzService = new ClazzService();
    private final CourseService courseService = new CourseService();
    private final TeacherService teacherService = new TeacherService();
    private final ScheduleService scheduleService = new ScheduleService();

    private JPanel mainPanel; // Nếu em dùng GUI Designer
    private JTable table1;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtMaxStudent, txtStartDate, txtEndDate;
    private JComboBox<Course> cbCourse; //
    private JComboBox<Teacher> cbTeacher; //
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    public AdminClazzForm() {
        // Nếu dùng GUI Designer, dòng này là bắt buộc
        add(mainPanel);

        initTable();
        initData();   // Đổ dữ liệu vào JComboBox
        initEvents(); // Thiết lập sự kiện nút bấm
        refreshTable();
    }

    private void initTable() {
        String[] columns = {"ID", "Tên Lớp", "Khóa Học", "Giáo Viên", "Sĩ Số", "Ngày Bắt Đầu"};
        tableModel = new DefaultTableModel(columns, 0);
        table1.setModel(tableModel);
    }

    // Đổ dữ liệu từ Database vào các Combobox
    private void initData() {
        cbCourse.removeAllItems();
        courseService.getAllCourses().forEach(cbCourse::addItem);

        cbTeacher.removeAllItems();
        teacherService.getAllTeachers().forEach(cbTeacher::addItem);
    }

    private void initEvents() {
        // thêm mới
        btnAdd.addActionListener(e -> {
            try {
                clazzService.createClass(
                        txtName.getText(),
                        LocalDate.parse(txtStartDate.getText()),
                        LocalDate.parse(txtEndDate.getText()),
                        Integer.parseInt(txtMaxStudent.getText()),
                        (Teacher) cbTeacher.getSelectedItem(),
                        (Course) cbCourse.getSelectedItem()
                );
                refreshTable();
                JOptionPane.showMessageDialog(this, "Thêm lớp học thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        // Sự kiện Click vào bảng để lấy dữ liệu ngược lên Form
        table1.getSelectionModel().addListSelectionListener(e -> {
            int row = table1.getSelectedRow();
            if (row != -1) {
                Long id = (Long) tableModel.getValueAt(row, 0);
                Clazz clazz = clazzService.getClassById(id);
                txtId.setText(clazz.getClassId().toString());
                txtName.setText(clazz.getClassName());
                txtMaxStudent.setText(clazz.getMaxStudent().toString());
                txtStartDate.setText(clazz.getStartDate().toString());
                txtEndDate.setText(clazz.getEndDate().toString());
                cbCourse.setSelectedItem(clazz.getCourse());
                cbTeacher.setSelectedItem(clazz.getTeacher());
            }
        });

        btnClear.addActionListener(e -> clearFields());

        //Cập nhật
        btnUpdate.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty()) throw new Exception("Vui lòng chọn một lớp học để sửa!");

                Clazz clazz = clazzService.getClassById(Long.parseLong(txtId.getText()));
                clazz.setClassName(txtName.getText());
                clazz.setMaxStudent(Integer.parseInt(txtMaxStudent.getText()));
                clazz.setStartDate(LocalDate.parse(txtStartDate.getText()));
                clazz.setEndDate(LocalDate.parse(txtEndDate.getText()));
                clazz.setCourse((Course) cbCourse.getSelectedItem());
                clazz.setTeacher((Teacher) cbTeacher.getSelectedItem());

                clazzService.updateClass(clazz);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
            }
        });

        //Xoá
        btnDelete.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty()) return;
                Long classId = Long.parseLong(txtId.getText());

                int confirm = JOptionPane.showConfirmDialog(this, "Xóa lớp sẽ xóa toàn bộ lịch học liên quan. Tiếp tục?", "Xác nhận", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Bước 1: Gọi hàm lọc phức tạp để xóa hết Schedule của lớp này trước
                    scheduleService.deleteSchedulesByClass(classId);

                    // Bước 2: Sau khi "sạch" lịch học, mới xóa lớp
                    clazzService.deleteClass(classId);

                    refreshTable();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Đã xóa lớp và các lịch học liên quan!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa: " + ex.getMessage());
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Clazz> list = clazzService.getAllClasses();
        // Sử dụng Stream API để hiển thị dữ liệu liên kết
        list.forEach(c -> tableModel.addRow(new Object[]{
                c.getClassId(),
                c.getClassName(),
                c.getCourse().getCourseName(),
                c.getTeacher().getFullName(),
                c.getMaxStudent(),
                c.getStartDate()
        }));
    }

    private void clearFields() {
        txtId.setText(""); txtName.setText(""); txtMaxStudent.setText("");
        txtStartDate.setText(""); txtEndDate.setText("");
        cbCourse.setSelectedIndex(0); cbTeacher.setSelectedIndex(0);
    }

    public JPanel getMainPanel() { return mainPanel; }
}