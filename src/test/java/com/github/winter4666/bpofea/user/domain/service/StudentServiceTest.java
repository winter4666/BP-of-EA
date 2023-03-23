package com.github.winter4666.bpofea.user.domain.service;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseDao;
import com.github.winter4666.bpofea.user.domain.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private StudentService studentService;

    @Test
    void should_choose_course_successfully() {
        Faker faker = new Faker();
        Student student = mock(Student.class);
        Course course = mock(Course.class);
        long studentId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();
        when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
        when(courseDao.findById(courseId)).thenReturn(Optional.of(course));

        studentService.chooseCourse(studentId, courseId);

        verify(student).chooseCourse(course);
    }

    @Test
    void should_throw_exception_when_choose_course_given_student_not_found() {
        Faker faker = new Faker();
        long studentId = faker.number().randomNumber();
        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> studentService.chooseCourse(studentId, faker.number().randomNumber()));

        assertThat(exception.getMessage(), equalTo("Student cannot be found by student id " + studentId));
    }

    @Test
    void should_throw_exception_when_choose_course_given_course_not_found() {
        Faker faker = new Faker();
        Student student = mock(Student.class);
        long studentId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();
        when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
        when(courseDao.findById(courseId)).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> studentService.chooseCourse(studentId, courseId));

        assertThat(exception.getMessage(), equalTo("Course cannot be found by course id " + courseId));
    }

}