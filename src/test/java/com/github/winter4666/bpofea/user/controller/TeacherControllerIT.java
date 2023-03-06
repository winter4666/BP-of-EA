package com.github.winter4666.bpofea.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.user.domain.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
class TeacherControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_add_user_successfully() throws Exception {
        Faker faker = new Faker();
        Map<String, Object> teacher = new HashMap<>() {
            {
                put("name", faker.name().fullName());
                put("jobNumber", String.valueOf(faker.number().randomNumber()));
            }
        };

        mvc.perform(post("/teachers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isCreated());

        verify(teacherService).addTeacher((String)teacher.get("name"), (String)teacher.get("jobNumber"));
    }
}