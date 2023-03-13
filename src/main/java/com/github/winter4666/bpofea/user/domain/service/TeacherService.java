package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherDao teacherDao;

    public Teacher addTeacher(String name, String jobNumber) {
        Teacher teacher = Teacher.builder().name(name).jobNumber(jobNumber).build();;
        return teacherDao.save(teacher);
    }

    @Transactional
    public Course startCourse(Long teacherId, Course course) {
        Teacher teacher = teacherDao.findById(teacherId).orElseThrow(() -> new DataNotFoundException("Teacher cannot be found by teacher id {}", teacherId));
        teacher.startCourse(course);
        return course;
    }
}
