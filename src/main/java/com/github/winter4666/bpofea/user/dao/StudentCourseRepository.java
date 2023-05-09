package com.github.winter4666.bpofea.user.dao;

import com.github.winter4666.bpofea.user.domain.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourse.PK> {
}
