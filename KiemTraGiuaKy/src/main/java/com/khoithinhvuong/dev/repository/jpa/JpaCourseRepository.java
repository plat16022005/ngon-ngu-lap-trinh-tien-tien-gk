package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Course;
import com.khoithinhvuong.dev.repository.CourseRepository;

import java.util.List;

public class JpaCourseRepository implements CourseRepository {
    private final TransactionManager tx;
    public JpaCourseRepository(TransactionManager tx) {
        this.tx = tx;
    }

    @Override
    public void create(Course course) {
        tx.runInTransaction(
                em -> {
                    em.persist(course);
                    return null;
                });
    }

    @Override
    public void update(Course course) {
        tx.runInTransaction(
                em -> {
                    em.merge(course);
                    return null;
                });
    }

    @Override
    public Course findById(Long courseId ) {
        return tx.runInTransaction(em ->  em.find( Course.class, courseId) );
    }

    @Override
    public List<Course> findAll() {
        return tx.runInTransaction( em -> em.createQuery("FROM Course", Course.class).getResultList());

    }

    @Override
    public void delete(Long courseId ) {
        tx.runInTransaction(em -> {
            Course course = em.find(Course.class, courseId);
            if (course != null) {
                em.remove(course);
            }
            return null;
        });
    }
}

