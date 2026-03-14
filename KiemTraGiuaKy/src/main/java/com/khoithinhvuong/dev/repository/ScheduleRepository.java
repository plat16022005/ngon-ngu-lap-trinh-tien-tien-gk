package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {
    void create(Schedule schedule);
    void update(Schedule schedule);
    List<Schedule> findByClassId(Long classId); //Tim lich hoc cua 1 lop
    List<Schedule> findAll();
    Schedule findById(Long id);
    List<Schedule> findByTeacherId(Long teacherId);

    void delete(Long id);
    List<Schedule> findByStudentAndDate(Long studentId, LocalDate date);
}
