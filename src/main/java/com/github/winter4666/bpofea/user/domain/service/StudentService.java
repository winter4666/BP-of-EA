package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseDao;
import com.github.winter4666.bpofea.user.domain.model.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class StudentService {

    private final StudentDao studentDao;

    private final CourseDao courseDao;

    @Transactional
    public void chooseCourse(Long studentId, Long courseId) {
        Student student = studentDao.findById(studentId).orElseThrow(() -> new DataNotFoundException("Student cannot be found by student id {}", studentId));
        Course course = courseDao.findById(courseId).orElseThrow(() -> new DataNotFoundException("Course cannot be found by course id {}", courseId));
        student.chooseCourse(course);
    }
}
