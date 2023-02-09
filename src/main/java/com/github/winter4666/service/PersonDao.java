package com.github.winter4666.service;

import com.github.winter4666.model.Person;

import java.util.List;

public interface PersonDao {

    int insert(Person person);

    List<Person> getPersons();

}
