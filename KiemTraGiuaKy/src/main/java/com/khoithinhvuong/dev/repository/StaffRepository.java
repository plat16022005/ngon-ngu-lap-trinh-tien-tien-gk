package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Staff;
import java.util.List;

public interface StaffRepository {

    void create(Staff staff);

    void update(Staff staff);

    void delete(Long staffId);

    Staff findById(Long staffId);

    List<Staff> findAll();
    List<Staff> findAllExceptAdmin();
}