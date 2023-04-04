package com.github.winter4666.bpofea.course.domain.service;

import com.github.winter4666.bpofea.common.domain.model.Page;
import com.github.winter4666.bpofea.common.domain.model.PageOptions;
import com.github.winter4666.bpofea.course.domain.model.Course;

import java.util.Optional;

public interface CourseDao {
    Page<Course> findAll(String namePrefix, PageOptions pageOptions);

    Course save(Course course);

    Optional<Course> findById(Long courseId);

    Course getById(Long courseId);
}
