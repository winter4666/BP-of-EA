package com.github.winter4666.bpofea.course.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.winter4666.bpofea.common.HibernateObjectMapperHolder;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.testsupport.RdbDaoTest;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    void should_insert_successfully() {
        Course course = new CourseBuilder().build();

        courseDao.save(course);

        List<Map<String, Object>> courses = jdbcTemplate.queryForList("select * from course");
        assertAll(
                () -> assertThat(courses.size(), equalTo(1)),
                () -> assertThat(courses.get(0).get("name"), equalTo(course.getName())),
                () -> assertThat(DateFormatUtils.format((Date)courses.get(0).get("start_date"), "yyyy-MM-dd"), equalTo(course.getStartDate().toString())),
                () -> assertThat(DateFormatUtils.format((Date)courses.get(0).get("stop_date"), "yyyy-MM-dd"), equalTo(course.getStopDate().toString())),
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
        Course course = new CourseBuilder().build();
        long courseId = new SimpleJdbcInsert(jdbcTemplate).withTableName("course").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(new HashMap<>(){
                    {put("name", course.getName());}
                    {put("start_date", course.getStartDate());}
                    {put("stop_date", course.getStopDate());}
                    {put("class_times", HibernateObjectMapperHolder.get().writeValueAsString(course.getClassTimes()));}
                    {put("teacher_id", course.getTeacher().getId());}
                }).longValue();

        List<Course> courses = courseDao.findAll();

        assertAll(
                () -> assertThat(courses.size(), equalTo(1)),
                () -> assertThat(courses.get(0).getId(), equalTo(courseId)),
                () -> assertThat(courses.get(0).getName(), equalTo(course.getName())),
                () -> assertThat(courses.get(0).getStartDate(), equalTo(course.getStartDate())),
                () -> assertThat(courses.get(0).getStopDate(), equalTo(course.getStopDate())),
                () -> assertThat(courses.get(0).getClassTimes().get(0).getDayOfWeek(), equalTo(course.getClassTimes().get(0).getDayOfWeek())),
                () -> assertThat(courses.get(0).getClassTimes().get(0).getStartTime(), equalTo(course.getClassTimes().get(0).getStartTime())),
                () -> assertThat(courses.get(0).getClassTimes().get(0).getStopTime(), equalTo(course.getClassTimes().get(0).getStopTime())));
    }

    @Test
    void should_get_course_by_id_successfully() throws JsonProcessingException {
        Course course = new CourseBuilder().build();
        long courseId = new SimpleJdbcInsert(jdbcTemplate).withTableName("course").usingGeneratedKeyColumns("id")
                .executeAndReturnKey(new HashMap<>(){
                    {put("name", course.getName());}
                    {put("start_date", course.getStartDate());}
                    {put("stop_date", course.getStopDate());}
                    {put("class_times", HibernateObjectMapperHolder.get().writeValueAsString(course.getClassTimes()));}
                    {put("teacher_id", course.getTeacher().getId());}
                }).longValue();

        Course courseInDb = courseDao.findById(courseId).orElse(null);

        assertAll(
                () -> assertThat(courseInDb, notNullValue()),
                () -> assertThat(courseInDb.getId(), equalTo(courseId)),
                () -> assertThat(courseInDb.getName(), equalTo(course.getName())),
                () -> assertThat(courseInDb.getStartDate(), equalTo(course.getStartDate())),
                () -> assertThat(courseInDb.getStopDate(), equalTo(course.getStopDate())),
                () -> assertThat(courseInDb.getClassTimes().get(0).getDayOfWeek(), equalTo(course.getClassTimes().get(0).getDayOfWeek())),
                () -> assertThat(courseInDb.getClassTimes().get(0).getStartTime(), equalTo(course.getClassTimes().get(0).getStartTime())),
                () -> assertThat(courseInDb.getClassTimes().get(0).getStopTime(), equalTo(course.getClassTimes().get(0).getStopTime())));
    }
}