package com.github.winter4666.bpofea.course.domain.model;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

class CourseTest {

    @Test
    void should_react_correctly_on_started() {
        Teacher teacher = mock(Teacher.class);
        Course course = new Course();

        course.onStarted(teacher);

        assertThat(course.getTeacher(), equalTo(teacher));
    }

    @ParameterizedTest
    @MethodSource("coursePairWithoutCollisionProvider")
    void should_return_false_when_invoke_collide_with_given_no_collision(Course course1, Course course2) {
        assertThat(course1.collideWith(course2), equalTo(false));
    }

    static Stream<Arguments> coursePairWithoutCollisionProvider() {
        return Stream.of(
                arguments(
                        new CourseBuilder()
                                .startDate(LocalDate.of(2023, 1, 1))
                                .stopDate(LocalDate.of(2023, 5, 1))
                                .build(),
                        new CourseBuilder()
                                .startDate(LocalDate.of(2023, 6, 1))
                                .stopDate(LocalDate.of(2023, 7, 1))
                                .build()
                ),
                arguments(
                        new CourseBuilder()
                                .startDate(LocalDate.of(2023, 6, 1))
                                .stopDate(LocalDate.of(2023, 7, 1))
                                .build(),
                        new CourseBuilder()
                                .startDate(LocalDate.of(2023, 1, 1))
                                .stopDate(LocalDate.of(2023, 5, 1))
                                .build()
                ),
                arguments(
                        new CourseBuilder()
                                .startDate(LocalDate.of(2023, 1, 1))
                                .stopDate(LocalDate.of(2023, 5, 1))
                                .classTimes(List.of(new CourseBuilder.ClassTimeBuilder().dayOfWeek(DayOfWeek.MONDAY)))
                                .build(),
                        new CourseBuilder()
                                .startDate(LocalDate.of(2023, 1, 1))
                                .stopDate(LocalDate.of(2023, 5, 1))
                                .classTimes(List.of(new CourseBuilder.ClassTimeBuilder().dayOfWeek(DayOfWeek.TUESDAY)))
                                .build()
                ),
                arguments(
                        new CourseBuilder()
                                .startDate(LocalDate.of(2023, 1, 1))
                                .stopDate(LocalDate.of(2023, 5, 1))
                                .classTimes(List.of(new CourseBuilder.ClassTimeBuilder().dayOfWeek(DayOfWeek.MONDAY)))
                                .build(),
                        new CourseBuilder()
                                .startDate(LocalDate.of(2023, 3, 1))
                                .stopDate(LocalDate.of(2023, 5, 1))
                                .classTimes(List.of(new CourseBuilder.ClassTimeBuilder().dayOfWeek(DayOfWeek.TUESDAY)))
                                .build()
                )
        );
    }

    @Test
    void should_return_true_when_invoke_collide_with_given_collision_existed() {
        Course course1 = new CourseBuilder().build();
        Course course2 = new CourseBuilder().build();
        assertThat(course1.collideWith(course2), equalTo(true));
    }

    @ParameterizedTest
    @MethodSource("coursePairAndExpectedResultProvider")
    public void should_return_expected_result_when_invoke_equals_given_different_arguments(Course course1, Object course2, boolean expectedResult) {
        assertThat(course1.equals(course2), equalTo(expectedResult));
    }

    static Stream<Arguments> coursePairAndExpectedResultProvider() {
        Faker faker = new Faker();
        List<Long> teacherIds = Stream.generate(() -> faker.number().randomNumber()).distinct().limit(2).toList();
        Teacher teacher1 = Teacher.builder().id(teacherIds.get(0)).build();
        Teacher teacher2 = Teacher.builder().id(teacherIds.get(1)).build();
        List<String> courseNames = Stream.generate(() -> faker.educator().course()).distinct().limit(2).toList();
        Course course = Course.builder().name(courseNames.get(0)).teacher(teacher1).build();

        return Stream.of(
                Arguments.of(course, course, true),
                Arguments.of(course, null, false),
                Arguments.of(course, new Object(), false),
                Arguments.of(Course.builder().name(courseNames.get(0)).teacher(teacher1).build(),
                        Course.builder().name(courseNames.get(0)).teacher(teacher1).build(), true),
                Arguments.of(Course.builder().name(courseNames.get(0)).teacher(teacher1).build(),
                        Course.builder().name(courseNames.get(1)).teacher(teacher1).build(), false),
                Arguments.of(Course.builder().name(courseNames.get(0)).teacher(teacher1).build(),
                        Course.builder().name(courseNames.get(0)).teacher(teacher2).build(), false)
        );
    }

}