package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.JpaUtil;
import com.khoithinhvuong.dev.model.Schedule;
import com.khoithinhvuong.dev.repository.ScheduleRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class JpaScheduleRepository implements ScheduleRepository {

    @Override
    public void save(Schedule schedule) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            entitymanager.getTransaction().begin();
            if (schedule.getScheduleID() == null)
                entitymanager.persist(schedule);
            else
            {
                entitymanager.merge(schedule);
            }
            entitymanager.getTransaction().commit();
        }
        catch (Exception e)
        {
            entitymanager.getTransaction().rollback();
            e.printStackTrace();
        }
        finally {
            entitymanager.close();
        }
    }

    @Override
    public List<Schedule> findByClassId(Long classId) {
        try (EntityManager entitymanager = JpaUtil.getEntityManager()) {
            return entitymanager.createQuery("SELECT s FROM Schedule s WHERE s.classEntity.classId = :cid", Schedule.class)
                    .setParameter("cid", classId)
                    .getResultList();
        }
    }

    @Override
    public List<Schedule> findAll() {
        try (EntityManager entitymanager = JpaUtil.getEntityManager()) {
            return entitymanager.createQuery("FROM Schedule", Schedule.class).getResultList();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            entitymanager.getTransaction().begin();
            Schedule schedule = entitymanager.find(Schedule.class, id );
            if( schedule != null )
            {
                entitymanager.remove( schedule ); //xoá khỏi Database
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
