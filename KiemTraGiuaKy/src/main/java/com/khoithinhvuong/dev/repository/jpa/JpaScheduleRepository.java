package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Schedule;
import com.khoithinhvuong.dev.repository.ScheduleRepository;

import java.time.LocalDate;
import java.util.List;

public class JpaScheduleRepository implements ScheduleRepository {
    private final TransactionManager tx;
    public JpaScheduleRepository(TransactionManager tx) {
        this.tx = tx;
    }

    @Override
    public void create(Schedule schedule) {
        tx.runInTransaction(
                em -> {
                    em.persist(schedule);
                    return null;
                });
    }

    @Override
    public void update(Schedule schedule) {
        tx.runInTransaction(
                em -> {
                    em.merge(schedule);
                    return null;
                });
    }

    @Override
    public List<Schedule> findByClassId(Long classId) {
        return tx.runInTransaction(em ->
                em.createQuery("SELECT s FROM Schedule s WHERE s.clazzEntity.classId = :cid", Schedule.class)
                        .setParameter("cid", classId)
                        .getResultList()
        );
    }
    @Override
    public List<Schedule> findAll() {
            return tx.runInTransaction(em -> em.createQuery("FROM Schedule", Schedule.class).getResultList() );
    }

    @Override
    public Schedule findById(Long id) {
        return tx.runInTransaction(em -> em.find(Schedule.class, id));
    }

    @Override
    public List<Schedule> findByTeacherId(Long teacherId) {
        return tx.runInTransaction(em ->
                em.createQuery("SELECT s FROM Schedule s WHERE s.clazzEntity.teacher.teacherId = :tid", Schedule.class)
                        .setParameter("tid", teacherId)
                        .getResultList()
        );
    }

    @Override
    public void delete(Long scheduleId) {
        tx.runInTransaction(em -> {
            Schedule schedule = em.find(Schedule.class, scheduleId);
            if (schedule != null) {
                em.remove(schedule);
            }
            return null;
        });
    }
    @Override
    public List<Schedule> findByStudentAndDate(Long studentId, LocalDate date) {

        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT s FROM Schedule s " +
                                        "JOIN Enrollment e ON e.classId = s.clazzEntity.classId " +
                                        "WHERE e.student.studentId = :sid " +
                                        "AND s.date = :date",
                                Schedule.class
                        )
                        .setParameter("sid", studentId)
                        .setParameter("date", date)
                        .getResultList()
        );
    }
}
