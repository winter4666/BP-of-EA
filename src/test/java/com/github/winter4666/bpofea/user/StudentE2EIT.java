package com.github.winter4666.bpofea.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.dao.HibernateObjectMapperHolder;
import com.github.winter4666.bpofea.testsupport.RdbDaoTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StudentE2EIT extends RdbDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_choose_course_successfully() throws JsonProcessingException {
        Faker faker = new Faker();
        long studentId = new SimpleJdbcInsert(jdbcTemplate).withTableName("student")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(new HashMap<>(){
                    {put("name", faker.educator().course());}
                    {put("student_number", String.valueOf(faker.number().randomNumber()));}
                }).longValue();
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
                put("start_date", LocalDate.of(2023, 1, 1));
                put("stop_date", LocalDate.of(2023, 5, 1));
                put("class_times", HibernateObjectMapperHolder.get().writeValueAsString(List.of(classTime)));
                put("teacher_id", faker.number().randomNumber());
            }
        };
        long courseId = new SimpleJdbcInsert(jdbcTemplate).withTableName("course")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(course).longValue();

        given().contentType(ContentType.JSON).body(objectMapper.writeValueAsString(new HashMap<>(){
                    {
                        put("id", courseId);
                    }
                }))
                .when().post("/students/{studentId}/courses", studentId)
                .then().statusCode(HttpStatus.CREATED.value());

        List<Map<String, Object>> studentCourses = jdbcTemplate.queryForList("select * from student_course where student_id = ? and course_id = ?", studentId, courseId);
        assertThat(studentCourses, is(not(empty())));
    }

    @Test
    void should_revoke_choice_successfully() throws JsonProcessingException {
        Faker faker = new Faker();
        long studentId = new SimpleJdbcInsert(jdbcTemplate).withTableName("student")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(new HashMap<>(){
                    {put("name", faker.educator().course());}
                    {put("student_number", String.valueOf(faker.number().randomNumber()));}
                }).longValue();
        Map<String, Object> classTime = new HashMap<>() {
            {
                put("dayOfWeek", DayOfWeek.MONDAY);
                put("startTime", LocalTime.of(9, 0));
                put("stopTime", LocalTime.of(10, 0));
            }
        };
        long courseId1 = new SimpleJdbcInsert(jdbcTemplate).withTableName("course")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(new HashMap<>(){
                    {
                        put("name", faker.educator().course());
                        put("start_date", LocalDate.of(2023, 1, 1));
                        put("stop_date", LocalDate.of(2023, 5, 1));
                        put("class_times", HibernateObjectMapperHolder.get().writeValueAsString(List.of(classTime)));
                        put("teacher_id", faker.number().randomNumber());
                    }
                }).longValue();
        long courseId2 = new SimpleJdbcInsert(jdbcTemplate).withTableName("course")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(new HashMap<>(){
                    {
                        put("name", faker.educator().course());
                        put("start_date", LocalDate.of(2023, 9, 1));
                        put("stop_date", LocalDate.of(2023, 2, 1));
                        put("class_times", HibernateObjectMapperHolder.get().writeValueAsString(List.of(classTime)));
                        put("teacher_id", faker.number().randomNumber());
                    }
                }).longValue();
        new SimpleJdbcInsert(jdbcTemplate).withTableName("student_course")
                .execute(new HashMap<>(){
                    {put("student_id", studentId);}
                    {put("course_id", courseId1);}
                });
        new SimpleJdbcInsert(jdbcTemplate).withTableName("student_course")
                .execute(new HashMap<>(){
                    {put("student_id", studentId);}
                    {put("course_id", courseId2);}
                });


        when().delete("/students/{studentId}/courses/{courseId}", studentId, courseId1).then().statusCode(HttpStatus.NO_CONTENT.value());

        List<Map<String, Object>> studentCourses = jdbcTemplate.queryForList("select * from student_course where student_id = ?", studentId);
        assertThat(studentCourses.size(), is(equalTo(1)));
    }

}
