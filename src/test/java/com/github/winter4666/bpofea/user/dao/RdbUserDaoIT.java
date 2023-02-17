package com.github.winter4666.bpofea.user.dao;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.testbase.RdbDaoTest;
import com.github.winter4666.bpofea.user.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class RdbUserDaoIT extends RdbDaoTest {

    @Autowired
    private RdbUserDao rdbUserDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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