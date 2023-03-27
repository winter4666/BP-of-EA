package com.github.winter4666.bpofea.course.domain.service;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void should_get_course_when_find_course_by_id_given_course_found() {
        Course course = new CourseBuilder().id(new Faker().number().randomNumber()).build();
        when(courseDao.findById(course.getId())).thenReturn(Optional.of(course));

        Course actualCourse = courseService.findCourseByIdAndThrowExceptionIfNotFound(course.getId());

        assertThat(actualCourse, equalTo(course));
    }

    @Test
    void should_throw_exception_when_find_course_by_id_given_course_no_found() {
        long courseId = new Faker().number().randomNumber();
        when(courseDao.findById(courseId)).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> courseService.findCourseByIdAndThrowExceptionIfNotFound(courseId));

        assertThat(exception.getMessage(), equalTo("Course cannot be found by course id " + courseId));
    }

}