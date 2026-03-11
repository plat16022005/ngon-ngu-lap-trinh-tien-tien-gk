package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Course;
import com.khoithinhvuong.dev.repository.CourseRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaCourseRepository;

import java.util.List;

public class CourseService {
    private static final TransactionManager tx = new TransactionManager();
    private final CourseRepository courseRepository = new JpaCourseRepository(tx);

    public void createCourse(String name, String desc, String level, Integer duration, Double fee) {
        // Kiểm tra đầu vào
        if (name == null || name.isEmpty())
            throw new RuntimeException("Tên khóa học không được để trống");
        if (fee < 0)
            throw new RuntimeException("Học phí không được âm");

        Course course = new Course(null, name, desc, level, duration, fee, "Active");
        courseRepository.create(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public void updateCourse(Course course) {
        courseRepository.update(course);
    }

    public void deleteCourse(Long id) {
        // Có thể thêm sau : Không cho xóa nếu khóa học đang có lớp học
        courseRepository.delete(id);
    }
}