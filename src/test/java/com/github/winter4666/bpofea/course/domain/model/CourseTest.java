package com.github.winter4666.bpofea.course.domain.model;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.exception.DataCollisionException;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.user.datafaker.TeacherBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CourseTest {

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
        TeacherBuilder teacherBuilder1 = new TeacherBuilder().id(teacherIds.get(0));
        TeacherBuilder teacherBuilder2 = new TeacherBuilder().id(teacherIds.get(1));
        List<String> courseNames = Stream.generate(() -> faker.educator().course()).distinct().limit(2).toList();
        Course course = new CourseBuilder().name(courseNames.get(0)).teacher(teacherBuilder1).build();

        return Stream.of(
                Arguments.of(course, course, true),
                Arguments.of(course, null, false),
                Arguments.of(course, new Object(), false),
                Arguments.of(new CourseBuilder().name(courseNames.get(0)).teacher(teacherBuilder1).build(),
                        new CourseBuilder().name(courseNames.get(0)).teacher(teacherBuilder1).build(), true),
                Arguments.of(new CourseBuilder().name(courseNames.get(0)).teacher(teacherBuilder1).build(),
                        new CourseBuilder().name(courseNames.get(1)).teacher(teacherBuilder1).build(), false),
                Arguments.of(new CourseBuilder().name(courseNames.get(0)).teacher(teacherBuilder1).build(),
                        new CourseBuilder().name(courseNames.get(0)).teacher(teacherBuilder2).build(), false)
        );
    }

    @Test
    public void should_set_start_date_when_set_start_date_given_parameter_not_null() {
        Course course = new Course();
        LocalDate startDate = LocalDate.of(2022, 1, 1);

        course.setStartDateIfNotNull(startDate);

        assertThat(course.getStartDate(), equalTo(startDate));
    }

    @Test
    public void should_not_set_start_date_when_set_start_date_given_parameter_is_null() {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        Course course = new CourseBuilder().startDate(startDate).build();

        course.setStartDateIfNotNull(null);

        assertThat(course.getStartDate(), equalTo(startDate));
    }

    @Test
    public void should_set_stop_date_when_set_start_date_given_parameter_not_null() {
        Course course = new Course();
        LocalDate stopDate = LocalDate.of(2022, 1, 1);

        course.setStopDateIfNotNull(stopDate);

        assertThat(course.getStopDate(), equalTo(stopDate));
    }

    @Test
    public void should_not_set_stop_date_when_set_start_date_given_parameter_is_null() {
        LocalDate stopDate = LocalDate.of(2023, 2, 1);
        Course course = new CourseBuilder().stopDate(stopDate).build();

        course.setStopDateIfNotNull(null);

        assertThat(course.getStopDate(), equalTo(stopDate));
    }

    @Test
    public void should_set_class_times_when_set_start_date_given_parameter_not_null() {
        Course course = new Course();
        List<ClassTime> classTimes = List.of(new CourseBuilder.ClassTimeBuilder().build());

        course.setClassTimesIfNotNull(classTimes);

        assertThat(course.getClassTimes(), equalTo(classTimes));
    }

    @Test
    public void should_not_set_class_times_when_set_start_date_given_parameter_is_null() {
        List<CourseBuilder.ClassTimeBuilder> classTimeBuilders = List.of(new CourseBuilder.ClassTimeBuilder());
        Course course = new CourseBuilder().classTimes(classTimeBuilders).build();

        course.setStopDateIfNotNull(null);

        assertThat(course.getClassTimes(), equalTo(classTimeBuilders.stream().map(CourseBuilder.ClassTimeBuilder::build).toList()));
    }

    @ParameterizedTest
    @EnumSource(value = Course.State.class, names = {"PUBLISHED"}, mode = EnumSource.Mode.EXCLUDE)
    public void should_throw_exception_when_on_chosen_given_state_is_not_published(Course.State state) {
        Course course = new CourseBuilder().state(state).build();

        DataCollisionException exception = assertThrows(DataCollisionException.class, course::onChosen) ;

        assertThat(exception.getMessage(), equalTo("The state of the course is not published. CourseId = " + course.getId()));
    }

    @Test
    public void should_throw_exception_when_on_chosen_given_student_number_reach_to_capacity() {
        Course course = new CourseBuilder().currentStudentNumber(20L).capacity(20L).state(Course.State.PUBLISHED).build();

        DataCollisionException exception = assertThrows(DataCollisionException.class, course::onChosen) ;

        assertThat(exception.getMessage(), equalTo("The people number in the course has reached to capacity. CourseId = " + course.getId()));
    }

    @Test
    public void should_increase_current_student_number_when_on_chosen_given_state_is_published() {
        long currentStudentNumber = 10L;
        Course course = new CourseBuilder().currentStudentNumber(currentStudentNumber).capacity(20L).state(Course.State.PUBLISHED).build();

        course.onChosen();

        assertAll(
                () -> assertThat(course.getCurrentStudentNumber(), equalTo(currentStudentNumber + 1)),
                () -> assertThat(course.getDomainEvents(), empty())
        );
    }

    @Test
    public void should_increase_current_student_number_and_register_event_when_on_chosen_given_state_is_published_and_course_is_full() {
        long currentStudentNumber = 19L;
        Course course = new CourseBuilder().currentStudentNumber(currentStudentNumber).capacity(20L).state(Course.State.PUBLISHED).build();

        course.onChosen();

        assertAll(
                () -> assertThat(course.getCurrentStudentNumber(), equalTo(currentStudentNumber + 1)),
                () -> assertThat(course.getDomainEvents().size(), equalTo(1))
        );
    }

    @Test
    public void should_decrease_current_student_number_when_on_revoked_given_state_is_published() {
        long currentStudentNumber = 10L;
        Course course = new CourseBuilder().currentStudentNumber(currentStudentNumber).capacity(20L).state(Course.State.PUBLISHED).build();

        course.onRevoked();

        assertThat(course.getCurrentStudentNumber(), equalTo(currentStudentNumber - 1));
    }
}