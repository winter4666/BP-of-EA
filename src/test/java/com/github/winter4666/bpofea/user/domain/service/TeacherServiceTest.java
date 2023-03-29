package com.github.winter4666.bpofea.user.domain.service;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.exception.DataInvalidException;
import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseService;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void should_add_teacher_successfully() {
        Faker faker = new Faker();
        Teacher teacher = Teacher.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().fullName())
                .jobNumber(String.valueOf(faker.number().randomNumber()))
                .build();
        when(teacherDao.save(argThat(t -> teacher.getName().equals(t.getName())
                && teacher.getJobNumber().equals(t.getJobNumber())))).thenReturn(teacher);

        Teacher returnedTeacher = teacherService.addTeacher(teacher.getName(), teacher.getJobNumber());

        assertThat(returnedTeacher, equalTo(teacher));
    }

    @Test
    void should_start_course_successfully() {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        Course course = new CourseBuilder().build();
        Teacher teacher = mock(Teacher.class);
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));

        Course returnedCourse = teacherService.startCourse(teacherId, course);

        verify(teacher).startCourse(course);
        assertThat(returnedCourse, equalTo(course));
    }

    @Test
    void should_remove_course_successfully() {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();
        Course course = new CourseBuilder().build();
        Teacher teacher = mock(Teacher.class);
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(courseService.findCourseByIdAndThrowExceptionIfNotFound(courseId)).thenReturn(course);

        teacherService.removeCourse(teacherId, courseId);

        verify(teacher).removeCourse(course);
    }

    @Test
    void should_throw_exception_when_start_course_given_teacher_not_found() {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        Course course = new CourseBuilder().build();
        Mockito.when(teacherDao.findById(teacherId)).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> teacherService.startCourse(teacherId, course));

        assertThat(exception.getMessage(), equalTo("Teacher cannot be found by teacher id " + teacherId));
    }

    @Test
    void should_throw_exception_when_start_course_given_stop_date_is_before_start_date() {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        Course course = new CourseBuilder().startDate(LocalDate.of(2023, 5, 1)).stopDate(LocalDate.of(2023, 1, 1)).build();

        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> teacherService.startCourse(teacherId, course));

        assertThat(exception.getMessage(), equalTo("Stop data should be later than start data in a course"));
    }
}