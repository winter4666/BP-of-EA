package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.common.domain.BusinessException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
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

    public void startCourse(Long teacherId, Course course) {
        Teacher teacher = teacherDao.findById(teacherId).orElseThrow(() -> new BusinessException("teacher not found"));
        teacher.startCourse(course);
        teacherDao.save(teacher);
    }
}
