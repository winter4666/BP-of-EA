package com.github.winter4666.bpofea.user.dao;

import com.github.winter4666.bpofea.user.domain.model.Student;
import com.github.winter4666.bpofea.user.domain.service.StudentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RdbStudentDao implements StudentDao {

    private final StudentRepository studentRepository;

    @Override
    public Optional<Student> findById(long id) {
        return studentRepository.findById(id);
    }

}
