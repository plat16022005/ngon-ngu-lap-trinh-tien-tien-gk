package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.JpaUtil;
import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.repository.ClazzRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class JpaClazzRepository implements ClazzRepository {

    @Override
    public void save(Clazz clazz) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try{
            entitymanager.getTransaction().begin();
            if ( clazz.getClassId() == null )
                entitymanager.persist(clazz);
            else
                entitymanager.merge(clazz);
            entitymanager.getTransaction().commit();

        }
        catch(Exception e)
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
    public Clazz findById(Long id) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
           return entitymanager.find( Clazz.class, id);
        }
        finally
        {
            entitymanager.close();
        }
    }

    @Override
    public List<Clazz> findAll() {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            return entitymanager.createQuery( "FROM Clazz", Clazz.class).getResultList();
        }
        finally
        {
            entitymanager.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            entitymanager.getTransaction().begin();
            Clazz clazz = entitymanager.find(Clazz.class, id );
            if( clazz!= null )
            {
                entitymanager.remove( clazz ); //xoá khỏi Database
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

    @Override
    public List<Clazz> findByTeacher(Long teacherId) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            return entitymanager.createQuery( "FROM Clazz", Clazz.class).getResultList()
                    .stream()
                    .filter(c -> c.getTeacher().getTeacherId().equals(teacherId))
                    .collect(Collectors.toList());
        }
        finally
        {
            entitymanager.close();
        }
    }
}
