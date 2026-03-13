package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.repository.ClazzRepository;

import java.util.List;
import java.util.stream.Collectors;

public class JpaClazzRepository implements ClazzRepository {
    private final TransactionManager tx;
    public JpaClazzRepository(TransactionManager tx) {
        this.tx = tx;
    }
    @Override
    public void create(Clazz clazz) {
        tx.runInTransaction(
                em -> {
                    em.persist(clazz);
                    return null;
                });
    }

    @Override
    public void update(Clazz clazz) {
        tx.runInTransaction(
            em -> {
            em.merge(clazz);
            return null;
        });
    }

    @Override
    public Clazz findById(Long classId) {
        return tx.runInTransaction( em -> em.find( Clazz.class, classId));
    }

    @Override
    public List<Clazz> findAll() {
        return tx.runInTransaction( em -> em.createQuery("FROM Clazz", Clazz.class).getResultList());
    }

    @Override
    public void delete(Long classId) {
        tx.runInTransaction(em -> {
            Clazz clazz = em.find(Clazz.class, classId);
            if (clazz != null) {
                em.remove(clazz);
            }
            return null;
        });

    }

    @Override
    public List<Clazz> findByTeacher(Long teacherId) {
        return tx.runInTransaction( em -> em.createQuery("FROM Clazz", Clazz.class).getResultList())
                .stream().filter(c->c.getTeacher().getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    //Lọc lớp theo khoá học
    @Override
    public List<Clazz> findByCourse(Long courseId) {
        return tx.runInTransaction(em ->
                em.createQuery("SELECT c FROM Clazz c WHERE c.course.courseId = :cid", Clazz.class)
                        .setParameter("cid", courseId)
                        .getResultList());
    }
}
