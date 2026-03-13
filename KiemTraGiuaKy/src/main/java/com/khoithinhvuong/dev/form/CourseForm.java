package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.MainFrame;
import com.khoithinhvuong.dev.model.Course;
import com.khoithinhvuong.dev.service.CourseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseForm extends JPanel {
    private final CourseService courseService = new CourseService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtDuration, txtFee;
    private JTextArea txtDesc;
    private JComboBox<String> cbLevel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JComboBox cbSortOrder;
    private JButton btnSort;
    private JButton btnViewClasses;
    private JPanel mainPanel;

    public CourseForm() {
        setLayout(new BorderLayout());
        if (mainPanel == null) {
            mainPanel = new JPanel();
        }
        add(mainPanel);
        initTableData();
        initData();
        initEvents();
        refreshTable();
        //customizeUI();
    }


    private void initTableData() {
        String[] columns = {"ID", "Tên", "Cấp độ", "Thời lượng", "Học phí"};
        tableModel = new DefaultTableModel(columns, 0);

        if (table != null) {
            table.setModel(tableModel);
        }
    }

    private void initData() {
        // Nạp dữ liệu cho ComboBox sắp xếp
        if (cbSortOrder != null) {
            cbSortOrder.removeAllItems();
            cbSortOrder.addItem("Giá thấp đến cao"); // Index 0 -> Ascending
            cbSortOrder.addItem("Giá cao đến thấp"); // Index 1 -> Descending
        }

        // Nạp dữ liệu cho ComboBox cấp độ
        if (cbLevel != null && cbLevel.getItemCount() == 0) {
            cbLevel.addItem("Beginner");
            cbLevel.addItem("Intermediate");
            cbLevel.addItem("Advanced");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Course> list = courseService.getAllCourses();
        for (Course c : list) {
            tableModel.addRow(new Object[]{
                    c.getCourseId(), c.getCourseName(), c.getLevel(), c.getDuration(), c.getFee()
            });
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtDesc.setText("");
        txtDuration.setText("");
        txtFee.setText("");
        cbLevel.setSelectedIndex(0);
        table.clearSelection();
    }


    private void initEvents() {
        //Thêm
        btnAdd.addActionListener(e -> {
            try {
                courseService.createCourse(txtName.getText(), txtDesc.getText(),
                        (String) cbLevel.getSelectedItem(), Integer.parseInt(txtDuration.getText()),
                        Double.parseDouble(txtFee.getText()));
                refreshTable();
                clearFields();
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        //Cập nhật
        btnUpdate.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty()) throw new Exception("Hãy chọn một khóa học từ bảng!");

                // Lấy dữ liệu và đóng gói vào Object (The Why: Dùng merge trong JPA)
                Course c = courseService.getCourseById(Long.parseLong(txtId.getText()));
                c.setCourseName(txtName.getText());
                c.setLevel((String) cbLevel.getSelectedItem());
                c.setDuration(Integer.parseInt(txtDuration.getText()));
                c.setFee(Double.parseDouble(txtFee.getText()));
                c.setDescription(txtDesc.getText());

                courseService.updateCourse(c);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
            }
        });


        //Xoá
        btnDelete.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập học phí!");
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khóa học này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    courseService.deleteCourse(Long.parseLong(txtId.getText()));
                    refreshTable();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Đã xóa!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa: " + ex.getMessage());
            }
        });

        // Click Table đổ dữ liệu
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long id = (Long) table.getValueAt(row, 0);
                Course c = courseService.getCourseById(id);
                if (c != null) {
                    txtId.setText(c.getCourseId().toString());
                    txtName.setText(c.getCourseName());
                    txtFee.setText(c.getFee().toString());
                    txtDesc.setText(c.getDescription());
                    cbLevel.setSelectedItem(c.getLevel());
                    txtDuration.setText(c.getDuration().toString());
                }
            }
        });

        btnClear.addActionListener(e -> clearFields());

        //Tìm kiếm
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            List<Course> results = courseService.searchCourses(keyword); // Sử dụng hàm search dùng Stream API em đã viết

            tableModel.setRowCount(0);
            results.forEach(c -> tableModel.addRow(new Object[]{
                    c.getCourseId(), c.getCourseName(), c.getLevel(), c.getDuration(), c.getFee()
            }));
        });

        btnSort.addActionListener(e -> {
            // Kiểm tra lựa chọn
            boolean isAscending = cbSortOrder.getSelectedIndex() == 0;

            tableModel.setRowCount(0);

            List<Course> list = courseService.getCoursesSortedByFee(isAscending);

            list.forEach(c -> tableModel.addRow(new Object[]{
                    c.getCourseId(), c.getCourseName(), c.getLevel(), c.getDuration(), c.getFee()
            }));

            String message = isAscending ? "Đã sắp xếp giá thấp nhất lên đầu" : "Đã sắp xếp giá cao nhất lên đầu";
            JOptionPane.showMessageDialog(this, message);
        });

        // Xem lớp cuủa khoá học
        btnViewClasses.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Long courseId = (Long) tableModel.getValueAt(row, 0); // Lấy ID khóa học
                MainFrame.showClassOfCourse(courseId); // Gửi lệnh đi!
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một khóa học!");
            }
        });
    }


    // Làm đẹp

}