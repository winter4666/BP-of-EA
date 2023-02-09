package com.github.winter4666.user.dao;

import com.github.winter4666.user.domain.model.User;
import com.github.winter4666.user.domain.service.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RdbUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public RdbUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(User user) {
        return jdbcTemplate.update("INSERT INTO user (name) VALUES (?)", user.getName());
    }

    @Override
    public List<User> getPersons() {
        return jdbcTemplate.query("select * from user", (rs, n) -> new User(rs.getInt("id"), rs.getString("name")));
    }
}
