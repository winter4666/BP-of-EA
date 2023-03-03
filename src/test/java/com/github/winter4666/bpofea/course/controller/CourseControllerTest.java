package com.github.winter4666.bpofea.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@ComponentScan
class CourseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void should_get_courses_successfully() throws Exception {
        Faker faker = new Faker();
        Course course = Course.builder()
                .id(faker.random().nextLong())
                .name(faker.educator().course())
                .startDate(LocalDate.of(2023, 1, 1))
                .stopDate(LocalDate.of(2023, 5, 1))
                .classTimes(List.of(new ClassTime(DayOfWeek.MONDAY, LocalTime.of(9,0),LocalTime.of(10, 0))))
                .build();
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
    void should_add_course_successfully() throws Exception {
        Map<String, Object> classTime = new HashMap<>() {
            {
                put("dayOfWeek", DayOfWeek.MONDAY);
                put("startTime", LocalTime.of(9, 0));
                put("stopTime", LocalTime.of(10, 0));
            }
        };
        Map<String, Object> course = new HashMap<>(){
            {
                put("name", new Faker().educator().course());
                put("startDate", LocalDate.of(2023, 1, 1));
                put("stopDate", LocalDate.of(2023, 5, 1));
                put("classTimes", List.of(classTime));
            }
        };

        mvc.perform(post("/courses").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isCreated());

        verify(courseService).addCourse(argThat(c -> course.get("name").equals(c.getName())
                && course.get("startDate").equals(c.getStartDate())
                && course.get("stopDate").equals(c.getStopDate())
                && classTime.get("dayOfWeek").equals(c.getClassTimes().get(0).getDayOfWeek())
                && classTime.get("startTime").equals(c.getClassTimes().get(0).getStartTime())
                && classTime.get("stopTime").equals(c.getClassTimes().get(0).getStopTime())
        ));
    }

}