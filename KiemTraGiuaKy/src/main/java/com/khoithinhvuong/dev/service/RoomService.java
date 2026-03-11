package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Room;
import com.khoithinhvuong.dev.repository.RoomRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaRoomRepository;

import java.util.List;

public class RoomService {
    private static final TransactionManager tx = new TransactionManager();
    private final RoomRepository roomRepository = new JpaRoomRepository(tx);

    public void createRoom(String name, Integer capacity, String location)
    {
        Room room = new Room(null, name, capacity, location, "Available");
        roomRepository.create(room);
    }

    public void updateRoom(Room room)
    {
        roomRepository.update(room);
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public void deleteRoom( Long roomId )
    {
        roomRepository.delete(roomId);
    }
}
