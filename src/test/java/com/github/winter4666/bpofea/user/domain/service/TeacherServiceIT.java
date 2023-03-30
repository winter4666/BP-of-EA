package com.github.winter4666.bpofea.user.domain.service;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.testsupport.RdbDaoTest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TeacherServiceIT extends RdbDaoTest {

    @Autowired
    private TeacherService teacherService;

    @ParameterizedTest
    @MethodSource("nameAndJobNumberProvider")
    void should_throw_exception_when_add_teacher_given_invalid_teacher(String name, String jobNumber) {
        assertThrows(ConstraintViolationException.class, () -> teacherService.addTeacher(name, jobNumber));
    }

    static Stream<Arguments> nameAndJobNumberProvider() {
        return Stream.of(
                arguments(" ", String.valueOf(new Faker().number().randomNumber())),
                arguments(new Faker().name().fullName(), null)
        );
    }

    @ParameterizedTest
    @MethodSource("courseProvider")
    void should_throw_exception_when_start_course_given_invalid_course(Course course) {
        assertThrows(ConstraintViolationException.class, () -> teacherService.createCourse(new Faker().number().randomNumber(), course));
    }

    static Stream<Course> courseProvider() {
        return Stream.of(
                new CourseBuilder().name(" ").build(),
                new CourseBuilder().startDate(null).build(),
                new CourseBuilder().stopDate(null).build(),
                new CourseBuilder().capacity(null).build(),
                new CourseBuilder().classTimes(null).build(),
                new CourseBuilder().classTimes(new ArrayList<>()).build(),
                new CourseBuilder().classTimes(List.of(new CourseBuilder.ClassTimeBuilder().dayOfWeek(null))).build(),
                new CourseBuilder().classTimes(List.of(new CourseBuilder.ClassTimeBuilder().startTime(null))).build(),
                new CourseBuilder().classTimes(List.of(new CourseBuilder.ClassTimeBuilder().stopTime(null))).build()
        );
    }

}
