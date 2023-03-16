package com.github.winter4666.bpofea.course.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ClassTimeTest {

    @Test
    void should_return_false_when_invoke_collide_with_given_no_collision() {
        ClassTime classTime1 = new ClassTime(DayOfWeek.MONDAY, LocalTime.of(9,0), LocalTime.of(10,0));
        ClassTime classTime2 = new ClassTime(DayOfWeek.TUESDAY, LocalTime.of(9,0), LocalTime.of(10,0));
        assertThat(classTime1.collideWith(classTime2), equalTo(false));
    }

    @ParameterizedTest
    @MethodSource("collidedClassTimePairProvider")
    void should_return_true_when_invoke_collide_with_given_collision_existed(ClassTime classTime1, ClassTime classTime2 ) {
        assertThat(classTime1.collideWith(classTime2), equalTo(true));
    }

    static Stream<Arguments> collidedClassTimePairProvider() {
        return Stream.of(
                arguments(new ClassTime(DayOfWeek.MONDAY, LocalTime.of(9,0), LocalTime.of(10,0)),
                        new ClassTime(DayOfWeek.MONDAY, LocalTime.of(9,30), LocalTime.of(10,30))),
                arguments(new ClassTime(DayOfWeek.MONDAY, LocalTime.of(11,0), LocalTime.of(12,0)),
                        new ClassTime(DayOfWeek.MONDAY, LocalTime.of(10,30), LocalTime.of(11,30))),
                arguments(new ClassTime(DayOfWeek.MONDAY, LocalTime.of(10,0), LocalTime.of(11,0)),
                        new ClassTime(DayOfWeek.MONDAY, LocalTime.of(9,0), LocalTime.of(12,0)))
        );
    }

}