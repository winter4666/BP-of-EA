package com.github.winter4666.user.controller;

import com.github.javafaker.Faker;
import com.github.winter4666.user.domain.model.User;
import com.github.winter4666.user.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void should_get_users_successfully() throws Exception {
        User user = new User();
        user.setName(new Faker().name().fullName());
        when(userService.getUsers()).thenReturn(List.of(user));

        mvc.perform(get("/users"))
                .andExpectAll(status().isOk(),
                        jsonPath("$[0].name").value(user.getName()));
    }

    @Test
    void should_add_user_successfully() throws Exception {
        String name = new Faker().name().fullName();

        mvc.perform(post("/users").param("name", name))
                .andExpect(status().isCreated());
        verify(userService).addUser(name);
    }
}