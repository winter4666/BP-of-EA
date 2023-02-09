package com.github.winter4666.user.domain.service;

import com.github.winter4666.user.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(String name) {
        User user = new User();
        user.setName(name);
        userDao.insert(user);
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }
}
