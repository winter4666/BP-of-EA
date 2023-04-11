package com.github.winter4666.bpofea.user.dao;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.testsupport.MySQL.MySQLTestable;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;

class RdbTeacherDaoIT implements MySQLTestable {

    @Autowired
    private RdbTeacherDao teacherDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void should_insert_successfully() {
        Faker faker = new Faker();
        Teacher teacher = Teacher.builder()
                .name(faker.name().fullName())
                .jobNumber(String.valueOf(faker.number().randomNumber()))
                .build();

        teacherDao.save(teacher);

        List<Map<String, Object>> teachers = jdbcTemplate.queryForList("select * from teacher");
        assertAll(
                () -> assertThat(teachers.size(), equalTo(1)),
                () -> assertThat(teachers.get(0).get("name"), equalTo(teacher.getName())),
                () -> assertThat(teachers.get(0).get("job_number"), equalTo(teacher.getJobNumber()))
        );
    }

    @Test
    void should_get_teacher_successfully() {
        Faker faker = new Faker();
        Map<String, Object> args = new HashMap<>(){
            {put("name", faker.educator().course());}
            {put("job_number", String.valueOf(faker.number().randomNumber()));}
        };
        long teacherId = new SimpleJdbcInsert(jdbcTemplate).withTableName("teacher")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(args).longValue();

        Teacher teacher = teacherDao.findById(teacherId).orElse(null);
        assertThat(teacher, notNullValue());
        assertThat(teacher.getName(), equalTo(args.get("name")));
        assertThat(teacher.getJobNumber(), equalTo(args.get("job_number")));
    }
}