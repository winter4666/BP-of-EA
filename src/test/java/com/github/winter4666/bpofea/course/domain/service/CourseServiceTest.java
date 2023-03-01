package com.github.winter4666.bpofea.course.domain.service;

import com.github.winter4666.bpofea.course.domain.model.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private CourseService courseService;

    @Test
    void should_add_user_successfully() {
        Course course = new Course();

        courseService.addCourse(course);

        verify(courseDao).save(eq(course));
    }

    @Test
    void should_get_users_successfully() {
        List<Course> courses = new ArrayList<>();
        when(courseDao.findAll()).thenReturn(courses);

        List<Course> actualCourses = courseService.getCourses();

        assertThat(actualCourses, equalTo(courses));
    }

}