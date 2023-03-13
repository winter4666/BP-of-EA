package com.github.winter4666.bpofea.common.springmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.exception.BusinessException;
import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.user.controller.TeacherController;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import com.github.winter4666.bpofea.user.domain.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
@ComponentScan("com.github.winter4666.bpofea.user.controller")
class GlobalExceptionHandlerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_500_status_code_when_call_restful_api_given_exception_occurred() throws Exception {
        Faker faker = new Faker();
        Teacher teacher = Teacher.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().fullName())
                .jobNumber(String.valueOf(faker.number().randomNumber()))
                .build();
        when(teacherService.addTeacher(teacher.getName(), teacher.getJobNumber())).thenThrow(new RuntimeException("Some exception occurred"));

        mvc.perform(post("/teachers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(teacher)))
                .andExpectAll(status().isInternalServerError());
    }

    @Test
    void should_return_500_status_code_and_error_message_when_call_restful_api_given_business_exception_exception_occurred() throws Exception {
        Faker faker = new Faker();
        Teacher teacher = Teacher.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().fullName())
                .jobNumber(String.valueOf(faker.number().randomNumber()))
                .build();
        String errorMessage = "Some business error occurred";
        when(teacherService.addTeacher(teacher.getName(), teacher.getJobNumber())).thenThrow(new BusinessException(errorMessage));

        MvcResult mvcResult = mvc.perform(post("/teachers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(teacher)))
                .andExpectAll(status().isInternalServerError()).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(errorMessage));
    }

    @Test
    void should_return_404_status_code_and_error_message_when_call_restful_api_given_data_not_found_exception_occurred() throws Exception {
        Faker faker = new Faker();
        Teacher teacher = Teacher.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().fullName())
                .jobNumber(String.valueOf(faker.number().randomNumber()))
                .build();
        String errorMessage = "Something not found";
        when(teacherService.addTeacher(teacher.getName(), teacher.getJobNumber())).thenThrow(new DataNotFoundException(errorMessage));

        MvcResult mvcResult = mvc.perform(post("/teachers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(teacher)))
                .andExpectAll(status().isNotFound()).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(errorMessage));
    }
}