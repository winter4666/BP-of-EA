package com.github.winter4666.bpofea.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.testbase.RdbDaoTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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

        given().contentType(ContentType.JSON).body(objectMapper.writeValueAsString(teacher))
                .when().post("/teachers")
                .then().statusCode(HttpStatus.CREATED.value());

        List<Map<String, Object>> teachers = jdbcTemplate.queryForList("select * from teacher");
        assertAll(
                () -> assertThat(teachers.size(), equalTo(1)),
                () -> assertThat(teachers.get(0).get("name"), equalTo(teacher.get("name"))),
                () -> assertThat(teachers.get(0).get("job_number"), equalTo(teacher.get("jobNumber")))
        );
    }

}
