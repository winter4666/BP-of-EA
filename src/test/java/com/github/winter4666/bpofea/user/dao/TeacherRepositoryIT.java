package com.github.winter4666.bpofea.user.dao;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.testbase.RdbDaoTest;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import com.github.winter4666.bpofea.user.domain.service.TeacherDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

class TeacherRepositoryIT extends RdbDaoTest {

    @Autowired
    private TeacherDao teacherDao;

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
}