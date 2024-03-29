package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseService;
import com.github.winter4666.bpofea.user.domain.model.Student;
import com.github.winter4666.bpofea.user.domain.model.StudentCourse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class StudentService {

    private final StudentDao studentDao;

    private final CourseService courseService;

    private final StudentCourseDao studentCourseDao;

    @Transactional
    public void chooseCourse(long studentId, long courseId) {
        Student student = findStudentByIdAndThrowExceptionIfNotFound(studentId);
        Course course = courseService.findCourseByIdAndThrowExceptionIfNotFound(courseId);
        student.chooseCourse(course);
    }

    @Transactional
    public void revokeChoice(long studentId, long courseId) {
        StudentCourse studentCourse = studentCourseDao.findByStudentIdAndCourseId(studentId, courseId).orElseThrow();
        studentCourseDao.delete(studentCourse);
        studentCourse.getCourse().onRevoked();
    }

    private Student findStudentByIdAndThrowExceptionIfNotFound(long studentId) {
        return studentDao.findById(studentId).orElseThrow(() -> new DataNotFoundException("Student cannot be found by student id {}", studentId));
    }
}
