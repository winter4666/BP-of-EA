package com.github.winter4666.bpofea.course.domain.service;

import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseDao courseDao;

    public List<Course> getCourses() {
        return courseDao.findAll();
    }

    public void addCourse(Course course) {
        courseDao.save(course);
    }

    public Course findCourseByIdAndThrowExceptionIfNotFound(long courseId) {
        return courseDao.findById(courseId).orElseThrow(() -> new DataNotFoundException("Course cannot be found by course id {}", courseId));
    }

}
