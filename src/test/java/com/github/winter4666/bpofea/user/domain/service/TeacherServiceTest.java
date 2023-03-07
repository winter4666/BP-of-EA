package com.github.winter4666.bpofea.user.domain.service;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.BusinessException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void should_add_teacher_successfully() {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String jobNumber = String.valueOf(faker.number().randomNumber());

        teacherService.addTeacher(name, jobNumber);

        verify(teacherDao).save(argThat(t -> name.equals(t.getName())
                && jobNumber.equals(t.getJobNumber())));
    }

    @Test
    void should_start_course_successfully() {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        Course course = Mockito.mock(Course.class);
        Teacher teacher = Mockito.mock(Teacher.class);
        Mockito.when(teacherDao.findById(teacherId)).thenReturn(Optional.of(teacher));

        teacherService.startCourse(teacherId, course);

        verify(teacher).startCourse(course);
    }

    @Test
    void should_throw_exception_when_start_course_given_teacher_not_found() {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        Course course = Mockito.mock(Course.class);
        Mockito.when(teacherDao.findById(teacherId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> teacherService.startCourse(teacherId, course));

        assertThat(exception.getMessage(), equalTo("teacher not found"));
    }
}