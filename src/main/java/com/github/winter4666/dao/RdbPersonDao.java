package com.github.winter4666.dao;

import com.github.winter4666.model.Person;
import com.github.winter4666.service.PersonDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RdbPersonDao implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    public RdbPersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(Person person) {
        return jdbcTemplate.update("INSERT INTO person (name) VALUES (?)", person.getName());
    }

    @Override
    public List<Person> getPersons() {
        return jdbcTemplate.query("select * from person", (rs, n) -> new Person(rs.getInt("id"), rs.getString("name")));
    }
}
