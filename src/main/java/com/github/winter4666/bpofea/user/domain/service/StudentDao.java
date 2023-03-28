package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.user.domain.model.Student;

import java.util.Optional;

public interface StudentDao {

    Optional<Student> findById(long id);

}
