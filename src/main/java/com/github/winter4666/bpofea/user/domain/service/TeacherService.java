package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.user.domain.model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherDao teacherDao;

    public void addTeacher(String name, String jobNumber) {
        teacherDao.save(Teacher.builder().name(name).jobNumber(jobNumber).build());
    }
}
