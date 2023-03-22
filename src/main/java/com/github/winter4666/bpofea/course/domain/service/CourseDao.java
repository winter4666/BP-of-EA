package com.github.winter4666.bpofea.course.domain.service;

import com.github.winter4666.bpofea.course.domain.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    List<Course> findAll();

    Course save(Course course);

    Optional<Course> findById(Long courseId);
}
