package com.github.winter4666.bpofea.course.domain.service;

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

}
