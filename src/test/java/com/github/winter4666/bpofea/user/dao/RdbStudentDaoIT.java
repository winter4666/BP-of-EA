package com.github.winter4666.bpofea.user.dao;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.testsupport.RdbDaoTest;
import com.github.winter4666.bpofea.user.domain.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class RdbStudentDaoIT extends RdbDaoTest {

    @Autowired
    private RdbStudentDao studentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void should_get_teacher_successfully() {
        Faker faker = new Faker();
        Map<String, Object> args = new HashMap<>(){
            {put("name", faker.educator().course());}
            {put("student_number", String.valueOf(faker.number().randomNumber()));}
        };
        long studentId = new SimpleJdbcInsert(jdbcTemplate).withTableName("student")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(args).longValue();

        Student student = studentDao.findById(studentId).orElse(null);
        assertThat(student, notNullValue());
        assertThat(student.getName(), equalTo(args.get("name")));
        assertThat(student.getStudentNumber(), equalTo(args.get("student_number")));
    }
}