package com.github.winter4666.bpofea.course.dao;

import com.github.winter4666.bpofea.course.domain.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}