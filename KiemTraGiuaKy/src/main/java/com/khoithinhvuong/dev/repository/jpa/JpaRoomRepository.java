package com.khoithinhvuong.dev.repository.jpa;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Room;
import com.khoithinhvuong.dev.repository.RoomRepository;

import java.util.List;

public class JpaRoomRepository implements RoomRepository {
    private final TransactionManager tx;
    public JpaRoomRepository(TransactionManager tx) {
        this.tx = tx;
    }
    @Override
    public void create(Room room) {
        tx.runInTransaction(
                em -> {
                    em.persist(room);
                    return null;
                });
    }

    @Override
    public void update(Room room ){
        tx.runInTransaction(
                em -> {
                    em.merge(room);
                    return null;
                });
    }

    @Override
    public Room findById(Long roomId) {
        return tx.runInTransaction( em-> em.find(Room.class, roomId));
    }

    @Override
    public List<Room> findAll() {
        return tx.runInTransaction( em->em.createQuery("FROM Room", Room.class).getResultList());
    }

    @Override
    public void delete(Long roomId) {
        tx.runInTransaction(em -> {
            Room room = em.find(Room.class, roomId);
            if (room != null) {
                em.remove(room);
            }
            return null;
        });
    }

}
