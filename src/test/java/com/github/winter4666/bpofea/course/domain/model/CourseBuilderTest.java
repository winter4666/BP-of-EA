package com.github.winter4666.bpofea.course.domain.model;

import com.github.winter4666.bpofea.common.domain.exception.DataInvalidException;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseBuilderTest {

    @ParameterizedTest
    @MethodSource("courseProvider")
    void should_throw_exception_when_start_course_given_invalid_course(Course.CourseBuilder courseBuilder) {
        Exception exception = assertThrows(Exception.class, courseBuilder::build);

        assertThat(exception, anyOf(instanceOf(ConstraintViolationException.class), instanceOf(DataInvalidException.class)));
    }

    static Stream<Course.CourseBuilder> courseProvider() {
        return Stream.of(
                new CourseBuilder().name(" ").createCourseBuilder(),
                new CourseBuilder().startDate(null).createCourseBuilder(),
                new CourseBuilder().stopDate(null).createCourseBuilder(),
                new CourseBuilder().capacity(null).createCourseBuilder(),
                new CourseBuilder().classTimes(null).createCourseBuilder(),
                new CourseBuilder().classTimes(new ArrayList<>()).createCourseBuilder(),
                new CourseBuilder().classTimes(List.of(new CourseBuilder.ClassTimeBuilder().dayOfWeek(null))).createCourseBuilder(),
                new CourseBuilder().classTimes(List.of(new CourseBuilder.ClassTimeBuilder().startTime(null))).createCourseBuilder(),
                new CourseBuilder().classTimes(List.of(new CourseBuilder.ClassTimeBuilder().stopTime(null))).createCourseBuilder(),
                new CourseBuilder().startDate(LocalDate.of(2023, 5, 1)).stopDate(LocalDate.of(2023, 1, 1))
                        .createCourseBuilder(),
                new CourseBuilder().classTimes(List.of(new CourseBuilder.ClassTimeBuilder(), new CourseBuilder.ClassTimeBuilder())).createCourseBuilder()
        );
    }

}