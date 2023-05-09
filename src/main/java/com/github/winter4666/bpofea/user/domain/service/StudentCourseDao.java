package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.user.domain.model.StudentCourse;

import java.util.Optional;

public interface StudentCourseDao {
    Optional<StudentCourse> findByStudentIdAndCourseId(long studentId, long courseId);

    void delete(StudentCourse studentCourse);
}
