package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Results;
import com.khoithinhvuong.dev.repository.ResultsRepository;

import java.util.List;

public class JpaResultsRepository implements ResultsRepository {

    private final TransactionManager tx;

    public JpaResultsRepository(TransactionManager tx) {
        this.tx = tx;
    }

    @Override
    public void create(Results results) {
        tx.runInTransaction(em -> {
            em.persist(results);
            return null;
        });
    }

    @Override
    public void update(Results results) {
        tx.runInTransaction(em -> {
            em.merge(results);
            return null;
        });
    }

    @Override
    public void delete(Long id) {
        tx.runInTransaction(em -> {
            Results r = em.find(Results.class, id);
            if (r != null) {
                em.remove(r);
            }
            return null;
        });
    }

    @Override
    public Results findById(Long id) {
        return tx.runInTransaction(em ->
                em.find(Results.class, id)
        );
    }

    @Override
    public List<Results> findAll() {
        return tx.runInTransaction(em ->
                em.createQuery("SELECT r FROM Results r", Results.class)
                        .getResultList()
        );
    }

    @Override
    public List<Results> findByClass(Long classId) {
        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT r FROM Results r WHERE r.clazzEntity.classId = :classId",
                                Results.class)
                        .setParameter("classId", classId)
                        .getResultList()
        );
    }

    @Override
    public Results findByStudent(Long studentId) {
        List<Results> list = tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT r FROM Results r WHERE r.student.studentId = :studentId",
                                Results.class)
                        .setParameter("studentId", studentId)
                        .getResultList()
        );

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Double calculateAverageScore(Long classId) {

        Double avg = tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT AVG(r.score) FROM Results r WHERE r.clazzEntity.classId = :classId",
                                Double.class)
                        .setParameter("classId", classId)
                        .getSingleResult()
        );

        return avg == null ? 0.0 : avg;
    }

    @Override
    public boolean existsByStudentAndClass(Long studentId, Long classId) {

        Long count = tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT COUNT(r) FROM Results r WHERE r.student.studentId = :studentId AND r.clazzEntity.classId = :classId",
                                Long.class)
                        .setParameter("studentId", studentId)
                        .setParameter("classId", classId)
                        .getSingleResult()
        );

        return count > 0;
    }
    @Override
    public List<Object[]> getStudentsWithResults(Long classId) {

        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT s.studentId, s.fullName, r.score, r.grade, r.comment " +
                                        "FROM Enrollment e " +
                                        "JOIN e.student s " +
                                        "LEFT JOIN Results r ON r.student.studentId = s.studentId " +
                                        "AND r.clazzEntity.classId = :classId " +
                                        "WHERE e.classId = :classId",
                                Object[].class)
                        .setParameter("classId", classId)
                        .getResultList()
        );
    }
    @Override
    public List<Object[]> getResultsByStudent(Long studentId){

        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT c.className, r.score, r.grade, r.comment " +
                                        "FROM Results r " +
                                        "JOIN r.clazzEntity c " +
                                        "WHERE r.student.studentId = :sid",
                                Object[].class)
                        .setParameter("sid", studentId)
                        .getResultList()
        );
    }
    @Override
    public List<Object[]> getResultsByStudentAndClass(Long studentId, Long classId){

        return tx.runInTransaction(em ->
                em.createQuery(
                                "SELECT c.className, r.score, r.grade, r.comment " +
                                        "FROM Results r " +
                                        "JOIN r.clazzEntity c " +
                                        "WHERE r.student.studentId = :sid " +
                                        "AND c.classId = :cid",
                                Object[].class)
                        .setParameter("sid", studentId)
                        .setParameter("cid", classId)
                        .getResultList()
        );
    }
}