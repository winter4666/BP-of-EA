package com.github.winter4666.bpofea.user.dao;

import com.github.winter4666.bpofea.user.domain.model.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
