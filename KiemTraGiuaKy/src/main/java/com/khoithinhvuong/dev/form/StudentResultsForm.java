package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.service.ClazzService;
import com.khoithinhvuong.dev.service.ResultsService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StudentResultsForm {

    private JPanel mainPanel;
    private JTable table1;
    private JComboBox<Object> comboBox1;
    private JButton xemButton;

    private final Long studentId;

    private final ClazzService clazzService = new ClazzService();
    private final ResultsService resultsService = new ResultsService();

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public StudentResultsForm(Long studentId){

        this.studentId = studentId;

        loadStudentClasses();

        // khi mở form
        loadAllResults();

        xemButton.addActionListener(e -> filterResults());
    }

    // load comboBox lớp
    private void loadStudentClasses(){

        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();

        model.addElement("All Classes");

        List<Clazz> classes = clazzService.getClassesByStudent(studentId);

        for(Clazz c : classes){
            model.addElement(c);
        }

        comboBox1.setModel(model);
    }

    // load tất cả kết quả
    private void loadAllResults(){

        List<Object[]> results = resultsService.getResultsByStudent(studentId);

        fillTable(results);
    }

    // lọc theo lớp
    private void filterResults(){

        Object selected = comboBox1.getSelectedItem();

        if(selected instanceof String){

            loadAllResults();
            return;
        }

        Clazz clazz = (Clazz) selected;

        List<Object[]> results =
                resultsService.getResultsByStudentAndClass(studentId, clazz.getClassId());

        fillTable(results);
    }

    // rót dữ liệu vào JTable
    private void fillTable(List<Object[]> data){

        String[] column = {
                "Class",
                "Score",
                "Grade",
                "Comment"
        };

        DefaultTableModel model = new DefaultTableModel(column,0);

        for(Object[] row : data){

            model.addRow(new Object[]{
                    row[0],
                    row[1],
                    row[2],
                    row[3]
            });
        }

        table1.setModel(model);
    }
}