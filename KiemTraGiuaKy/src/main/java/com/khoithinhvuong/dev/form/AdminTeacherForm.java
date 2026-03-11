package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.service.TeacherService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class AdminTeacherForm extends JPanel {
    private final TeacherService teacherService = new TeacherService();
    private JPanel mainPanel;
    private JTextField txtId, txtFullName, txtPhone, txtEmail, txtHireDate;
    private JTable tableTeacher;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JComboBox<String> cbSpecialty1, cbSpecialty2, cbStatus; // Cập nhật đúng kiểu dữ liệu

    public AdminTeacherForm() {
        add(mainPanel);
        initTable();
        initEvents();
        refreshTable();
    }

    private void initTable() {
        // Thêm cột "Trạng thái" theo đúng đề bài BA
        String[] columns = {"ID", "Họ Tên", "Số ĐT", "Email", "Chuyên Môn", "Ngày Vào", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        tableTeacher.setModel(tableModel);
    }

    private void updateTable(List<Teacher> list) {
        tableModel.setRowCount(0);
        list.forEach(t -> tableModel.addRow(new Object[]{
                t.getTeacherId(), t.getFullName(), t.getPhone(),
                t.getEmail(), t.getSpecialty(), t.getHireDate(), t.getStatus() // Thêm Status vào bảng
        }));
    }

    private void refreshTable() {
        updateTable(teacherService.getAllTeachers());
    }

    private void initEvents() {
        // 1. Thêm mới - Sử dụng dữ liệu từ ComboBox
        btnAdd.addActionListener(e -> {
            try {
                // Lưu ý: createTeacher ở Service cần cập nhật để nhận thêm Status nếu cần
                teacherService.createTeacher(
                        txtFullName.getText(), txtPhone.getText(), txtEmail.getText(),
                        cbSpecialty1.getSelectedItem().toString(), // Lấy từ cbSpecialty1
                        LocalDate.parse(txtHireDate.getText())
                );
                refreshTable();
                clearFields();
                JOptionPane.showMessageDialog(this, "Thêm giáo viên thành công!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });

        // 2. Chọn dòng - Đổ ngược dữ liệu lên ComboBox
        tableTeacher.getSelectionModel().addListSelectionListener(e -> {
            int row = tableTeacher.getSelectedRow();
            if (row != -1) {
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtFullName.setText(tableModel.getValueAt(row, 1).toString());
                txtPhone.setText(tableModel.getValueAt(row, 2).toString());
                txtEmail.setText(tableModel.getValueAt(row, 3).toString());
                cbSpecialty1.setSelectedItem(tableModel.getValueAt(row, 4).toString()); // Đổ vào cb1
                txtHireDate.setText(tableModel.getValueAt(row, 5).toString());
                cbStatus.setSelectedItem(tableModel.getValueAt(row, 6).toString()); // Đổ vào cbStatus
            }
        });

        // 3. Cập nhật
        btnUpdate.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty()) return;
                Teacher t = teacherService.getTeacherById(Long.parseLong(txtId.getText()));
                t.setFullName(txtFullName.getText());
                t.setPhone(txtPhone.getText());
                t.setEmail(txtEmail.getText());
                t.setSpecialty(cbSpecialty1.getSelectedItem().toString()); // Cập nhật từ cb1
                t.setHireDate(LocalDate.parse(txtHireDate.getText()));
                t.setStatus(cbStatus.getSelectedItem().toString()); // Cập nhật trạng thái

                teacherService.updateTeacher(t);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage()); }
        });

        // 4. Tìm kiếm (Lọc) - Sử dụng cbSpecialty2
        btnSearch.addActionListener(e -> {
            String specialty = cbSpecialty2.getSelectedItem().toString();
            List<Teacher> filtered = teacherService.searchBySpecialty(specialty);
            updateTable(filtered);
        });

        // 5. Xóa
        btnDelete.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa giáo viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                teacherService.deleteTeacher(Long.parseLong(txtId.getText()));
                refreshTable();
                clearFields();
            }
        });

        btnClear.addActionListener(e -> {
            clearFields();
            refreshTable(); // Reset lại toàn bộ bảng khi làm mới
        });
    }

    private void clearFields() {
        txtId.setText(""); txtFullName.setText(""); txtPhone.setText("");
        txtEmail.setText(""); txtHireDate.setText("");
        cbSpecialty1.setSelectedIndex(0);
        cbSpecialty2.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        tableTeacher.clearSelection();
    }
}