package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.service.TeacherService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class AdminTeacherForm extends JPanel {
    private final TeacherService teacherService = new TeacherService();
    private Long currentSelectedId = null; //biến lưu ngầm ID

    private JPanel mainPanel;
    private JTextField txtUsername, txtFullName, txtPhone, txtEmail, txtHireDate;
    private JTable tableTeacher;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JComboBox<String> cbSpecialty1, cbSpecialty2, cbStatus; // Cập nhật đúng kiểu dữ liệu
    private JPasswordField txtPassword;

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
        // 1. Thêm mới
        btnAdd.addActionListener(e -> {
            try {
                //Tạo đối tượng Teacher tạm thời ( chưa có id )
                Teacher newTeacher = new Teacher(null,
                        txtFullName.getText(), txtPhone.getText(), txtEmail.getText(),
                        cbSpecialty1.getSelectedItem().toString(),
                        LocalDate.parse(txtHireDate.getText()), "Active");
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                teacherService.createTeacherWithAccount(newTeacher, username, password);
                refreshTable();
                clearFields();
                JOptionPane.showMessageDialog(this, "Thêm GV và tạo tài khoản thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        // 2. Chọn dòng
        tableTeacher.getSelectionModel().addListSelectionListener(e -> {
            int row = tableTeacher.getSelectedRow();
            if (row != -1) {
                // Lưu id vào biến private để dùng cho update và delete
                currentSelectedId = (Long) tableModel.getValueAt(row, 0);

                txtUsername.setText("");
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
                if (currentSelectedId == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn giáo viên từ bảng!");
                    return;
                }

                Teacher t = teacherService.getTeacherById(currentSelectedId);
                t.setFullName(txtFullName.getText());
                t.setPhone(txtPhone.getText());
                t.setEmail(txtEmail.getText());
                t.setSpecialty(cbSpecialty1.getSelectedItem().toString());
                t.setHireDate(LocalDate.parse(txtHireDate.getText()));
                t.setStatus(cbStatus.getSelectedItem().toString()); // Cập nhật trạng thái

                // Lấy thông tin tài khoản mới (nếu có)
                String newUsername = txtUsername.getText().trim();
                String newPassword = new String(txtPassword.getPassword()).trim();

                teacherService.updateTeacherWithAccount(t, newUsername, newPassword);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage()); }
        });

        // 4. Tìm kiếm (Lọc)
        btnSearch.addActionListener(e -> {
            String specialty = cbSpecialty2.getSelectedItem().toString();
            List<Teacher> filtered = teacherService.searchBySpecialty(specialty);
            updateTable(filtered);
        });

        // 5. Xóa
        btnDelete.addActionListener(e -> {
            if (currentSelectedId == null) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa giáo viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                teacherService.deleteTeacher(currentSelectedId);                refreshTable();
                clearFields();
            }
        });

        //6. Làm mới
        btnClear.addActionListener(e -> {
            clearFields();
            refreshTable(); // Reset lại toàn bộ bảng khi làm mới
        });

        //7. Nháy đúp chuột
        tableTeacher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableTeacher.getSelectedRow();
                    String detail = String.format(
                            "Chi tiết giáo viên\n" +
                                    "--------------------------\n" +
                                    "ID: %s\n" +
                                    "Họ tên: %s\n" +
                                    "SĐT: %s\n" +
                                    "Email: %s\n" +
                                    "Chuyên môn: %s\n" +
                                    "Trạng thái: %s",
                            tableModel.getValueAt(row, 0),
                            tableModel.getValueAt(row, 1),
                            tableModel.getValueAt(row, 2),
                            tableModel.getValueAt(row, 3),
                            tableModel.getValueAt(row, 4),
                            tableModel.getValueAt(row, 6)
                    );
                    JOptionPane.showMessageDialog(null, detail, "Chi tiết giáo viên", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private void clearFields() {
        currentSelectedId = null; //Reset id ngầm
        txtUsername.setText("");
        txtFullName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtHireDate.setText("");
        txtPassword.setText("");
        cbSpecialty1.setSelectedIndex(0);
        cbSpecialty2.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        tableTeacher.clearSelection();
    }
}