package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.JpaUtil;
import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.repository.TeacherRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class JpaTeacherRepository implements TeacherRepository {
    @Override
    public void save(Teacher teacher) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            entitymanager.getTransaction().begin();
            if (teacher.getTeacherId() == null)
            {
                entitymanager.persist( teacher );
            }
            else
            {
                entitymanager.merge (teacher);
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

    @Override
    public List<Teacher> findAll() {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            return entitymanager.createQuery( "FROM Teacher", Teacher.class).getResultList();
        }
        finally
        {
            entitymanager.close();
        }
    }

    @Override
    public Teacher findById(long teacherId)
    {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            return entitymanager.find( Teacher.class, teacherId);
        }
        finally
        {
            entitymanager.close();
        }
    }

    @Override
    public void delete(long teacherId) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            entitymanager.getTransaction().begin();
            Teacher teacher = entitymanager.find(Teacher.class, teacherId );
            if( teacher!= null )
            {
                entitymanager.remove( teacher); //xoá khỏi Database
            }
            entitymanager.getTransaction().commit();
        }
        catch( Exception e )
        {
            entitymanager.getTransaction().rollback();
        }
        finally
        {
            entitymanager.close();
        }
    }
}
