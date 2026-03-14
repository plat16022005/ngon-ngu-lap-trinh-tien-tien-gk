package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Attendance;
import com.khoithinhvuong.dev.repository.AttendanceRepository;

import java.time.LocalDate;
import java.util.List;

public class JpaAttendanceRepository implements AttendanceRepository {

    private final TransactionManager tx;

    public JpaAttendanceRepository(TransactionManager tx) {
        this.tx = tx;
    }

    @Override
    public void create(Attendance attendance) {
        tx.runInTransaction(em -> {
            em.persist(attendance);
            return null;
        });
    }

    @Override
    public void update(Attendance attendance) {
        tx.runInTransaction(em -> {
            em.merge(attendance);
            return null;
        });
    }

    @Override
    public void delete(Long id) {
        tx.runInTransaction(em -> {
            Attendance attendance = em.find(Attendance.class, id);
            if (attendance != null) {
                em.remove(attendance);
            }
            return null;
        });
    }

    @Override
    public Attendance findById(Long id) {
        return tx.runInTransaction(em ->
                em.find(Attendance.class, id)
        );
    }

    @Override
    public List<Attendance> findAll() {
        return tx.runInTransaction(em ->
                em.createQuery("SELECT a FROM Attendance a", Attendance.class)
                        .getResultList()
        );
    }

    @Override
    public List<Attendance> findByClass(Long classId) {
        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT a FROM Attendance a WHERE a.clazzEntity.classId = :classId",
                                Attendance.class)
                        .setParameter("classId", classId)
                        .getResultList()
        );
    }

    @Override
    public List<Attendance> findByStudent(Long studentId) {
        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT a FROM Attendance a WHERE a.student.studentId = :studentId",
                                Attendance.class)
                        .setParameter("studentId", studentId)
                        .getResultList()
        );
    }

    @Override
    public List<Attendance> findByClassAndDate(Long classId, LocalDate date) {
        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT a FROM Attendance a WHERE a.clazzEntity.classId = :classId AND a.date = :date",
                                Attendance.class)
                        .setParameter("classId", classId)
                        .setParameter("date", date)
                        .getResultList()
        );
    }

    @Override
    public boolean existsAttendance(Long studentId, Long classId, LocalDate date) {

        Long count = tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT COUNT(a) FROM Attendance a " +
                                        "WHERE a.student.studentId = :studentId " +
                                        "AND a.clazzEntity.classId = :classId " +
                                        "AND a.date = :date",
                                Long.class)
                        .setParameter("studentId", studentId)
                        .setParameter("classId", classId)
                        .setParameter("date", date)
                        .getSingleResult()
        );

        return count > 0;
    }

    @Override
    public List<Object[]> getStudentAttendance(Long classId, LocalDate date) {

        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT s.studentId, s.fullName, a.status " +
                                        "FROM Enrollment e " +
                                        "JOIN e.student s " +
                                        "LEFT JOIN Attendance a ON a.student.studentId = s.studentId " +
                                        "AND a.clazzEntity.classId = :classId " +
                                        "AND a.date = :date " +
                                        "WHERE e.classId = :classId",
                                Object[].class)
                        .setParameter("classId", classId)
                        .setParameter("date", date)
                        .getResultList()
        );
    }
    @Override
    public Attendance findByStudentClassAndDate(Long studentId, Long classId, LocalDate date) {

        List<Attendance> result = tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT a FROM Attendance a " +
                                        "WHERE a.student.studentId = :studentId " +
                                        "AND a.clazzEntity.classId = :classId " +
                                        "AND a.date = :date",
                                Attendance.class)
                        .setParameter("studentId", studentId)
                        .setParameter("classId", classId)
                        .setParameter("date", date)
                        .getResultList()
        );

        return result.isEmpty() ? null : result.get(0);
    }
}