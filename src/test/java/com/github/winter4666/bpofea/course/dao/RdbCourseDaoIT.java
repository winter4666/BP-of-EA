package com.github.winter4666.bpofea.course.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.model.Page;
import com.github.winter4666.bpofea.common.domain.model.PageOptions;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.testsupport.RdbDaoTest;
import jakarta.persistence.OptimisticLockException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.winter4666.bpofea.testsupport.SameFieldValuesAs.sameFieldValuesAs;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RdbCourseDaoIT extends RdbDaoTest {

    @Autowired
    private RdbCourseDao courseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void should_insert_successfully() {
        Course course = new CourseBuilder().build();

        courseDao.save(course);

        Map<String, Object> courseInDb = jdbcTemplate.queryForMap("select * from course where id = ?", course.getId());
        assertAll(
                () -> assertThat(courseInDb.get("name"), equalTo(course.getName())),
                () -> assertThat(DateFormatUtils.format((Date)courseInDb.get("start_date"), "yyyy-MM-dd"), equalTo(course.getStartDate().toString())),
                () -> assertThat(DateFormatUtils.format((Date)courseInDb.get("stop_date"), "yyyy-MM-dd"), equalTo(course.getStopDate().toString())),
                () -> assertThat(courseInDb.get("class_times"), isJson(allOf(
                                withJsonPath("$[0].dayOfWeek", equalTo(course.getClassTimes().get(0).dayOfWeek().toString())),
                                withJsonPath("$[0].startTime[0]", equalTo(course.getClassTimes().get(0).startTime().getHour())),
                                withJsonPath("$[0].startTime[1]", equalTo(course.getClassTimes().get(0).startTime().getMinute())),
                                withJsonPath("$[0].stopTime[0]", equalTo(course.getClassTimes().get(0).stopTime().getHour())),
                                withJsonPath("$[0].stopTime[1]", equalTo(course.getClassTimes().get(0).stopTime().getMinute()))
                        )
                )),
                () -> assertThat(courseInDb.get("capacity"), equalTo(course.getCapacity())),
                () -> assertThat(courseInDb.get("current_student_number"), equalTo(course.getCurrentStudentNumber())),
                () -> assertThat(courseInDb.get("state"), equalTo(course.getState().toString())));
    }

    @Test
    void should_return_courses_when_find_all_given_course_prefix_and_status() throws JsonProcessingException {
        Faker faker = new Faker();
        long totalElements = 11L;
        Set<String> courseNames = Stream.generate(() -> faker.educator().course()).distinct().limit(totalElements).collect(Collectors.toSet());
        CourseBuilder courseBuilder = new CourseBuilder();
        Set<Character> prefixes = Set.of('a', 'b');
        for(char c : prefixes) {
            for(String courseName : courseNames) {
                new SimpleJdbcInsert(jdbcTemplate).withTableName("course")
                        .execute(courseBuilder.name(c + courseName).buildArgsForDbInsertion());
            }
        }
        int perPage = 10;

        Page<Course> actualCourses = courseDao.findCoursesNotRelatedToStudent(null, prefixes.iterator().next().toString(), Course.State.DRAFT, new PageOptions(perPage, 1));

        Course course = courseBuilder.build();
        assertAll(
                () -> assertThat(actualCourses.totalElements(), equalTo(totalElements)),
                () -> assertThat(actualCourses.content().size(), equalTo(perPage)),
                () -> assertThat(actualCourses.content().get(0), sameFieldValuesAs(course, "id", "name", "teacher", "studentCourses")));
    }

    @Test
    void should_return_courses_when_find_all_given_student_id() throws JsonProcessingException {
        Faker faker = new Faker();
        List<Long> studentIds =  Stream.generate(() -> faker.number().randomNumber()).distinct().limit(2).toList();
        List<String> courseNames = Stream.generate(() -> faker.educator().course()).distinct().limit(2).toList();
        CourseBuilder courseBuilder = new CourseBuilder();
        long courseId1 = new SimpleJdbcInsert(jdbcTemplate).withTableName("course")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(courseBuilder.name(courseNames.get(0)).buildArgsForDbInsertion()).longValue();
        long courseId2 = new SimpleJdbcInsert(jdbcTemplate).withTableName("course")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(courseBuilder.name(courseNames.get(1)).buildArgsForDbInsertion()).longValue();
        new SimpleJdbcInsert(jdbcTemplate).withTableName("student_course")
                .execute(new HashMap<>(){
                    {put("student_id", studentIds.get(0));}
                    {put("course_id", courseId1);}
                });
        new SimpleJdbcInsert(jdbcTemplate).withTableName("student_course")
                .execute(new HashMap<>(){
                    {put("student_id", studentIds.get(1));}
                    {put("course_id", courseId2);}
                });

        Page<Course> actualCourses = courseDao.findCoursesNotRelatedToStudent(studentIds.get(0), null, null, new PageOptions(10, 1));

        assertAll(
                () -> assertThat(actualCourses.totalElements(), equalTo(1L)),
                () -> assertThat(actualCourses.content().get(0).getName(), equalTo(courseNames.get(1)))
        );
    }

    @Test
    void should_return_courses_when_find_all_given_course_prefix_and_state_is_null() throws JsonProcessingException {
        Faker faker = new Faker();
        long totalElements = 11L;
        Set<String> courseNames = Stream.generate(() -> faker.educator().course()).distinct().limit(totalElements).collect(Collectors.toSet());
        CourseBuilder courseBuilder = new CourseBuilder();
        for(String courseName : courseNames) {
            new SimpleJdbcInsert(jdbcTemplate).withTableName("course")
                    .execute(courseBuilder.name(courseName).buildArgsForDbInsertion());
        }
        int perPage = 10;

        Page<Course> actualCourses = courseDao.findCoursesNotRelatedToStudent(null,null, null, new PageOptions(perPage, 1));

        Course course = courseBuilder.build();
        assertAll(
                () -> assertThat(actualCourses.totalElements(), equalTo(totalElements)),
                () -> assertThat(actualCourses.content().size(), equalTo(perPage)),
                () -> assertThat(actualCourses.content().get(0), sameFieldValuesAs(course, "id", "name", "teacher", "studentCourses")));
    }

    @Test
    void should_find_course_by_id_successfully() throws JsonProcessingException {
        CourseBuilder courseBuilder = new CourseBuilder();
        long courseId = new SimpleJdbcInsert(jdbcTemplate).withTableName("course").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(courseBuilder.buildArgsForDbInsertion()).longValue();

        Course courseInDb = courseDao.findById(courseId).orElse(null);

        Course course = courseBuilder.build();
        assertAll(
                () -> assertThat(courseInDb, notNullValue()),
                () -> assertThat(courseInDb.getId(), equalTo(courseId)),
                () -> assertThat(courseInDb, sameFieldValuesAs(course, "id", "teacher", "studentCourses")));
    }

    @Test
    void should_get_course_by_id_successfully() throws JsonProcessingException {
        CourseBuilder courseBuilder = new CourseBuilder();
        long courseId = new SimpleJdbcInsert(jdbcTemplate).withTableName("course").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(courseBuilder.buildArgsForDbInsertion()).longValue();

        Course courseInDb = courseDao.getById(courseId);

        Course course = courseBuilder.build();
        assertAll(
                () -> assertThat(courseInDb.getId(), equalTo(courseId)),
                () -> assertThat(courseInDb.getName(), equalTo(course.getName())),
                () -> assertThat(courseInDb.getStartDate(), equalTo(course.getStartDate())),
                () -> assertThat(courseInDb.getStopDate(), equalTo(course.getStopDate())),
                () -> assertThat(courseInDb.getCapacity(), equalTo(course.getCapacity())),
                () -> assertThat(courseInDb.getClassTimes().get(0).dayOfWeek(), equalTo(course.getClassTimes().get(0).dayOfWeek())),
                () -> assertThat(courseInDb.getClassTimes().get(0).startTime(), equalTo(course.getClassTimes().get(0).startTime())),
                () -> assertThat(courseInDb.getClassTimes().get(0).stopTime(), equalTo(course.getClassTimes().get(0).stopTime())),
                () -> assertThat(courseInDb.getCapacity(), equalTo(course.getCapacity())),
                () -> assertThat(courseInDb.getState(), equalTo(course.getState())));
    }

    @Test
    void should_throw_exception_when_update_course_given_collision_occurred() throws JsonProcessingException {
        CourseBuilder courseBuilder = new CourseBuilder();
        long courseId = new SimpleJdbcInsert(jdbcTemplate).withTableName("course").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(courseBuilder.buildArgsForDbInsertion()).longValue();
        Course course = courseDao.findById(courseId).orElseThrow();
        jdbcTemplate.update("update course set version = version + 1 where id = ?", courseId);
        course.setStartDateIfNotNull(LocalDate.of(2023, 2, 1));

        assertThrows(OptimisticLockException.class, () -> entityManager.flush());
    }

    @Test
    void should_delete_course_by_id_successfully() {
        Course course = new CourseBuilder().build();
        entityManager.persist(course);

        courseDao.delete(course);

        entityManager.flush();
        Long courseCount = jdbcTemplate.queryForObject("select count(1) from course where id = ?", Long.class, course.getId());
        assertThat(courseCount, is(equalTo(0L)));
    }
}