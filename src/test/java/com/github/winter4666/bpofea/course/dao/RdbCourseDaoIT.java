package com.github.winter4666.bpofea.course.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.model.Page;
import com.github.winter4666.bpofea.common.domain.model.PageOptions;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.testsupport.RdbDaoTest;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.winter4666.bpofea.testsupport.SameFieldValuesAs.sameFieldValuesAs;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class RdbCourseDaoIT extends RdbDaoTest {

    @Autowired
    private RdbCourseDao courseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

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
                                withJsonPath("$[0].dayOfWeek", equalTo(course.getClassTimes().get(0).getDayOfWeek().toString())),
                                withJsonPath("$[0].startTime[0]", equalTo(course.getClassTimes().get(0).getStartTime().getHour())),
                                withJsonPath("$[0].startTime[1]", equalTo(course.getClassTimes().get(0).getStartTime().getMinute())),
                                withJsonPath("$[0].stopTime[0]", equalTo(course.getClassTimes().get(0).getStopTime().getHour())),
                                withJsonPath("$[0].stopTime[1]", equalTo(course.getClassTimes().get(0).getStopTime().getMinute()))
                        )
                )),
                () -> assertThat(courseInDb.get("capacity"), equalTo(course.getCapacity())),
                () -> assertThat(courseInDb.get("current_student_number"), equalTo(course.getCurrentStudentNumber())));
    }

    @Test
    void should_get_courses_successfully() throws JsonProcessingException {
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

        Page<Course> actualCourses = courseDao.findAll(prefixes.iterator().next().toString(), new PageOptions(perPage, 1));

        Course course = courseBuilder.build();
        assertAll(
                () -> assertThat(actualCourses.totalElements(), equalTo(totalElements)),
                () -> assertThat(actualCourses.content().size(), equalTo(perPage)),
                () -> assertThat(actualCourses.content().get(0), sameFieldValuesAs(course, "id", "name", "teacher")));
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
                () -> assertThat(courseInDb, sameFieldValuesAs(course, "id", "teacher")));
    }

    @Test
    @Transactional
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
                () -> assertThat(courseInDb.getClassTimes().get(0).getDayOfWeek(), equalTo(course.getClassTimes().get(0).getDayOfWeek())),
                () -> assertThat(courseInDb.getClassTimes().get(0).getStartTime(), equalTo(course.getClassTimes().get(0).getStartTime())),
                () -> assertThat(courseInDb.getClassTimes().get(0).getStopTime(), equalTo(course.getClassTimes().get(0).getStopTime())),
                () -> assertThat(courseInDb.getCapacity(), equalTo(course.getCapacity())));
    }
}