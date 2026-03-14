package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Schedule;
import com.khoithinhvuong.dev.service.ScheduleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.time.LocalDate;
import java.util.List;

public class StudentScheduleForm {

    private JPanel mainPanel;
    private JTable table1;
    private JButton button1;
    private JFormattedTextField formattedTextField1;

    private final ScheduleService scheduleService = new ScheduleService();
    private final Long studentId;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public StudentScheduleForm(Long studentId) {

        this.studentId = studentId;

        setupDateField();

        button1.addActionListener(e -> loadSchedule());
    }

    private void setupDateField() {

        try {
            MaskFormatter mask = new MaskFormatter("####-##-##");
            mask.setPlaceholderCharacter('_');
            mask.install(formattedTextField1);

            formattedTextField1.setText(LocalDate.now().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSchedule() {

        String dateText = formattedTextField1.getText();

        if (dateText.contains("_")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đủ ngày");
            return;
        }

        LocalDate date = LocalDate.parse(dateText);

        List<Schedule> schedules =
                scheduleService.getStudentSchedule(studentId, date);

        String[] column = {
                "Class",
                "Room",
                "Start Time",
                "End Time"
        };

        DefaultTableModel model = new DefaultTableModel(column, 0);

        for (Schedule s : schedules) {

            model.addRow(new Object[]{
                    s.getClassEntity().getClassName(),
                    s.getRoom().getRoomName(),
                    s.getStartTime(),
                    s.getEndTime()
            });
        }

        table1.setModel(model);
    }
}