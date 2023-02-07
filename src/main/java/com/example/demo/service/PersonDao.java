package com.example.demo.service;

import com.example.demo.model.Person;

import java.util.List;

public interface PersonDao {

    void insert(Person person);

    List<Person> getPersons();

}
