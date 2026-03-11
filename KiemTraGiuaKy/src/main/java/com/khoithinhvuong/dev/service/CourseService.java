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

    public Course getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    //Tìm kiếm theo tên
    public List<Course> searchCourses(String keyword) {
        return getAllCourses().stream()
                .filter(c -> c.getCourseName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public void updateCourse(Course course) {
        courseRepository.update(course);
    }

    public void deleteCourse(Long id) {
        // Có thể thêm sau : Không cho xóa nếu khóa học đang có lớp học
        courseRepository.delete(id);
    }

    //lọc các khóa học đang hoạt động và sắp xếp theo học phí giảm dần
    public List<Course> getActiveCoursesSortedByFee() {
        return courseRepository.findAll().stream()
                .filter(c -> "Active".equals(c.getStatus()))
                .sorted((c1, c2) -> c2.getFee().compareTo(c1.getFee()))
                .toList();
    }
}