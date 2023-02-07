package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    public List<Person> getPersons() {
        return personService.getPersons();
    }

    @PostMapping("/persons")
    public ResponseEntity<?> addPerson(String name) {
        personService.addPerson(name);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
