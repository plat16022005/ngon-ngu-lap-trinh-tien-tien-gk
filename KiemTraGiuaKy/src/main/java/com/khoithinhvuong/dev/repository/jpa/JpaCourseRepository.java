package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.JpaUtil;
import com.khoithinhvuong.dev.model.Course;
import com.khoithinhvuong.dev.repository.CourseRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class JpaCourseRepository implements CourseRepository {

    @Override
    public void save(Course course) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            entitymanager.getTransaction().begin();
            if (course.getCourseId() == null)
            {
                entitymanager.persist( course );
            }
            else
            {
                entitymanager.merge (course);
            }
            entitymanager.getTransaction().commit();
        }
        catch( Exception e )
        {
            entitymanager.getTransaction().rollback();
            e.printStackTrace();
        }
        finally
        {
            entitymanager.close(); //Đóng để tránh rò rỉ bộ nhớ
        }
    }

    @Override
    public Course findById(Long courseId ) {
        try (EntityManager entitymanager = JpaUtil.getEntityManager()) {
            return entitymanager.find(Course.class, courseId);
        }
    }

    @Override
    public List<Course> findAll() {
        try (EntityManager entitymanager = JpaUtil.getEntityManager()) {
            return entitymanager.createQuery("FROM Course", Course.class).getResultList();
        }
    }

    @Override
    public void delete(Long courseId ) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            entitymanager.getTransaction().begin();
            Course course = entitymanager.find(Course.class, courseId  );
            if( course != null )
            {
                entitymanager.remove( course ); //xoá khỏi Database
            }
            entitymanager.getTransaction().commit();
        }
        catch( Exception e )
        {
            entitymanager.getTransaction().rollback();
            e.printStackTrace();
        }
        finally
        {
            entitymanager.close();
        }
    }
}

