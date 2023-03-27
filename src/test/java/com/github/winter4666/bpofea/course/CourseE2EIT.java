package com.github.winter4666.bpofea.course;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.dao.HibernateObjectMapperHolder;
import com.github.winter4666.bpofea.testsupport.RdbDaoTest;
import io.restassured.http.ContentType;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CourseE2EIT extends RdbDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_update_course_successfully() throws JsonProcessingException {
        Faker faker = new Faker();
        Map<String, Object> classTime = new HashMap<>() {
            {
                put("dayOfWeek", DayOfWeek.MONDAY);
                put("startTime", LocalTime.of(9, 0));
                put("stopTime", LocalTime.of(10, 0));
            }
        };
        long courseId = new SimpleJdbcInsert(jdbcTemplate).withTableName("course")
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
        Map<String, Object> updateCourseRequest = new HashMap<>(){{
            put("startDate", LocalDate.of(2023,2,1));
        }};

        given().contentType(ContentType.JSON).body(objectMapper.writeValueAsString(updateCourseRequest))
                .when().patch("/courses/{courseId}", courseId)
                .then().statusCode(HttpStatus.OK.value());

        List<Map<String, Object>> courses = jdbcTemplate.queryForList("select * from course where id = ?", courseId);
        assertAll(
                () -> assertThat(DateFormatUtils.format((Date) courses.get(0).get("start_date"), "yyyy-MM-dd"), equalTo(updateCourseRequest.get("startDate").toString())));
    }

}
