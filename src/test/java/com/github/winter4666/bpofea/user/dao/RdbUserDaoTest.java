package com.github.winter4666.bpofea.user.dao;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.user.domain.model.User;
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
class RdbUserDaoTest {

    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.30");

    @Autowired
    private RdbUserDao rdbUserDao;

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
        User user = new User();
        user.setName(new Faker().name().fullName());

        int count = rdbUserDao.insert(user);

        assertThat(count, equalTo(1));
        List<User> users = jdbcTemplate.query("select * from user", (rs, n) -> new User(rs.getInt("id"), rs.getString("name")));
        assertThat(users.size(), equalTo(1));
        assertThat(users.get(0).getName(), equalTo(user.getName()));
    }

    @Test
    void should_get_users_successfully() {
        String name = new Faker().name().fullName();
        jdbcTemplate.update("INSERT INTO user (name) VALUES (?)", name);

        List<User> users = rdbUserDao.getUsers();

        assertThat(users.size(), equalTo(1));
        assertThat(users.get(0).getName(), equalTo(name));
    }
}