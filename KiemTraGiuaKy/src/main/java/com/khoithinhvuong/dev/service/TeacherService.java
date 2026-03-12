package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.PasswordUtils;
import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.model.UserAccount;
import com.khoithinhvuong.dev.repository.TeacherRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaTeacherRepository;

import java.time.LocalDate;
import java.util.List;

public class TeacherService {
    private static final TransactionManager tx = new TransactionManager();
    private final TeacherRepository teacherRepository = new JpaTeacherRepository(tx);

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    //Tạo thông tin giáo viên
    public void createTeacher(String name, String phone, String email, String specialty, LocalDate hireDate)
    {
        Teacher teacher = new Teacher(null, name, phone, email, specialty, hireDate, "Active");
        teacherRepository.create(teacher);
    }

    //Tạo tài khoản cho giáo viên tương ứng
    public void createTeacherWithAccount(Teacher teacher, String username, String password) {
        tx.runInTransaction(em -> {
            // 1. Lưu thông tin giáo viên trước để lấy ID
            em.persist(teacher);

            // 2. Tạo tài khoản liên kết với teacherId vừa tạo
            String hashedPw = PasswordUtils.hashPassword(password);
            UserAccount account = new UserAccount(null, username, hashedPw, UserAccount.Role.TEACHER, teacher.getTeacherId());
            em.persist(account);
            return null;
        });
    }

    // Cập nhật hồ sơ và tài khoản giáo viên
    public void updateTeacherWithAccount(Teacher teacher, String newUsername, String newPassword) {
        tx.runInTransaction(em -> {
            // 1. Cập nhật hồ sơ giáo viên
            em.merge(teacher);

            // 2. Kiểm tra nếu Admin có nhập Username/Password mới thì mới cập nhật UserAccount
            if (!newUsername.isEmpty() || !newPassword.isEmpty()) {
                // Tìm tài khoản dựa trên relatedId (là teacherId)
                UserAccount account = em.createQuery(
                                "FROM UserAccount u WHERE u.relatedId = :tid AND u.role = :role", UserAccount.class)
                        .setParameter("tid", teacher.getTeacherId())
                        .setParameter("role", UserAccount.Role.TEACHER)
                        .getResultStream().findFirst().orElse(null);

                if (account != null) {
                    if (!newUsername.isEmpty()) account.setUsername(newUsername);
                    if (!newPassword.isEmpty()) account.setPasswordHash(PasswordUtils.hashPassword(newPassword));
                    em.merge(account);
                }
            }
            return null;
        });
    }

    public void updateTeacher(Teacher teacher) {
        teacherRepository.update(teacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.delete(id);
    }

    //Tìm kiếm giáo viên theo chuyên môn
    public List<Teacher> searchBySpecialty(String specialty) {
        return teacherRepository.findAll().stream()
                .filter(t -> t.getSpecialty().toLowerCase().contains(specialty.toLowerCase()))
                .toList();
    }
}
