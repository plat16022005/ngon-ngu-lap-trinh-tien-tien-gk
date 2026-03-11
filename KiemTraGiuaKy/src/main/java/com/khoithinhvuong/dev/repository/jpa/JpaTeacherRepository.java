package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.repository.TeacherRepository;

import java.util.List;

public class JpaTeacherRepository implements TeacherRepository {
    private final TransactionManager tx;
    public JpaTeacherRepository(TransactionManager tx)
    {
        this.tx = tx;
    }

    @Override
    public void create(Teacher teacher) {
        tx.runInTransaction(
                em -> {
                    em.persist(teacher);
                    return null;
                });
    }

    @Override
    public void update(Teacher teacher) {
        tx.runInTransaction(
                em -> {
                    em.merge(teacher);
                    return null;
                });
    }

    @Override
    public List<Teacher> findAll() {
            return tx.runInTransaction(em -> em.createQuery("FROM Teacher", Teacher.class).getResultList() );

    }

    @Override
    public Teacher findById(Long teacherId)
    {
        return tx.runInTransaction( em-> em.find(Teacher.class, teacherId));

    }

    @Override
    public void delete(Long teacherId) {
        tx.runInTransaction(em -> {
            Teacher teacher = em.find(Teacher.class, teacherId);
            if (teacher != null) {
                em.remove(teacher);
            }
            return null;
        });
    }
}
