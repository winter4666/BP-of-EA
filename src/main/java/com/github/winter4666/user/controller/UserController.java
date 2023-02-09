package com.github.winter4666.user.controller;

import com.github.winter4666.user.domain.model.User;
import com.github.winter4666.user.domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getPersons() {
        return userService.getUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<?> addPerson(String name) {
        userService.addUser(name);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
