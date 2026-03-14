package com.khoithinhvuong.dev.service;

import com.khoithinhvuong.dev.config.TransactionManager;
import com.khoithinhvuong.dev.model.Clazz;
import com.khoithinhvuong.dev.model.Results;
import com.khoithinhvuong.dev.model.Student;
import com.khoithinhvuong.dev.repository.ResultsRepository;
import com.khoithinhvuong.dev.repository.jpa.JpaResultsRepository;

import java.util.List;

public class ResultsService {

    private static final TransactionManager tx = new TransactionManager();
    private final ResultsRepository resultsRepository =
            new JpaResultsRepository(tx);

    // Lấy danh sách sinh viên + kết quả
    public List<Object[]> getStudentsWithResults(Long classId){
        return resultsRepository.getStudentsWithResults(classId);
    }

    // Lưu hoặc cập nhật kết quả
    public void saveResult(Long studentId,
                           Long classId,
                           double score,
                           String grade,
                           String comment){

        if(resultsRepository.existsByStudentAndClass(studentId,classId)){

            Results r = resultsRepository.findByStudent(studentId);

            r.setScore(score);
            r.setGrade(grade);
            r.setComment(comment);

            resultsRepository.update(r);

        }else{

            Results r = new Results();

            Student student = new Student();
            student.setStudentId(studentId);

            Clazz clazz = new Clazz();
            clazz.setClassId(classId);

            r.setStudent(student);
            r.setClassEntity(clazz);
            r.setScore(score);
            r.setGrade(grade);
            r.setComment(comment);

            resultsRepository.create(r);
        }
    }

    // Lấy kết quả theo lớp
    public List<Results> getResultsByClass(Long classId){
        return resultsRepository.findByClass(classId);
    }

    // Tính điểm trung bình lớp
    public double getAverageScore(Long classId){
        return resultsRepository.calculateAverageScore(classId);
    }

    // Quy đổi điểm số -> điểm chữ
    public String convertGrade(double score){

        if(score >= 9) return "A";
        if(score >= 8) return "B";
        if(score >= 7) return "C";
        if(score >= 6) return "D";
        return "F";
    }
    public List<Object[]> getResultsByStudent(Long studentId){
        return resultsRepository.getResultsByStudent(studentId);
    }

    public List<Object[]> getResultsByStudentAndClass(Long studentId, Long classId){
        return resultsRepository.getResultsByStudentAndClass(studentId,classId);
    }
}