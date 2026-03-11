package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Room;
import com.khoithinhvuong.dev.service.RoomService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class RoomForm extends JPanel {
    private final RoomService roomService = new RoomService();

    private JPanel mainPanel;
    private JTextField txtId, txtName, txtCapacity, txtLocation;
    private JComboBox<String> cbStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable tableRoom;
    private DefaultTableModel tableModel;

    public RoomForm() {
        add(mainPanel);
        initTable();
        initEvents();
        refreshTable();
    }

    private void initTable() {
        String[] columns = {"ID", "Tên Phòng", "Sức Chứa", "Vị Trí", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        tableRoom.setModel(tableModel);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Room> list = roomService.getAllRooms(); // Lấy dữ liệu từ DB

        // Đổ dữ liệu
        list.forEach(r -> tableModel.addRow(new Object[]{
                r.getRoomId(), r.getRoomName(), r.getCapacity(), r.getLocation(), r.getStatus()
        }));
    }

    private void initEvents() {
        // 1. thêm mới
        btnAdd.addActionListener(e -> {
            try {
                roomService.createRoom(
                        txtName.getText(),
                        Integer.parseInt(txtCapacity.getText()),
                        txtLocation.getText()
                );
                refreshTable();
                clearFields();
                JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        // 2. Click vào bảng
        tableRoom.getSelectionModel().addListSelectionListener(e -> {
            int row = tableRoom.getSelectedRow();
            if (row != -1) {
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtCapacity.setText(tableModel.getValueAt(row, 2).toString());
                txtLocation.setText(tableModel.getValueAt(row, 3).toString());
                cbStatus.setSelectedItem(tableModel.getValueAt(row, 4).toString());
            }
        });

        // 3. Cập nhật
        btnUpdate.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty()) return;
                Room r = roomService.getRoomById(Long.parseLong(txtId.getText()));
                r.setRoomName(txtName.getText());
                r.setCapacity(Integer.parseInt(txtCapacity.getText()));
                r.setLocation(txtLocation.getText());
                r.setStatus((String) cbStatus.getSelectedItem());

                roomService.updateRoom(r);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        // 4. xoá
        btnDelete.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Xoá phòng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                roomService.deleteRoom(Long.parseLong(txtId.getText()));
                refreshTable();
                clearFields();
            }
        });

        btnClear.addActionListener(e -> clearFields());
    }

    private void clearFields() {
        txtId.setText(""); txtName.setText(""); txtCapacity.setText("");
        txtLocation.setText(""); cbStatus.setSelectedIndex(0);
        tableRoom.clearSelection();
    }

    public JPanel getMainPanel() { return mainPanel; }
}