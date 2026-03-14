package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {

    void create(Attendance attendance);

    void update(Attendance attendance);

    void delete(Long id);

    Attendance findById(Long id);

    List<Attendance> findAll();

    List<Attendance> findByClass(Long classId);

    List<Attendance> findByStudent(Long studentId);

    List<Attendance> findByClassAndDate(Long classId, LocalDate date);

    Attendance findByStudentClassAndDate(Long studentId, Long classId, LocalDate date);

    List<Object[]> getStudentAttendance(Long classId, LocalDate date);

    boolean existsAttendance(Long studentId, Long classId, LocalDate date);
}