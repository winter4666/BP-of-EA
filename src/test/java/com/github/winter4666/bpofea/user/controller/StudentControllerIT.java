package com.github.winter4666.bpofea.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.datafaker.TestCourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.user.domain.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_choose_course_successfully() throws Exception {
        Faker faker = new Faker();
        long studentId = faker.number().randomNumber();
        Course course = new TestCourseBuilder().id(new Faker().random().nextLong()).build();

        mvc.perform(post("/students/{studentId}/courses", studentId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(course)))
                .andExpectAll(status().isCreated(), jsonPath("$.id", equalTo(course.getId())));

        verify(studentService).chooseCourse(studentId, course.getId());
    }

    @Test
    void should_revoke_choice_successfully() throws Exception {
        Faker faker = new Faker();
        long studentId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();

        mvc.perform(delete("/students/{studentId}/courses/{courseId}", studentId, courseId))
                .andExpectAll(status().isNoContent());

        verify(studentService).revokeChoice(studentId, courseId);
    }
}