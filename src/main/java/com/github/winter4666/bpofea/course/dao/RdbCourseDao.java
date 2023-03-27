package com.github.winter4666.bpofea.course.dao;

import com.github.winter4666.bpofea.common.dao.PageMapper;
import com.github.winter4666.bpofea.common.domain.model.Page;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RdbCourseDao implements CourseDao {

    private final CourseRepository courseRepository;

    @Override
    public Page<Course> findAll(int perPage, int page) {
        return PageMapper.toPage(courseRepository.findAll(PageRequest.of(page - 1, perPage)));
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }
}
