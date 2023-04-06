package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.user.datafaker.TeacherBuilder;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TeacherBuilderTest {

    @ParameterizedTest
    @MethodSource("teacherBuilderProvider")
    void should_throw_exception_when_start_course_given_invalid_course(Teacher.TeacherBuilder teacherBuilder) {
        assertThrows(ConstraintViolationException.class, teacherBuilder::build);
    }

    static Stream<Teacher.TeacherBuilder> teacherBuilderProvider() {
        return Stream.of(
                new TeacherBuilder().name(" ").createTeacherBuilder(),
                new TeacherBuilder().jobNumber(" ").createTeacherBuilder()
        );
    }
}
