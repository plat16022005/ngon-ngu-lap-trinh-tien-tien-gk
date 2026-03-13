package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.model.Schedule;
import com.khoithinhvuong.dev.service.ScheduleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TeacherScheduleForm extends JPanel {
    // 1. Khai báo Service để lấy dữ liệu
    private final ScheduleService scheduleService = new ScheduleService();
    private List<Schedule> fullScheduleList;

    // 2. Các thành phần kết nối từ GUI Designer
    private JPanel mainPanel;
    private JTable table1;
    private JComboBox cbClassFilter;
    private JButton btnFilter;
    private JButton btnShowAll;
    private DefaultTableModel tableModel;

    // 3. Constructor nhận vào ID của giáo viên đang đăng nhập
    public TeacherScheduleForm(Long teacherId) {
        add(mainPanel); // Gắn panel từ file .form vào
        initTable();
        initEvents();
        loadMySchedule(teacherId);
    }

    private void initTable() {
        // Định nghĩa các cột mà giáo viên quan tâm
        String[] columns = {"Ngày học", "Giờ bắt đầu", "Giờ kết thúc", "Lớp", "Phòng học"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Chặn không cho giáo viên sửa trực tiếp trên bảng
            }
        };
        table1.setModel(tableModel);
    }

    // 4.lấy dữ liệu và hiển thị
    public void loadMySchedule(Long teacherId) {
        // 1. Lấy dữ liệu từ Service
        fullScheduleList = scheduleService.getTeacherSchedule(teacherId);

        // 2. Đổ dữ liệu vào bảng thông qua hàm dùng chung
        updateTable(fullScheduleList);

        // 3. Tự động đổ danh sách lớp vào Combo Box để lọc (Dùng Stream API)
        cbClassFilter.removeAllItems();
        fullScheduleList.stream()
                .map(Schedule::getClassEntity) // Lấy đối tượng Clazz
                .distinct() // Lấy các lớp không trùng nhau
                .forEach(cbClassFilter::addItem);
    }

    private void initEvents() {
        // Nút Lọc
        btnFilter.addActionListener(e -> {
            Clazz selectedClass = (Clazz) cbClassFilter.getSelectedItem();
            if (selectedClass != null && fullScheduleList != null) {
                List<Schedule> filteredList = fullScheduleList.stream()
                        .filter(s -> s.getClassEntity().getClassId().equals(selectedClass.getClassId()))
                        .toList();
                updateTable(filteredList);
            }
        });

        // Nút xem tất cả
        btnShowAll.addActionListener(e -> updateTable(fullScheduleList));
    }

    private void updateTable(List<Schedule> list) {
        tableModel.setRowCount(0); // Xóa trắng bảng hiện tại

        // Đổ dữ liệu mới vào bảng
        list.forEach(s -> tableModel.addRow(new Object[]{
                s.getDate(),
                s.getStartTime(),
                s.getEndTime(),
                s.getClassEntity().getClassName(),
                s.getRoom().getRoomName()
        }));
    }


}