package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseDao;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherDao teacherDao;

    private final CourseDao courseDao;

    public Teacher addTeacher(@NotBlank String name, @NotNull String jobNumber) {
        Teacher teacher = Teacher.builder().name(name).jobNumber(jobNumber).build();;
        return teacherDao.save(teacher);
    }

    @Transactional
    public Course createCourse(long teacherId, Course course) {
        Teacher teacher = findTeacherByIdAndThrowExceptionIfNotFound(teacherId);
        teacher.createCourse(course);
        return course;
    }

    @Transactional
    public void removeCourse(long teacherId, long courseId) {
        Teacher teacher = findTeacherByIdAndThrowExceptionIfNotFound(teacherId);
        Course course = courseDao.getById(courseId);
        teacher.removeCourse(course);
    }

    private Teacher findTeacherByIdAndThrowExceptionIfNotFound(long teacherId) {
        return teacherDao.findById(teacherId).orElseThrow(() -> new DataNotFoundException("Teacher cannot be found by teacher id {}", teacherId));
    }
}
