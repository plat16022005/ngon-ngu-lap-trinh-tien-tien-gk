package com.khoithinhvuong.dev.form;

import com.khoithinhvuong.dev.service.StaffService;
import com.khoithinhvuong.dev.model.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class FormAdmin {
    private static final Logger log = LoggerFactory.getLogger(FormAdmin.class);
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JButton teacherButton;
    private JButton courseButton;
    private JButton classesButton;
    private JButton roomButton;
    private JButton enrollmentButton;
    private JPanel contentPanel;
    private JButton logoutButton;
    private JButton staffButton;
    private JButton scheduleButton;

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private CardLayout cardLayout = new CardLayout();
    private CourseForm courseForm = new CourseForm();
    private AdminClazzForm clazzForm = new AdminClazzForm();
    private AdminScheduleForm scheduleForm = new AdminScheduleForm();
    private AdminTeacherForm teacherForm = new AdminTeacherForm();
    private RoomForm roomForm = new RoomForm();
    private AdminEnrollmentForm enrollmentForm = new AdminEnrollmentForm();
    private AdminStaffForm staffForm = new AdminStaffForm();
    private StaffService staffService = new StaffService();
    public FormAdmin(Long id) {
        contentPanel.setLayout(cardLayout);


        Staff staff = staffService.getStaffById(id);
        // Sự kiện Menu bên trái
        if (staff.getRole().equals("HR") || staff.getRole().equals("Admin"))
        {
            contentPanel.add(teacherForm, "TEACHER");
            teacherButton.addActionListener(e -> showForm("TEACHER"));
        }
        else
        {
            teacherButton.setVisible(false);
        }
        if (staff.getRole().equals("Manager") || staff.getRole().equals("Admin"))
        {
            contentPanel.add(courseForm, "COURSE");
            contentPanel.add(clazzForm, "CLAZZ");
            contentPanel.add(scheduleForm, "SCHEDULE");

            contentPanel.add(roomForm, "ROOM");
            courseButton.addActionListener(e -> showForm("COURSE"));

            classesButton.addActionListener(e -> showForm("CLAZZ"));
            roomButton.addActionListener(e -> showForm("ROOM"));
            scheduleButton.addActionListener(e -> showForm("SCHEDULE"));
        }
        else {
            courseButton.setVisible(false);
            classesButton.setVisible(false);
            roomButton.setVisible(false);
            scheduleButton.setVisible(false);
        }
//        enrollmentButton.addActionListener(e -> showPanel(enrollmentForm.getAdminEnrollmentForm()));
        if (staff.getRole().equals("Admin"))
        {
            contentPanel.add(staffForm.getMainPanel(), "STAFF");
            staffButton.addActionListener(e -> showForm("STAFF"));
        }
        else
            staffButton.setVisible(false);
        logoutButton.addActionListener(e -> {

            Window window = SwingUtilities.getWindowAncestor(mainPanel);
            window.dispose(); // đóng FormTeacher

            JFrame frame = new JFrame("Đăng nhập");

            FormDangNhap form = new FormDangNhap();

            frame.setContentPane(form.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    public void showPanel(JPanel newPanel){
        contentPanel.removeAll();

        contentPanel.add(newPanel);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showForm(String cardName) {

        switch (cardName) {

            case "CLAZZ":
                clazzForm.initData();
                clazzForm.refreshTable();
                break;

            case "SCHEDULE":
                scheduleForm.initData();
                scheduleForm.refreshTable();
                break;

            case "TEACHER":
                break;

            case "ROOM":
                roomForm.refreshTable();
                break;

            case "STAFF":
                staffForm.refreshTable();
                break;
        }

        cardLayout.show(contentPanel, cardName);
    }

    // Hỗ trợ điều hướng kèm dữ liệu
    public void showClassOfCourse(Long courseId) {
        clazzForm.initData();
        clazzForm.filterByCourse(courseId); // Gọi hàm lọc ở Form Lớp
        cardLayout.show(contentPanel, "CLAZZ");
    }

    public void showScheduleOfClass(Long classId) {
        scheduleForm.initData();
        scheduleForm.filterByClass(classId); // Gọi hàm lọc ở Form Lịch
        cardLayout.show(contentPanel, "SCHEDULE");
    }
}
