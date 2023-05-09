package com.github.winter4666.bpofea.user.dao;

import com.github.winter4666.bpofea.course.dao.CourseRepository;
import com.github.winter4666.bpofea.user.domain.model.StudentCourse;
import com.github.winter4666.bpofea.user.domain.service.StudentCourseDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RdbStudentCourseDao implements StudentCourseDao {

    private final StudentCourseRepository studentCourseRepository;

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    @Override
    public Optional<StudentCourse> findByStudentIdAndCourseId(long studentId, long courseId) {
        return studentCourseRepository.findById(
            new StudentCourse.PK(studentRepository.getReferenceById(studentId), courseRepository.getReferenceById(courseId))
        );
    }

    @Override
    public void delete(StudentCourse studentCourse) {
        studentCourseRepository.delete(studentCourse);
    }
}
