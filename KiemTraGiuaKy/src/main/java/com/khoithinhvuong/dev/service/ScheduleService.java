package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Schedule;
import com.khoithinhvuong.dev.repository.ScheduleRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaScheduleRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class ScheduleService {
    private static final TransactionManager tx = new TransactionManager();
    private final ScheduleRepository scheduleRepository = new JpaScheduleRepository(tx);

    public void createSchedule(Schedule newSchedule) {
        List <Schedule> allSchedules = scheduleRepository.findAll();

        //kiểm tra trùng phòng và giờ : 2 khoảng thời gian (s1, e1), (s2, e2) chồng chéo khi s1 < e2 và s2 < e1
        boolean isConflict = allSchedules.stream().anyMatch(
                s -> s.getRoom().getRoomId().equals(newSchedule.getRoom().getRoomId()) &&
                        s.getDate().equals(newSchedule.getDate()) &&
                        newSchedule.getStartTime().isBefore(s.getEndTime()) &&
                        s.getStartTime().isBefore(newSchedule.getEndTime()));

        if (isConflict) {
            throw new RuntimeException("Phòng học đã có lịch vào khung giờ này!");
        }
        scheduleRepository.create(newSchedule);
    }

    public List<Schedule> getSchedulesByClass(Long classId) {
        return scheduleRepository.findByClassId(classId);
    }

    public List<Schedule> getTeacherSchedule(Long teacherId) {
        return scheduleRepository.findByTeacherId(teacherId).stream()
                .sorted(Comparator.comparing(Schedule::getDate)
                        .thenComparing(Schedule::getStartTime))
                .toList();
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public void updateSchedule( Schedule schedule)
    {
        scheduleRepository.update(schedule);
    }

    public void deleteSchedule (Long scheduleId)
    {

        scheduleRepository.delete( scheduleId);
    }

    //Xoá tất cả lịch học của lớp khi lớp đó bị xoá
    public void deleteSchedulesByClass(Long classId) {
        List<Schedule> classSchedules = getSchedulesByClass(classId);
        classSchedules.forEach(s -> deleteSchedule(s.getScheduleID()));
    }

    public Schedule getScheduleById(Long scheduleId)
    {
        return scheduleRepository.findById( scheduleId);
    }
    public List<Schedule> getStudentSchedule(Long studentId, LocalDate date){
        return scheduleRepository.findByStudentAndDate(studentId, date);
    }
}
