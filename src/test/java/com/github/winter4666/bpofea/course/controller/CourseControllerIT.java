package com.github.winter4666.bpofea.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.model.Page;
import com.github.winter4666.bpofea.common.domain.model.PageOptions;
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

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        Faker faker = new Faker();
        int perPage = (int)faker.number().randomNumber();
        int page = (int)faker.number().randomNumber();
        CourseBuilder courseBuilder = new CourseBuilder().id(faker.random().nextLong());
        Course course = courseBuilder.build();
        long totalElements = faker.number().randomNumber();
        when(courseService.getCourses(course.getName(), new PageOptions(perPage, page))).thenReturn(new Page<>(List.of(course), totalElements));

        mvc.perform(get("/courses")
                        .queryParam("perPage", String.valueOf(perPage))
                        .queryParam("page", String.valueOf(page))
                        .queryParam("namePrefix", course.getName()))
                .andExpectAll(status().isOk(),
                        jsonPath("$.totalElements").value(totalElements),
                        jsonPath("$.content[0].id").value(course.getId()),
                        jsonPath("$.content[0].name").value(course.getName()),
                        jsonPath("$.content[0].startDate").value(course.getStartDate().toString()),
                        jsonPath("$.content[0].stopDate").value(course.getStopDate().toString()),
                        jsonPath("$.content[0].classTimes[0].dayOfWeek").value(course.getClassTimes().get(0).dayOfWeek().toString()),
                        jsonPath("$.content[0].classTimes[0].startTime").value(course.getClassTimes().get(0).startTime().format(DateTimeFormatter.ISO_TIME)),
                        jsonPath("$.content[0].classTimes[0].stopTime").value(course.getClassTimes().get(0).stopTime().format(DateTimeFormatter.ISO_TIME)),
                        jsonPath("$.content[0].capacity").value(course.getCapacity()),
                        jsonPath("$.content[0].currentStudentNumber").value(course.getCurrentStudentNumber()),
                        jsonPath("$.content[0].teacherId").value(course.getTeacher().getId()),
                        jsonPath("$.content[0].state").value(course.getState().toString()));

    }

    @Test
    void should_update_course_successfully() throws Exception {
        CourseBuilder courseBuilder = new CourseBuilder().id(new Faker().random().nextLong());
        Course course = courseBuilder.build();
        Map<String, Object> updateCourseRequest = new HashMap<>(){{
            put("startDate", course.getStartDate());
            put("stopDate", course.getStopDate());
            put("classTimes", course.getClassTimes());
        }};
        when(courseService.updateCourse(eq(course.getId()), eq(course.getStartDate()), eq(course.getStopDate()), argThat(times -> times.get(0).equals(course.getClassTimes().get(0)))))
                .thenReturn(course);

        mvc.perform(patch("/courses/{courseId}", course.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateCourseRequest)))
                .andExpectAll(status().isOk(),
                        content().json(objectMapper.writeValueAsString(courseBuilder.buildMapForResponse())));
    }

}