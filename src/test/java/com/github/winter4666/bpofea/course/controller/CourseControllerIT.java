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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@ComponentScan
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

    @Test
    void should_update_course_successfully() throws Exception {
        Course course = new CourseBuilder().id(new Faker().random().nextLong()).build();
        Map<String, Object> updateCourseRequest = new HashMap<>(){{
            put("startDate", course.getStartDate());
            put("stopDate", course.getStopDate());
            put("classTimes", course.getClassTimes());
        }};
        when(courseService.updateCourse(eq(course.getId()), eq(course.getStartDate()), eq(course.getStopDate()), argThat(times -> times.get(0).equals(course.getClassTimes().get(0)))))
                .thenReturn(course);

        mvc.perform(patch("/courses/{courseId}", course.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateCourseRequest)))
                .andExpectAll(status().isOk(),
                        jsonPath("$.id", equalTo(course.getId()), Long.class),
                        jsonPath("$.name", equalTo(course.getName())),
                        jsonPath("$.startDate", equalTo(course.getStartDate().toString())),
                        jsonPath("$.stopDate", equalTo(course.getStopDate().toString())),
                        jsonPath("$.classTimes[0].dayOfWeek", equalTo(course.getClassTimes().get(0).getDayOfWeek().toString())),
                        jsonPath("$.classTimes[0].startTime", equalTo(course.getClassTimes().get(0).getStartTime().format(DateTimeFormatter.ISO_TIME))),
                        jsonPath("$.classTimes[0].stopTime", equalTo(course.getClassTimes().get(0).getStopTime().format(DateTimeFormatter.ISO_TIME))));
    }

}