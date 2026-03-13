package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.model.Room;
import com.khoithinhvuong.dev.model.Schedule;
import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.service.ClazzService;
import com.khoithinhvuong.dev.service.RoomService;
import com.khoithinhvuong.dev.service.ScheduleService;
import com.khoithinhvuong.dev.service.TeacherService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AdminScheduleForm extends JPanel {
    private final ScheduleService scheduleService = new ScheduleService();
    private final ClazzService clazzService = new ClazzService();
    private final RoomService roomService = new RoomService();
    TeacherService teacherService = new TeacherService();


    private JPanel mainPanel;
    private JComboBox<Clazz> cbClazz;
    private JComboBox<Room> cbRoom;
    private JTextField  txtNote;
    private JTable table1;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTextField txtId;
    private JButton btnFilter;
    private JComboBox cbTeacherFilter;
    private JFormattedTextField txtDate, txtStartTime, txtEndTime;

    public AdminScheduleForm() {
        setLayout(new BorderLayout());

        if (mainPanel != null) {
            add(mainPanel, BorderLayout.CENTER);
        }

        initTable();
        initData();
        setupInputMasks();
        initEvents();
        refreshTable();
    }

    private void setupInputMasks()
    {
        try {
            // 1. Nhập ngày học
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(txtDate);

            // 2. Nhập giờ học và kết thuúc
            MaskFormatter timeMask = new MaskFormatter("##:##");
            timeMask.setPlaceholderCharacter('_');

            timeMask.install(txtStartTime);
            timeMask.install(txtEndTime);

            // 3. gợi ý giá trị
            txtDate.setText(LocalDate.now().toString());
            txtStartTime.setText("08:00");
            txtEndTime.setText("10:00");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTable() {
        String[] columns = {"ID", "Lớp Học", "Phòng", "Ngày", "Bắt đầu", "Kết thúc"};
        tableModel = new DefaultTableModel(columns, 0);
        table1.setModel(tableModel);
    }

    public void initData() {
        //1.Dọn sạch trước khi nạp
        cbClazz.removeAllItems();
        cbRoom.removeAllItems();
        cbTeacherFilter.removeAllItems();
        // Load danh sách Lớp và Phòng vào Combobox
        clazzService.getAllClasses().forEach(cbClazz::addItem);
        roomService.getAllRooms().forEach(cbRoom::addItem);
        teacherService.getAllTeachers().forEach(cbTeacherFilter::addItem);
    }

    private void initEvents() {
        btnAdd.addActionListener(e -> {
            try {
                Schedule s = new Schedule();
                s.setClassEntity((Clazz) cbClazz.getSelectedItem());
                s.setRoom((Room) cbRoom.getSelectedItem());
                s.setDate(LocalDate.parse(txtDate.getText()));
                s.setStartTime(LocalTime.parse(txtStartTime.getText()));
                s.setEndTime(LocalTime.parse(txtEndTime.getText()));

                // Gọi service - nơi đã có logic check trùng bằng Stream API
                scheduleService.createSchedule(s);

                refreshTable();
                JOptionPane.showMessageDialog(this, "Xếp lịch thành công!");
            } catch (Exception ex) {
                // Hiển thị lỗi trùng lịch hoặc sai định dạng ngày giờ
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        //click chọn
        table1.getSelectionModel().addListSelectionListener(e -> {
            int row = table1.getSelectedRow();
            if (row != -1) {
                // Lấy ID từ cột đầu tiên của bảng
                Long id = (Long) tableModel.getValueAt(row, 0);
                // Tìm đối tượng Schedule từ Service
                Schedule s = scheduleService.getScheduleById(id);

                if (s != null) {
                    txtId.setText(s.getScheduleID().toString());
                    txtDate.setText(s.getDate().toString());
                    txtStartTime.setText(s.getStartTime().toString());
                    txtEndTime.setText(s.getEndTime().toString());
                    cbClazz.setSelectedItem(s.getClassEntity());
                    cbRoom.setSelectedItem(s.getRoom());
                }
            }
        });

        //Nút làm mới
        btnClear.addActionListener(e -> {
            clearFields();
            refreshTable(); // Hiển thị lại toàn bộ danh sách lịch học
        });

        // Nút Cập nhật
        btnUpdate.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty()) throw new Exception("Vui lòng chọn lịch cần sửa!");

                Schedule s = scheduleService.getScheduleById(Long.parseLong(txtId.getText()));
                s.setDate(LocalDate.parse(txtDate.getText()));
                s.setStartTime(LocalTime.parse(txtStartTime.getText()));
                s.setEndTime(LocalTime.parse(txtEndTime.getText()));
                s.setRoom((Room) cbRoom.getSelectedItem());
                s.setClassEntity((Clazz) cbClazz.getSelectedItem());

                scheduleService.updateSchedule(s);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật lịch thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
            }
        });

        // Nút Xoá
        btnDelete.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty()) return;
                int confirm = JOptionPane.showConfirmDialog(this, "Xoá lịch học này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    scheduleService.deleteSchedule(Long.parseLong(txtId.getText()));
                    refreshTable();
                    clearFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xoá: " + ex.getMessage());
            }
        });

        // Sự kiện nút lọc
        btnFilter.addActionListener(e -> {
            Teacher selected = (Teacher) cbTeacherFilter.getSelectedItem();
            if (selected != null) {
                tableModel.setRowCount(0);
                // Sử dụng hàm lọc phức tạp em đã viết ở Service
                List<Schedule> list = scheduleService.getTeacherSchedule(selected.getTeacherId());

                // Trình diễn Lambda để đổ dữ liệu đã lọc
                list.forEach(s -> tableModel.addRow(new Object[]{
                        s.getScheduleID(),
                        s.getClassEntity().getClassName(),
                        s.getRoom().getRoomName(),
                        s.getDate(),
                        s.getStartTime(),
                        s.getEndTime()
                }));

                if (list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Giáo viên này hiện chưa có lịch dạy!");
                }
            }
        });
    }

    //Lọc lấy lịch học theo lớp
    public void filterByClass(Long classId) {
        // 1. Lọc dữ liệu bảng
        tableModel.setRowCount(0);
        List<Schedule> list = scheduleService.getSchedulesByClass(classId);
        list.forEach(s -> tableModel.addRow(new Object[]{
                s.getScheduleID(), s.getClassEntity().getClassName(),
                s.getRoom().getRoomName(), s.getDate(), s.getStartTime(), s.getEndTime()
        }));

        // 2. Tự động chọn đúng Lớp trên ComboBox
        if (cbClazz != null) {
            for (int i = 0; i < cbClazz.getItemCount(); i++) {
                Clazz item = cbClazz.getItemAt(i);
                if (item.getClassId().equals(classId)) {
                    cbClazz.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Schedule> list = scheduleService.getAllSchedules();
        list.forEach(s -> tableModel.addRow(new Object[]{
                s.getScheduleID(),
                s.getClassEntity().getClassName(),
                s.getRoom().getRoomName(),
                s.getDate(),
                s.getStartTime(),
                s.getEndTime()
        }));
    }


    private void clearFields() {
        txtId.setText(""); // phải xóa ID để thêm mới
        txtDate.setText("");
        txtStartTime.setText("");
        txtEndTime.setText("");
        txtNote.setText("");

        setupInputMasks();

        if(cbClazz.getItemCount() > 0) cbClazz.setSelectedIndex(0);
        if(cbRoom.getItemCount() > 0) cbRoom.setSelectedIndex(0);
        table1.clearSelection();
        cbTeacherFilter.setSelectedIndex(0);
    }

    public JPanel getMainPanel()
    { return mainPanel; }
}