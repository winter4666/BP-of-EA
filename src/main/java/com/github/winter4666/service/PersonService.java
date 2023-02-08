package com.github.winter4666.service;

import com.github.winter4666.model.Person;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonDao personDao;

    public PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    public void addPerson(String name) {
        Person person = new Person();
        person.setName(name);
        personDao.insert(person);
    }

    public List<Person> getPersons() {
        return personDao.getPersons();
    }
}
