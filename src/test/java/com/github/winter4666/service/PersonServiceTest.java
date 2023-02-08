package com.github.winter4666.service;

import com.github.javafaker.Faker;
import com.github.winter4666.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonDao personDao;

    @InjectMocks
    private PersonService personService;

    @Test
    void should_add_person_successfully() {
        String name = new Faker().name().fullName();

        personService.addPerson(name);

        verify(personDao).insert(ArgumentMatchers.argThat(person -> name.equals(person.getName())));
    }

    @Test
    void should_get_persons_successfully() {
        List<Person> persons = new ArrayList<>();
        when(personDao.getPersons()).thenReturn(persons);

        List<Person> actualPersons = personService.getPersons();

        assertThat(actualPersons, equalTo(persons));
    }
}