package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Schedule;
import com.khoithinhvuong.dev.repository.ScheduleRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaScheduleRepository;

import java.util.List;

public class ScheduleService {
    private static final TransactionManager tx = new TransactionManager();
    private final ScheduleRepository scheduleRepository = new JpaScheduleRepository(tx);

    public void createSchedule(Schedule schedule) {
        // Cầm có thể dùng Stream API tại đây để kiểm tra phòng có bị trùng giờ không
        scheduleRepository.create(schedule);
    }

    public List<Schedule> getSchedulesByClass(Long classId) {
        return scheduleRepository.findByClassId(classId);
    }
}
