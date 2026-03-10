package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Course;

import java.util.List;

public interface CourseRepository {
    void save(Course course);
    Course findById(Long courseId );
    List<Course> findAll();
    void delete(Long courseId );

}
