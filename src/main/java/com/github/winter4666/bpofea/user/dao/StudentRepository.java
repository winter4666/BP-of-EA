package com.github.winter4666.bpofea.user.dao;

import com.github.winter4666.bpofea.user.domain.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
