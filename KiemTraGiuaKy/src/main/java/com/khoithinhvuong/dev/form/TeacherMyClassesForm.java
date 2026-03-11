package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.service.ClazzService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TeacherMyClassesForm extends JPanel {
    private final ClazzService clazzService = new ClazzService();
    private final Long currentTeacherId; // Lưu ID để dùng cho nút Refresh

    private JPanel panel1;
    private JTable tableClasses;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;

    public TeacherMyClassesForm(Long teacherId) {
        this.currentTeacherId = teacherId;
        add(panel1);
        initTable();
        initEvents();
        loadData(teacherId);
    }

    private void initTable() {
        String[] columns = {"ID", "Tên Lớp", "Khóa Học", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Giáo viên không được sửa thông tin lớp
            }
        };
        tableClasses.setModel(tableModel);
    }

    private void initEvents() {
        btnRefresh.addActionListener(e -> loadData(currentTeacherId));
    }

    private void loadData(Long teacherId) {
        tableModel.setRowCount(0);
        // Gọi hàm lọc phức tạp em đã viết: Lấy danh sách lớp của 1 giáo viên
        List<Clazz> list = clazzService.getClassesByTeacher(teacherId);

        // Trình diễn Lambda để đổ dữ liệu
        list.forEach(c -> tableModel.addRow(new Object[]{
                c.getClassId(),
                c.getClassName(),
                c.getCourse().getCourseName(), // Truy xuất tên khóa học từ liên kết
                c.getStartDate(),
                c.getEndDate(),
                c.getStatus()
        }));
    }
}