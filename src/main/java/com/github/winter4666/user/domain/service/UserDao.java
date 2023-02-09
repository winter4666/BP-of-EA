package com.github.winter4666.user.domain.service;

import com.github.winter4666.user.domain.model.User;

import java.util.List;

public interface UserDao {

    int insert(User user);

    List<User> getPersons();

}
