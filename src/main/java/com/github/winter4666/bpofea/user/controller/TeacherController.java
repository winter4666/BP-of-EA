package com.github.winter4666.bpofea.user.controller;

import com.github.winter4666.bpofea.course.controller.ClassTimeDto;
import com.github.winter4666.bpofea.course.controller.CourseMapper;
import com.github.winter4666.bpofea.course.controller.CourseResponse;
import com.github.winter4666.bpofea.course.controller.CourseResponseMapper;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import com.github.winter4666.bpofea.user.domain.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    private final CourseMapper courseMapper;

    private final CourseResponseMapper courseResponseMapper;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Teacher addTeacher(@RequestBody CreateTeacherRequest createTeacherRequest) {
        return teacherService.addTeacher(createTeacherRequest.name(), createTeacherRequest.jobNumber());
    }

    record CreateTeacherRequest(String name, String jobNumber) {
    }


    @PostMapping("/{teacherId}/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse startCourse(@PathVariable Long teacherId, @RequestBody CreateCourseRequest createCourseRequest) {
        return courseResponseMapper.courseToCourseResponse(teacherService.startCourse(teacherId, courseMapper.createCourseRequestToCourse(createCourseRequest)));
    }

    public record CreateCourseRequest(String name, LocalDate startDate, LocalDate stopDate, List<ClassTimeDto> classTimes) {

    }

}
