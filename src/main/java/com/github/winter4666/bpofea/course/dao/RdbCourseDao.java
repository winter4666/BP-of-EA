package com.github.winter4666.bpofea.course.dao;

import com.github.winter4666.bpofea.common.dao.PageFactory;
import com.github.winter4666.bpofea.common.dao.PageRequestFactory;
import com.github.winter4666.bpofea.common.domain.model.Page;
import com.github.winter4666.bpofea.common.domain.model.PageOptions;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.github.winter4666.bpofea.course.dao.CourseSpecs.hasNameStartingWith;

@Repository
@RequiredArgsConstructor
public class RdbCourseDao implements CourseDao {

    private final CourseRepository courseRepository;

    @Override
    public Page<Course> findAll(String name, PageOptions pageOptions) {
        return PageFactory.createPageFrom(courseRepository.findAll(hasNameStartingWith(name), PageRequestFactory.createPageRequestFrom(pageOptions)));
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Course getById(Long courseId) {
        return courseRepository.getReferenceById(courseId);
    }
}
