package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Room;

import java.util.List;

public interface RoomRepository {
    void save(Room room);
    Room findById(Long id);
    List<Room> findAll();
    void delete (Long id);
}
