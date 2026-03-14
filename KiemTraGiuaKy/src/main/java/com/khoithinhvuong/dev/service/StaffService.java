package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Staff;
import com.khoithinhvuong.dev.repository.StaffRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaStaffRepository;

import java.util.List;

public class StaffService {

    private static final TransactionManager tx = new TransactionManager();

    private final StaffRepository staffRepository =
            new JpaStaffRepository(tx);

    public void createStaff(Staff staff) {
        staffRepository.create(staff);
    }

    public void updateStaff(Staff staff) {
        staffRepository.update(staff);
    }

    public void deleteStaff(Long id) {
        staffRepository.delete(id);
    }

    public Staff getStaffById(Long id) {
        return staffRepository.findById(id);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }
    public List<Staff> getAllStaffExceptAdmin() {
        return staffRepository.findAllExceptAdmin();
    }
}