package com.github.winter4666.bpofea.course.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.HibernateObjectMapperHolder;
import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseDao;
import com.github.winter4666.bpofea.testbase.RdbDaoTest;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class CourseRepositoryTest extends RdbDaoTest {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void should_insert_successfully() {
        Faker faker = new Faker();
        Course course = Course.builder()
                .name(faker.educator().course())
                .startDate(new Date())
                .stopDate(DateUtils.addMonths(new Date(), 6))
                .classTimes(List.of(new ClassTime(DayOfWeek.MONDAY, LocalTime.of(9,0),LocalTime.of(10, 0))))
                .build();

        courseDao.save(course);

        List<Map<String, Object>> courses = jdbcTemplate.queryForList("select * from course");
        assertAll(
                () -> assertThat(courses.size(), equalTo(1)),
                () -> assertThat(courses.get(0).get("name"), equalTo(course.getName())),
                () -> assertThat(courses.get(0).get("start_date"), equalTo(DateUtils.truncate(course.getStartDate(), Calendar.DATE))),
                () -> assertThat(courses.get(0).get("stop_date"), equalTo(DateUtils.truncate(course.getStopDate(), Calendar.DATE))),
                () -> assertThat(courses.get(0).get("class_times"), isJson(allOf(
                                withJsonPath("$[0].dayOfWeek", equalTo(course.getClassTimes().get(0).getDayOfWeek().toString())),
                                withJsonPath("$[0].startTime[0]", equalTo(course.getClassTimes().get(0).getStartTime().getHour())),
                                withJsonPath("$[0].startTime[1]", equalTo(course.getClassTimes().get(0).getStartTime().getMinute())),
                                withJsonPath("$[0].stopTime[0]", equalTo(course.getClassTimes().get(0).getStopTime().getHour())),
                                withJsonPath("$[0].stopTime[1]", equalTo(course.getClassTimes().get(0).getStopTime().getMinute()))
                        )
                )));
    }

    @Test
    void should_get_courses_successfully() throws JsonProcessingException {
        Faker faker = new Faker();
        Course course = Course.builder()
                .name(faker.educator().course())
                .startDate(new Date())
                .stopDate(DateUtils.addMonths(new Date(), 6))
                .classTimes(List.of(new ClassTime(DayOfWeek.MONDAY, LocalTime.of(9,0),LocalTime.of(10, 0))))
                .build();
        new SimpleJdbcInsert(jdbcTemplate).withTableName("course")
                .execute(new HashMap<>(){
                    {put("name", course.getName());}
                    {put("start_date", course.getStartDate());}
                    {put("stop_date", course.getStopDate());}
                    {put("class_times", HibernateObjectMapperHolder.get().writeValueAsString(course.getClassTimes()));}
                });

        List<Course> courses = courseDao.findAll();

        assertAll(
                () -> assertThat(courses.size(), equalTo(1)),
                () -> assertThat(courses.get(0).getId(), notNullValue()),
                () -> assertThat(courses.get(0).getName(), equalTo(course.getName())),
                () -> assertThat(courses.get(0).getStartDate().compareTo(DateUtils.truncate(course.getStartDate(), Calendar.DATE)), equalTo(0)),
                () -> assertThat(courses.get(0).getStopDate().compareTo(DateUtils.truncate(course.getStopDate(), Calendar.DATE)), equalTo(0)),
                () -> assertThat(courses.get(0).getClassTimes().get(0).getDayOfWeek(), equalTo(course.getClassTimes().get(0).getDayOfWeek())),
                () -> assertThat(courses.get(0).getClassTimes().get(0).getStartTime(), equalTo(course.getClassTimes().get(0).getStartTime())),
                () -> assertThat(courses.get(0).getClassTimes().get(0).getStopTime(), equalTo(course.getClassTimes().get(0).getStopTime())));
    }
}