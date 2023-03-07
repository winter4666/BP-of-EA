package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.user.domain.model.Teacher;

import java.util.Optional;

public interface TeacherDao {

    Teacher save(Teacher teacher);

    Optional<Teacher> findById(Long id);

}
