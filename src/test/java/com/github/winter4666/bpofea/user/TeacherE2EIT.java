package com.github.winter4666.bpofea.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.testsupport.RdbDaoTest;
import com.github.winter4666.bpofea.user.datafaker.TestTeacherBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TeacherE2EIT extends RdbDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_add_teacher_successfully() throws JsonProcessingException {
        Faker faker = new Faker();
        Map<String, Object> teacher = new HashMap<>() {
            {
                put("name", faker.name().fullName());
                put("jobNumber", String.valueOf(faker.number().randomNumber()));
            }
        };

        Response response = given().contentType(ContentType.JSON).body(objectMapper.writeValueAsString(teacher))
                .when().post("/teachers")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(teacher.get("name")))
                .body("jobNumber", equalTo(teacher.get("jobNumber")))
                .extract().response();

        List<Map<String, Object>> teachers = jdbcTemplate.queryForList("select * from teacher where id = ?", response.jsonPath().getLong("id"));
        assertAll(
                () -> assertThat(teachers.size(), equalTo(1)),
                () -> assertThat(teachers.get(0).get("name"), equalTo(teacher.get("name"))),
                () -> assertThat(teachers.get(0).get("job_number"), equalTo(teacher.get("jobNumber")))
        );
    }

    @Test
    void should_create_course_successfully() throws JsonProcessingException {
        Faker faker = new Faker();
        Map<String, Object> args = new HashMap<>(){
            {put("name", faker.educator().course());}
            {put("job_number", String.valueOf(faker.number().randomNumber()));}
        };
        long teacherId = new SimpleJdbcInsert(jdbcTemplate).withTableName("teacher")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(args).longValue();
        Map<String, Object> classTime = new HashMap<>() {
            {
                put("dayOfWeek", DayOfWeek.MONDAY);
                put("startTime", LocalTime.of(9, 0));
                put("stopTime", LocalTime.of(10, 0));
            }
        };
        Map<String, Object> course = new HashMap<>(){
            {
                put("name", faker.educator().course());
                put("startDate", LocalDate.of(2023, 1, 1));
                put("stopDate", LocalDate.of(2023, 5, 1));
                put("classTimes", List.of(classTime));
                put("capacity", faker.number().randomNumber());
            }
        };

        Response response = given().contentType(ContentType.JSON).body(objectMapper.writeValueAsString(course))
                .when().post("/teachers/{teacherId}/courses", teacherId)
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().response();

        Map<String, Object> courseInDb = jdbcTemplate.queryForMap("select * from course where id = ?", response.jsonPath().getLong("id"));
        assertAll(
                () -> assertThat(courseInDb.get("name"), equalTo(course.get("name"))),
                () -> assertThat(DateFormatUtils.format((Date) courseInDb.get("start_date"), "yyyy-MM-dd"), equalTo(course.get("startDate").toString())),
                () -> assertThat(DateFormatUtils.format((Date) courseInDb.get("stop_date"), "yyyy-MM-dd"), equalTo(course.get("stopDate").toString())),
                () -> assertThat(courseInDb.get("teacher_id"), equalTo(teacherId)),
                () -> assertThat(courseInDb.get("current_student_number"), equalTo(0L)),
                () -> assertThat(courseInDb.get("class_times"), isJson(allOf(
                                withJsonPath("$[0].dayOfWeek", equalTo(classTime.get("dayOfWeek").toString())),
                                withJsonPath("$[0].startTime[0]", equalTo(((LocalTime) classTime.get("startTime")).getHour())),
                                withJsonPath("$[0].startTime[1]", equalTo(((LocalTime) classTime.get("startTime")).getMinute())),
                                withJsonPath("$[0].stopTime[0]", equalTo(((LocalTime) classTime.get("stopTime")).getHour())),
                                withJsonPath("$[0].stopTime[1]", equalTo(((LocalTime) classTime.get("stopTime")).getMinute()))
                        )
                )));
    }

    @Test
    void should_remove_course_successfully() throws JsonProcessingException {
        Faker faker = new Faker();
        long teacherId = new SimpleJdbcInsert(jdbcTemplate).withTableName("teacher")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(new HashMap<>(){
                    {put("name", faker.educator().course());}
                    {put("job_number", String.valueOf(faker.number().randomNumber()));}
                }).longValue();
        long courseId = new SimpleJdbcInsert(jdbcTemplate).withTableName("course")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(new CourseBuilder().teacher(new TestTeacherBuilder().id(teacherId)).buildArgsForDbInsertion()).longValue();

        when().delete("/teachers/{teacherId}/courses/{courseId}", teacherId, courseId).then().statusCode(HttpStatus.NO_CONTENT.value());

        List<Map<String, Object>> courses = jdbcTemplate.queryForList("select * from course where id = ?", courseId);
        assertThat(courses, is(empty()));
    }

}
