package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.Results;

import java.util.List;

public interface ResultsRepository {

    void create(Results results);

    void update(Results results);

    void delete(Long id);

    Results findById(Long id);

    List<Results> findAll();

    List<Results> findByClass(Long classId);

    Results findByStudent(Long studentId);

    Double calculateAverageScore(Long classId);

    boolean existsByStudentAndClass(Long studentId, Long classId);
    List<Object[]> getStudentsWithResults(Long classId);
    List<Object[]> getResultsByStudent(Long studentId);

    List<Object[]> getResultsByStudentAndClass(Long studentId, Long classId);
}