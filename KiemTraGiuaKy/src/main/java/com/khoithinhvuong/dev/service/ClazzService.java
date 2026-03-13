package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.model.Course;
import com.khoithinhvuong.dev.model.Teacher;
import com.khoithinhvuong.dev.repository.ClazzRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaClazzRepository;

import java.time.LocalDate;
import java.util.List;

public class ClazzService
{
    private static final TransactionManager tx = new TransactionManager();
    private final ClazzRepository clazzRepository = new JpaClazzRepository(tx);

    public void createClass(String name, LocalDate start, LocalDate end, Integer max, Teacher teacher, Course course) {
        //Ngày kết thúc phải sau ngày bắt đầu
        if (end.isBefore(start))
            throw new RuntimeException("Ngày kết thúc phải sau ngày bắt đầu!");

        Clazz clazz = new Clazz(null, name, start, end, max, "Open", teacher, course);
        clazzRepository.create(clazz);
    }

    public Clazz getClassById(Long classId)
    {
        return clazzRepository.findById( classId);
    }

    public List<Clazz> getAllClasses() {
        return clazzRepository.findAll();
    }

    public List<Clazz> getClassesByTeacher(Long teacherId) {
        return clazzRepository.findByTeacher(teacherId);
    }

    //Lấy danh sách lớp theo khoá học tương ứng
    public List<Clazz> getClassesByCourse(Long courseId) {
        return clazzRepository.findByCourse(courseId);
    }

    public void updateClass( Clazz clazz)
    {
        clazzRepository.update(clazz);
    }

    public void deleteClass (Long classId)
    {
        clazzRepository.delete( classId);
    }

    //Đếm số lượng lớp học của 1 giáo viên
    public long countClassesByTeacher(Long teacherId) {
        return clazzRepository.findAll().stream()
                .filter(c -> c.getTeacher().getTeacherId().equals(teacherId))
                .count();
    }
}