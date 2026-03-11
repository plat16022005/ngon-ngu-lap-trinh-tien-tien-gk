package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Room;
import com.khoithinhvuong.dev.repository.RoomRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaRoomRepository;

import java.util.List;

public class RoomService {
    private static final TransactionManager tx = new TransactionManager();
    private final RoomRepository roomRepository = new JpaRoomRepository(tx);

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
