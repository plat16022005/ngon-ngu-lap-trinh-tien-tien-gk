package com.khoithinhvuong.dev.form;

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

    public CourseForm() {
        setLayout(new BorderLayout(10, 10));
        initComponents();
        layoutComponents();
        initEvents();
        refreshTable();
        customizeUI();
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

    private void initComponents() {
        txtId = new JTextField(); txtId.setEditable(false);
        txtName = new JTextField(20);
        txtDuration = new JTextField(10);
        txtFee = new JTextField(10);
        txtDesc = new JTextArea(3, 20);
        cbLevel = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});

        // Khởi tạo Table
        String[] columns = {"ID", "Tên", "Cấp độ", "Thời lượng", "Học phí"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Khởi tạo Buttons
        btnAdd = new JButton("Thêm mới");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");
    }

    private void layoutComponents() {
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin Khóa học"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Hàng 0: ID
        gbc.gridx = 0; gbc.gridy = 0; pnlInput.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; pnlInput.add(txtId, gbc);

        // Hàng 1: Tên khóa
        gbc.gridx = 0; gbc.gridy = 1; pnlInput.add(new JLabel("Tên khóa:"), gbc);
        gbc.gridx = 1; pnlInput.add(txtName, gbc);

        // Hàng 2: Cấp độ (JComboBox)
        gbc.gridx = 0; gbc.gridy = 2; pnlInput.add(new JLabel("Cấp độ:"), gbc);
        gbc.gridx = 1; pnlInput.add(cbLevel, gbc);

        // Hàng 3: Thời lượng
        gbc.gridx = 0; gbc.gridy = 3; pnlInput.add(new JLabel("Thời lượng (tuần):"), gbc);
        gbc.gridx = 1; pnlInput.add(txtDuration, gbc);

        // Hàng 4: Học phí
        gbc.gridx = 0; gbc.gridy = 4; pnlInput.add(new JLabel("Học phí:"), gbc);
        gbc.gridx = 1; pnlInput.add(txtFee, gbc);

        // Hàng 5: Mô tả (JTextArea)
        gbc.gridx = 0; gbc.gridy = 5; pnlInput.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; pnlInput.add(new JScrollPane(txtDesc), gbc);

        // Button Panel (Giữ nguyên phần của em)
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButtons.add(btnAdd); pnlButtons.add(btnUpdate);
        pnlButtons.add(btnDelete); pnlButtons.add(btnClear);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pnlInput, BorderLayout.WEST);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    private void initEvents() {
        //Thêm
        btnAdd.addActionListener(e -> {
            try {
                courseService.createCourse(txtName.getText(), txtDesc.getText(),
                        (String)cbLevel.getSelectedItem(), Integer.parseInt(txtDuration.getText()),
                        Double.parseDouble(txtFee.getText()));
                refreshTable(); clearFields();
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty()) throw new Exception("Hãy chọn một khóa học từ bảng!");

                // Lấy dữ liệu và đóng gói vào Object (The Why: Dùng merge trong JPA)
                Course c = courseService.getCourseById(Long.parseLong(txtId.getText()));
                c.setCourseName(txtName.getText());
                c.setLevel((String)cbLevel.getSelectedItem());
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

        btnDelete.addActionListener(e -> {
            try {
                if (txtId.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập học phí!");
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khóa học này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    courseService.deleteCourse(Long.parseLong(txtId.getText()));
                    refreshTable(); clearFields();
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

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            List<Course> results = courseService.searchCourses(keyword); // Sử dụng hàm search dùng Stream API em đã viết

            tableModel.setRowCount(0);
            results.forEach(c -> tableModel.addRow(new Object[]{
                    c.getCourseId(), c.getCourseName(), c.getLevel(), c.getDuration(), c.getFee()
            }));
        });
    }



    // Làm đẹp
    private void customizeUI() {

        table.setRowHeight(30);
        table.setSelectionBackground(new Color(51, 153, 255));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        // Tùy chỉnh Buttons (Ví dụ nút Thêm)
        styleButton(btnAdd, new Color(40, 167, 69));
        styleButton(btnUpdate, new Color(0, 123, 255));
        styleButton(btnDelete, new Color(220, 53, 69));
        styleButton(btnClear, Color.GRAY);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}