package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.service.AttendanceService;
import com.khoithinhvuong.dev.service.ClazzService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.MaskFormatter;
import java.time.LocalDate;
import java.util.List;

public class TeacherAttendanceForm {

    private JPanel mainPanel;
    private JComboBox<Clazz> lopBox;
    private JFormattedTextField ngayBox;
    private JTable table1;
    private JButton xemButton;
    private JButton xácNhậnButton;
//    private JComboBox<String> comboBox1;

    private final ClazzService clazzService = new ClazzService();
    private final AttendanceService attendanceService = new AttendanceService();

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public TeacherAttendanceForm(Long teacherId) {

        loadClasses(teacherId);
        setupInputMasks();
//        setupStatusComboBox();

        xemButton.addActionListener(e -> loadAttendance());
        xácNhậnButton.addActionListener(e -> saveAttendance());
    }

    private void setupInputMasks() {
        try {
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(ngayBox);

            ngayBox.setText(LocalDate.now().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void setupStatusComboBox() {
//
//        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
//        model.addElement("PRESENT");
//        model.addElement("ABSENT");
//        model.addElement("LATE");
//
//        comboBox1.setModel(model);
//    }

    private void loadClasses(Long teacherId) {

        List<Clazz> classes = clazzService.getClassesByTeacher(teacherId);

        DefaultComboBoxModel<Clazz> model = new DefaultComboBoxModel<>();

        for (Clazz clazz : classes) {
            model.addElement(clazz);
        }

        lopBox.setModel(model);
    }

    private void loadAttendance() {

        Clazz clazz = (Clazz) lopBox.getSelectedItem();

        if (clazz == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn lớp");
            return;
        }

        String dateText = ngayBox.getText();

        if (dateText.contains("_")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đủ ngày");
            return;
        }

        LocalDate date = LocalDate.parse(dateText);

        Long classId = clazz.getClassId();

        List<Object[]> data = attendanceService.getStudentAttendance(classId, date);

        String[] column = {"StudentId", "Student Name", "Status"};

        DefaultTableModel model = new DefaultTableModel(column, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        for (Object[] row : data) {

            String status = row[2] == null ? "" : row[2].toString();

            model.addRow(new Object[]{
                    row[0],
                    row[1],
                    status
            });
        }

        table1.setModel(model);

        TableColumn statusColumn = table1.getColumnModel().getColumn(2);

        JComboBox<String> statusBox = new JComboBox<>();
        statusBox.addItem("PRESENT");
        statusBox.addItem("ABSENT");
        statusBox.addItem("LATE");

        statusColumn.setCellEditor(new DefaultCellEditor(statusBox));
    }

    private void saveAttendance() {

        Clazz clazz = (Clazz) lopBox.getSelectedItem();
        Long classId = clazz.getClassId();

        LocalDate date = LocalDate.parse(ngayBox.getText());

        DefaultTableModel model = (DefaultTableModel) table1.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {

            Long studentId = Long.parseLong(model.getValueAt(i, 0).toString());

            String status = model.getValueAt(i, 2).toString();
            System.out.println(status);

            attendanceService.saveAttendance(studentId, classId, date, status);
        }

        JOptionPane.showMessageDialog(null, "Attendance saved!");
    }
}