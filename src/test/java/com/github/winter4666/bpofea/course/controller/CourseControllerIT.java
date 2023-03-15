package com.github.winter4666.bpofea.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
class CourseControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void should_get_courses_successfully() throws Exception {
        Course course = new CourseBuilder().id(new Faker().random().nextLong()).build();
        when(courseService.getCourses()).thenReturn(List.of(course));

        mvc.perform(get("/courses"))
                .andExpectAll(status().isOk(),
                        jsonPath("$[0].id").value(course.getId()),
                        jsonPath("$[0].name").value(course.getName()),
                        jsonPath("$[0].startDate").value(course.getStartDate().toString()),
                        jsonPath("$[0].stopDate").value(course.getStopDate().toString()),
                        jsonPath("$[0].classTimes[0].dayOfWeek").value(course.getClassTimes().get(0).getDayOfWeek().toString()),
                        jsonPath("$[0].classTimes[0].startTime").value(course.getClassTimes().get(0).getStartTime().format(DateTimeFormatter.ISO_TIME)),
                        jsonPath("$[0].classTimes[0].stopTime").value(course.getClassTimes().get(0).getStopTime().format(DateTimeFormatter.ISO_TIME)));
    }

}