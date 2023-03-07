package com.github.winter4666.bpofea.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.user.domain.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
@ComponentScan
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

    @Test
    void should_start_course_successfully() throws Exception {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        Map<String, Object> classTime = new HashMap<>() {
            {
                put("dayOfWeek", DayOfWeek.MONDAY);
                put("startTime", LocalTime.of(9, 0));
                put("stopTime", LocalTime.of(10, 0));
            }
        };
        Map<String, Object> course = new HashMap<>(){
            {
                put("name", faker.educator().course());
                put("startDate", LocalDate.of(2023, 1, 1));
                put("stopDate", LocalDate.of(2023, 5, 1));
                put("classTimes", List.of(classTime));
            }
        };

        mvc.perform(post("/teachers/{teacherId}/courses", teacherId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isCreated());

        verify(teacherService).startCourse(eq(teacherId), argThat(c -> course.get("name").equals(c.getName())
                && course.get("startDate").equals(c.getStartDate())
                && course.get("stopDate").equals(c.getStopDate())
                && classTime.get("dayOfWeek").equals(c.getClassTimes().get(0).getDayOfWeek())
                && classTime.get("startTime").equals(c.getClassTimes().get(0).getStartTime())
                && classTime.get("stopTime").equals(c.getClassTimes().get(0).getStopTime())
        ));
    }
}