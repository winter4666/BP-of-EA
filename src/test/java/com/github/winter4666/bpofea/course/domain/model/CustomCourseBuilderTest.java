package com.github.winter4666.bpofea.course.domain.model;

import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomCourseBuilderTest {

    @ParameterizedTest
    @MethodSource("courseProvider")
    void should_throw_exception_when_start_course_given_invalid_course(CustomCourseBuilder courseBuilder) {
        assertThrows(ConstraintViolationException.class, courseBuilder::build);
    }

    static Stream<CustomCourseBuilder> courseProvider() {
        return Stream.of(
                new CourseBuilder().name(" ").createCustomCourseBuilder(),
                new CourseBuilder().startDate(null).createCustomCourseBuilder(),
                new CourseBuilder().stopDate(null).createCustomCourseBuilder(),
                new CourseBuilder().capacity(null).createCustomCourseBuilder(),
                new CourseBuilder().classTimes(null).createCustomCourseBuilder(),
                new CourseBuilder().classTimes(new ArrayList<>()).createCustomCourseBuilder(),
                new CourseBuilder().classTimes(List.of(new CourseBuilder.ClassTimeBuilder().dayOfWeek(null))).createCustomCourseBuilder(),
                new CourseBuilder().classTimes(List.of(new CourseBuilder.ClassTimeBuilder().startTime(null))).createCustomCourseBuilder(),
                new CourseBuilder().classTimes(List.of(new CourseBuilder.ClassTimeBuilder().stopTime(null))).createCustomCourseBuilder()
        );
    }

}