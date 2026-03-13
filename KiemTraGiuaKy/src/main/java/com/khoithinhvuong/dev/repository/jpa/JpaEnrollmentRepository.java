package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Enrollment;
import com.khoithinhvuong.dev.repository.EnrollmentRepository;

import java.util.List;

public class JpaEnrollmentRepository implements EnrollmentRepository {

    private final TransactionManager tx;

    public JpaEnrollmentRepository(TransactionManager tx) {
        this.tx = tx;
    }


    @Override
    public void create(Enrollment enrollment) {
        tx.runInTransaction(em -> {
            em.persist(enrollment);
            return null;
        });
    }

    @Override
    public void update(Enrollment enrollment) {
        tx.runInTransaction(em-> {
            em.merge(enrollment);
            return null;
        });
    }

    @Override
    public Enrollment findById(Long enrollmentId) {
        return tx.runInTransaction(em ->
            em.find(Enrollment.class,enrollmentId)
        );
    }

    @Override
    public List<Enrollment> findAll() {
        return tx.runInTransaction(em ->
                em.createQuery("FROM Enrollment", Enrollment.class)
                        .getResultList()
        );
    }

    @Override
    public List<Enrollment> findByStudentId(Long studentId) {
        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT e FROM Enrollment e WHERE e.student.studentId = :sid",
                                Enrollment.class)
                        .setParameter("sid", studentId)
                        .getResultList()
        );
    }

    @Override
    public void delete(Long enrollmentId) {
        tx.runInTransaction(em -> {
            Enrollment enrollment = em.find(Enrollment.class, enrollmentId);
            if (enrollment != null) {
                em.remove(enrollment);
            }
            return null;
        });
    }

    @Override
    public boolean checkAlreadyRegistered(Long studentId, Long classId) {
        return tx.runInTransaction(em -> {
            // Dùng COUNT để đếm số lượng bản ghi trùng khớp
            Long count = em.createQuery(
                            "SELECT COUNT(e) FROM Enrollment e WHERE e.student.studentId = :studentId AND e.classId = :classId", Long.class)
                    .setParameter("studentId", studentId)
                    .setParameter("classId", classId)
                    .getSingleResult();

            // Nếu count > 0 nghĩa là đã đăng ký rồi
            return count > 0;
        });
    }

    @Override
    public Long countStudentByClass(Long classId) {
        return tx.runInTransaction(em ->
                em.createQuery("SELECT COUNT(e) FROM Enrollment e WHERE e.classId = :classId", Long.class)
                        .setParameter("classId", classId)
                        .getSingleResult()
        );
    }

    @Override
    public void updateState(Long enrolmentId, String state) {
        tx.runInTransaction(em -> {
            em.createQuery("UPDATE Enrollment e SET e.status = :status WHERE e.enrollmentId = :id")
                    .setParameter("status", state)
                    .setParameter("id", enrolmentId)
                    .executeUpdate();
            return null;
        });
    }
}
