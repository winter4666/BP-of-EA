package com.github.winter4666.bpofea.user.dao;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.testbase.RdbDaoTest;
import com.github.winter4666.bpofea.user.domain.model.User;
import com.github.winter4666.bpofea.user.domain.service.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class UserRepositoryIT extends RdbDaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void should_insert_successfully() {
        User user = new User(new Faker().name().fullName());

        userDao.save(user);

        List<User> users = jdbcTemplate.query("select * from user", (rs, n) -> new User(rs.getString("name")));
        assertThat(users.size(), equalTo(1));
        assertThat(users.get(0).getName(), equalTo(user.getName()));
    }

    @Test
    void should_get_users_successfully() {
        String name = new Faker().name().fullName();
        new SimpleJdbcInsert(jdbcTemplate).withTableName("user")
                        .execute(new HashMap<>(){
                            {put("name", name);}
                        });

        List<User> users = userDao.findAll();

        assertThat(users.size(), equalTo(1));
        assertThat(users.get(0).getName(), equalTo(name));
    }
}