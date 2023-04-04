package com.github.winter4666.bpofea.course.domain.model;

import com.github.winter4666.bpofea.course.datafaker.TestCourseBuilder;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseBuilderTest {

    @ParameterizedTest
    @MethodSource("courseProvider")
    void should_throw_exception_when_start_course_given_invalid_course(Course.CourseBuilder courseBuilder) {
        assertThrows(ConstraintViolationException.class, courseBuilder::build);
    }

    static Stream<Course.CourseBuilder> courseProvider() {
        return Stream.of(
                new TestCourseBuilder().name(" ").createCourseBuilder(),
                new TestCourseBuilder().startDate(null).createCourseBuilder(),
                new TestCourseBuilder().stopDate(null).createCourseBuilder(),
                new TestCourseBuilder().capacity(null).createCourseBuilder(),
                new TestCourseBuilder().classTimes(null).createCourseBuilder(),
                new TestCourseBuilder().classTimes(new ArrayList<>()).createCourseBuilder(),
                new TestCourseBuilder().classTimes(List.of(new TestCourseBuilder.ClassTimeBuilder().dayOfWeek(null))).createCourseBuilder(),
                new TestCourseBuilder().classTimes(List.of(new TestCourseBuilder.ClassTimeBuilder().startTime(null))).createCourseBuilder(),
                new TestCourseBuilder().classTimes(List.of(new TestCourseBuilder.ClassTimeBuilder().stopTime(null))).createCourseBuilder()
        );
    }

}