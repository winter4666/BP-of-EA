package com.example.demo.dao;

import com.example.demo.model.Person;
import com.example.demo.service.PersonDao;
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
    public void insert(Person person) {
        jdbcTemplate.update("INSERT INTO person (name) VALUES (?)", person.getName());
    }

    @Override
    public List<Person> getPersons() {
        return jdbcTemplate.query("select * from person", (rs, n) -> new Person(rs.getInt("id"), rs.getString("name")));
    }
}
