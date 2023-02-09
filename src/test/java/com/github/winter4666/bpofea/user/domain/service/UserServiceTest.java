package com.github.winter4666.bpofea.user.domain.service;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.user.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    void should_add_user_successfully() {
        String name = new Faker().name().fullName();

        userService.addUser(name);

        verify(userDao).insert(argThat(p -> name.equals(p.getName())));
    }

    @Test
    void should_get_users_successfully() {
        List<User> users = new ArrayList<>();
        when(userDao.getUsers()).thenReturn(users);

        List<User> actualUsers = userService.getUsers();

        assertThat(actualUsers, equalTo(users));
    }
}