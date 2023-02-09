package com.github.winter4666.dao;

import com.github.javafaker.Faker;
import com.github.winter4666.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Testcontainers
class RdbPersonDaoTest {

    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.30");

    @Autowired
    private RdbPersonDao rdbPersonDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

    @Test
    void should_insert_successfully() {
        Person person = new Person();
        person.setName(new Faker().name().fullName());

        int count = rdbPersonDao.insert(person);

        assertThat(count, equalTo(1));
        List<Person> persons = jdbcTemplate.query("select * from person", (rs, n) -> new Person(rs.getInt("id"), rs.getString("name")));
        assertThat(persons.size(), equalTo(1));
        assertThat(persons.get(0).getName(), equalTo(person.getName()));
    }

    @Test
    void should_get_persons_successfully() {
        String name = new Faker().name().fullName();
        jdbcTemplate.update("INSERT INTO person (name) VALUES (?)", name);

        List<Person> persons = rdbPersonDao.getPersons();

        assertThat(persons.size(), equalTo(1));
        assertThat(persons.get(0).getName(), equalTo(name));
    }
}