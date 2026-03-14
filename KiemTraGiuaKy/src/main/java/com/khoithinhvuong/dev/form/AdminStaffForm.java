package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.config.PasswordUtils;
import com.khoithinhvuong.dev.model.Staff;
import com.khoithinhvuong.dev.service.StaffService;
import com.khoithinhvuong.dev.service.UserAccountService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AdminStaffForm {

    private JTable table1;
    private JTextField textField1; // fullname
    private JTextField textField2; // phone
    private JTextField textField3; // email
    private JComboBox comboBox1;   // role staff
    private JTextField textField4; // username
    private JTextField textField5; // password
    private JButton tạoButton;
    private JButton cậpNhậtButton;
    private JPanel mainPanel;

    private StaffService staffService = new StaffService();
    private UserAccountService userAccountService = new UserAccountService();

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public AdminStaffForm() {

        comboBox1.setModel(new DefaultComboBoxModel<>(new String[]{
                "Manager",
                "HR"
        }));
        table1.getSelectionModel().addListSelectionListener(e -> {

            int row = table1.getSelectedRow();

            if (row >= 0) {

                textField1.setText(table1.getValueAt(row, 1).toString());
                comboBox1.setSelectedItem(table1.getValueAt(row, 2).toString());
                textField2.setText(table1.getValueAt(row, 3).toString());
                textField3.setText(table1.getValueAt(row, 4).toString());

            }

        });
        tạoButton.addActionListener(e -> createStaff());
        cậpNhậtButton.addActionListener(e -> updateStaff());
    }

    private void createStaff() {

        try {

            String fullName = textField1.getText();
            String email = textField2.getText();
            String phone = textField3.getText();
            String role = comboBox1.getSelectedItem().toString();

            String username = textField4.getText();
            String password = textField5.getText();

            // 1️⃣ tạo staff
            Staff staff = new Staff(
                    null,
                    fullName,
                    role,
                    phone,
                    email
            );

            staffService.createStaff(staff);

            Long staffId = staff.getStaffId();

            // 2️⃣ tạo account admin
            userAccountService.createAdminAccount(
                    username,
                    PasswordUtils.hashPassword(password),
                    staffId
            );

            JOptionPane.showMessageDialog(mainPanel, "Tạo Staff thành công!");

            clearForm();
            loadStaffTable();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(mainPanel, "Lỗi: " + ex.getMessage());
        }
    }
    private void updateStaff() {

        int selectedRow = table1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Vui lòng chọn staff cần cập nhật!");
            return;
        }

        try {

            Long staffId = (Long) table1.getValueAt(selectedRow, 0);

            String fullName = textField1.getText();
            String role = comboBox1.getSelectedItem().toString();
            String phone = textField2.getText();
            String email = textField3.getText();

            Staff staff = new Staff(
                    staffId,
                    fullName,
                    role,
                    phone,
                    email
            );

            staffService.updateStaff(staff);

            JOptionPane.showMessageDialog(mainPanel, "Cập nhật thành công!");

            loadStaffTable();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(mainPanel, "Lỗi: " + ex.getMessage());

        }
    }
    private void loadStaffTable() {

        String[] column = {"ID", "Name", "Role", "Phone", "Email"};
        DefaultTableModel model = new DefaultTableModel(column, 0);

        List<Staff> list = staffService.getAllStaffExceptAdmin();

        for (Staff s : list) {

            model.addRow(new Object[]{
                    s.getStaffId(),
                    s.getFullName(),
                    s.getRole(),
                    s.getPhone(),
                    s.getEmail()
            });

        }

        table1.setModel(model);
    }
    private void clearForm() {

        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
    }
    public void refreshTable() {
        loadStaffTable();
    }
}