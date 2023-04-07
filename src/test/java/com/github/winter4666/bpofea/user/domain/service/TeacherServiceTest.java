package com.github.winter4666.bpofea.user.domain.service;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseDao;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.github.winter4666.bpofea.testsupport.SameFieldValuesAs.sameFieldValuesAs;
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
    private CourseDao courseDao;

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
    void should_create_course_successfully() {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        CourseBuilder courseBuilder = new CourseBuilder();
        Teacher teacher = mock(Teacher.class);
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));

        Course returnedCourse = teacherService.createCourse(teacherId, courseBuilder.buildCreateCourseRequest());

        verify(teacher).createCourse(refEq(courseBuilder.build(), "teacher", "currentStudentNumber", "version"));
        assertThat(returnedCourse, sameFieldValuesAs(courseBuilder.build(), "teacher", "currentStudentNumber", "version"));
    }

    @Test
    void should_remove_course_successfully() {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();
        Course course = new CourseBuilder().build();
        Teacher teacher = mock(Teacher.class);
        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(courseDao.getById(courseId)).thenReturn(course);

        teacherService.removeCourse(teacherId, courseId);

        verify(teacher).removeCourse(course);
    }

    @Test
    void should_throw_exception_when_create_course_given_teacher_not_found() {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        Mockito.when(teacherDao.findById(teacherId)).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> teacherService.createCourse(teacherId, new CourseBuilder().buildCreateCourseRequest()));

        assertThat(exception.getMessage(), equalTo("Teacher cannot be found by teacher id " + teacherId));
    }
}