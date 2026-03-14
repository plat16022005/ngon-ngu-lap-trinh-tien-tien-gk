package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.service.ClazzService;
import com.khoithinhvuong.dev.service.ResultsService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import java.util.List;

public class TeacherResultsForm {

    private JPanel mainPanel;
    private JTable table1;
    private JComboBox<Clazz> comboBox1;
    private JButton xemButton;
    private JButton cậpNhậtButton;

    private final ClazzService clazzService = new ClazzService();
    private final ResultsService resultsService = new ResultsService();

    private final Long teacherId;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public TeacherResultsForm(Long teacherId) {

        this.teacherId = teacherId;

        loadTeacherClasses();

        xemButton.addActionListener(e -> loadStudents());

        cậpNhậtButton.addActionListener(e -> saveResults());
    }

    // load lớp của teacher
    private void loadTeacherClasses() {

        List<Clazz> classes = clazzService.getClassesByTeacher(teacherId);

        DefaultComboBoxModel<Clazz> model = new DefaultComboBoxModel<>();

        for (Clazz c : classes) {
            model.addElement(c);
        }

        comboBox1.setModel(model);
    }

    // load sinh viên của lớp
    private void loadStudents() {

        Clazz clazz = (Clazz) comboBox1.getSelectedItem();

        if (clazz == null) return;

        Long classId = clazz.getClassId();

        List<Object[]> students = resultsService.getStudentsWithResults(classId);

        String[] column = {
                "StudentId",
                "Student Name",
                "Score",
                "Grade",
                "Comment"
        };

        DefaultTableModel model = new DefaultTableModel(column,0);

        for(Object[] row : students){

            model.addRow(new Object[]{
                    row[0],
                    row[1],
                    row[2],
                    row[3],
                    row[4]
            });
        }

        table1.setModel(model);

        // auto convert grade
        model.addTableModelListener(e -> {

            if(e.getColumn() == 2){

                int row = e.getFirstRow();

                Object value = model.getValueAt(row,2);

                if(value == null) return;

                double score = Double.parseDouble(value.toString());

                String grade = convertGrade(score);

                model.setValueAt(grade,row,3);
            }

        });

    }

    // convert score -> grade
    private String convertGrade(double score){

        if(score >= 9) return "A";
        if(score >= 8) return "B";
        if(score >= 7) return "C";
        if(score >= 6) return "D";
        return "F";
    }

    // save results
    private void saveResults(){

        Clazz clazz = (Clazz) comboBox1.getSelectedItem();

        Long classId = clazz.getClassId();

        DefaultTableModel model = (DefaultTableModel) table1.getModel();

        for(int i=0;i<model.getRowCount();i++){

            Long studentId = Long.parseLong(model.getValueAt(i,0).toString());

            Object scoreObj = model.getValueAt(i,2);

            if(scoreObj == null) continue;

            double score = Double.parseDouble(scoreObj.toString());

            String grade = model.getValueAt(i,3).toString();

            String comment = model.getValueAt(i,4) == null ? "" : model.getValueAt(i,4).toString();

            resultsService.saveResult(studentId,classId,score,grade,comment);

        }

        JOptionPane.showMessageDialog(null,"Saved results!");

    }
}