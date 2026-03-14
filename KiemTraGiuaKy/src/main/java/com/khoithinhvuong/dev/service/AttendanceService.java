package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Attendance;
import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.repository.AttendanceRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaAttendanceRepository;

import java.time.LocalDate;
import java.util.List;

public class AttendanceService {

    private static final TransactionManager tx = new TransactionManager();
    private final AttendanceRepository attendanceRepository = new JpaAttendanceRepository(tx);

    public List<Object[]> getStudentAttendance(Long classId, LocalDate date) {
        return attendanceRepository.getStudentAttendance(classId, date);
    }

    public void saveAttendance(Long studentId, Long classId, LocalDate date, String status) {

        Attendance attendance = attendanceRepository
                .findByStudentClassAndDate(studentId, classId, date);

        if (attendance != null) {

            attendance.setStatus(status);
            attendanceRepository.update(attendance);

        } else {

            Attendance newAttendance = new Attendance();

            newAttendance.setDate(date);
            newAttendance.setStatus(status);

            Student student = new Student();
            student.setStudentId(studentId);

            Clazz clazz = new Clazz();
            clazz.setClassId(classId);

            newAttendance.setStudent(student);
            newAttendance.setClassEntity(clazz);

            attendanceRepository.create(newAttendance);
        }
    }
}