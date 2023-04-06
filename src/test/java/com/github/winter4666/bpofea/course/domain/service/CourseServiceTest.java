package com.github.winter4666.bpofea.course.domain.service;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.common.domain.model.Page;
import com.github.winter4666.bpofea.common.domain.model.PageOptions;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private CourseService courseService;

    @Test
    void should_get_users_successfully() {
        Faker faker = new Faker();
        Page<Course> courses = new Page<>(new ArrayList<>(), faker.number().randomNumber());
        PageOptions pageOptions = new PageOptions((int)faker.number().randomNumber(), (int)faker.number().randomNumber());
        String name = faker.educator().course();
        when(courseDao.findAll(name, Course.State.DRAFT, pageOptions)).thenReturn(courses);

        Page<Course> actualCourses = courseService.getCourses(name, pageOptions);

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

    @Test
    void should_update_course_successfully() {
        long courseId = new Faker().number().randomNumber();
        Course course = mock(Course.class);
        when(courseDao.findById(courseId)).thenReturn(Optional.of(course));
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate stopDate =  LocalDate.of(2023, 5, 1);
        List<ClassTime> classTimes = List.of(new CourseBuilder.ClassTimeBuilder().build());

        courseService.updateCourse(courseId, startDate, stopDate, classTimes);

        verify(course).setStartDateIfNotNull(startDate);
        verify(course).setStopDateIfNotNull(stopDate);
        verify(course).setClassTimesIfNotNull(classTimes);
    }

}