package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.user.domain.model.User;

import java.util.List;

public interface UserDao {

    User save(User user);

    List<User> findAll();

}
