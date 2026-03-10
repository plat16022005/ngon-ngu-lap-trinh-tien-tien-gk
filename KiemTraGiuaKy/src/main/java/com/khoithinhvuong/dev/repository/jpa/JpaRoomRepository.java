package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.JpaUtil;
import com.khoithinhvuong.dev.model.Room;
import com.khoithinhvuong.dev.repository.RoomRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class JpaRoomRepository implements RoomRepository {
    @Override
    public void save(Room room) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            entitymanager.getTransaction().begin();
            if (room.getRoomId() == null ){
                entitymanager.persist(room);
            }
            else
            {
                entitymanager.merge(room);
            }
            entitymanager.getTransaction().commit();
        }
        catch (Exception e)
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
    public Room findById(Long id) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            return entitymanager.find(Room.class, id);
        }
        finally {
            entitymanager.close();
        }
    }

    @Override
    public List<Room> findAll() {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            return entitymanager.createQuery("FROM Room", Room.class).getResultList();
        }
        finally {
            entitymanager.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager entitymanager = JpaUtil.getEntityManager();
        try
        {
            entitymanager.getTransaction().begin();
            Room room = entitymanager.find(Room.class, id );
            if( room != null )
            {
                entitymanager.remove( room ); //xoá khỏi Database
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
