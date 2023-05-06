package com.github.winter4666.bpofea.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.controller.dto.CourseResponseMapperImpl;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.user.domain.model.Gender;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import com.github.winter4666.bpofea.user.domain.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
@Import({CourseResponseMapperImpl.class})
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
    void should_create_course_successfully() throws Exception {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        CourseBuilder courseBuilder = new CourseBuilder().id(new Faker().random().nextLong());

        when(teacherService.createCourse(teacherId,
                courseBuilder.buildCreateCourseRequest()))
                .thenReturn(courseBuilder.build());

        mvc.perform(post("/teachers/{teacherId}/courses", teacherId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(courseBuilder.buildCreateCourseRequest())))
                .andExpectAll(status().isCreated(),
                        content().json(objectMapper.writeValueAsString(courseBuilder.buildMapForResponse())));
    }

    @Test
    void should_remove_course_successfully() throws Exception {
        Faker faker = new Faker();
        long teacherId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();

        mvc.perform(delete("/teachers/{teacherId}/courses/{courseId}", teacherId, courseId))
                .andExpectAll(status().isNoContent());

        verify(teacherService).removeCourse(teacherId, courseId);
    }

    @Test
    void should_get_teacher_successfully() throws Exception {
        Faker faker = new Faker();
        TeacherService.TeacherInfo teacherInfo = new TeacherService.TeacherInfo(
          faker.number().randomNumber(), faker.name().name(), String.valueOf(faker.number().randomNumber()), Gender.MAN
        );
        when(teacherService.getTeacherInfo(teacherInfo.id())).thenReturn(teacherInfo);

        mvc.perform(get("/teachers/{id}", teacherInfo.id()))
                .andExpectAll(status().isOk(),
                        jsonPath("$.id").value(teacherInfo.id()),
                        jsonPath("$.name").value(teacherInfo.name()),
                        jsonPath("$.jobNumber").value(teacherInfo.jobNumber()),
                        jsonPath("$.gender").value(teacherInfo.gender().toString()));
    }
}