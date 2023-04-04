package com.github.winter4666.bpofea.course.dao;

import com.github.winter4666.bpofea.common.dao.PageFactory;
import com.github.winter4666.bpofea.common.dao.PageRequestFactory;
import com.github.winter4666.bpofea.common.domain.model.Page;
import com.github.winter4666.bpofea.common.domain.model.PageOptions;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Repository
@RequiredArgsConstructor
public class RdbCourseDao implements CourseDao {

    private final CourseRepository courseRepository;

    @Override
    public Page<Course> findAll(String name, PageOptions pageOptions) {
        Course probe = Course.builder().name(name).classTimes(null).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", startsWith());
        return PageFactory.createPageFrom(courseRepository.findAll(Example.of(probe, matcher), PageRequestFactory.createPageRequestFrom(pageOptions)));
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
