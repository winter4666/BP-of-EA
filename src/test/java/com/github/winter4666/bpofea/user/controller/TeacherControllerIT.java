package com.github.winter4666.bpofea.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void should_add_teacher_successfully() throws Exception {
        Faker faker = new Faker();
        Teacher teacher = Teacher.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().fullName())
                .jobNumber(String.valueOf(faker.number().randomNumber()))
                .build();
        when(teacherService.addTeacher(teacher.getName(), teacher.getJobNumber())).thenReturn(teacher);

        mvc.perform(post("/teachers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(teacher)))
                .andExpectAll(status().isCreated(),
                        jsonPath("$.id", equalTo(teacher.getId()), Long.class),
                        jsonPath("$.name", equalTo(teacher.getName())),
                        jsonPath("$.jobNumber", equalTo(teacher.getJobNumber())));
    }

    @Test
    void should_start_course_successfully() throws Exception {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        Course course = Course.builder()
                .id(faker.number().randomNumber())
                .name(faker.educator().course())
                .startDate(LocalDate.of(2023, 1, 1))
                .stopDate(LocalDate.of(2023, 5, 1))
                .classTimes(List.of(new ClassTime(DayOfWeek.MONDAY, LocalTime.of(9,0),LocalTime.of(10, 0))))
                .build();

        when(teacherService.startCourse(eq(teacherId), argThat(c -> course.getName().equals(c.getName())
                && course.getStartDate().equals(c.getStartDate())
                && course.getStopDate().equals(c.getStopDate())
                && course.getClassTimes().get(0).getDayOfWeek().equals(c.getClassTimes().get(0).getDayOfWeek())
                && course.getClassTimes().get(0).getStartTime().equals(c.getClassTimes().get(0).getStartTime())
                && course.getClassTimes().get(0).getStopTime().equals(c.getClassTimes().get(0).getStopTime())
        ))).thenReturn(course);

        mvc.perform(post("/teachers/{teacherId}/courses", teacherId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(course)))
                .andExpectAll(status().isCreated(),
                        jsonPath("$.id", equalTo(course.getId()), Long.class),
                        jsonPath("$.name", equalTo(course.getName())),
                        jsonPath("$.startDate", equalTo(course.getStartDate().toString())),
                        jsonPath("$.stopDate", equalTo(course.getStopDate().toString())),
                        jsonPath("$.classTimes[0].dayOfWeek", equalTo(course.getClassTimes().get(0).getDayOfWeek().toString())),
                        jsonPath("$.classTimes[0].startTime", equalTo(course.getClassTimes().get(0).getStartTime().format(DateTimeFormatter.ISO_TIME))),
                        jsonPath("$.classTimes[0].stopTime", equalTo(course.getClassTimes().get(0).getStopTime().format(DateTimeFormatter.ISO_TIME))));
    }
}