package com.github.winter4666.bpofea.user.controller;

import com.github.winter4666.bpofea.course.controller.dto.CourseResponse;
import com.github.winter4666.bpofea.course.controller.dto.CourseResponseMapper;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import com.github.winter4666.bpofea.user.domain.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

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
    public CourseResponse createCourse(@PathVariable Long teacherId, @RequestBody TeacherService.CreateCourseRequest createCourseRequest) {
        return courseResponseMapper.courseToCourseResponse(teacherService.createCourse(teacherId, createCourseRequest));
    }

    @DeleteMapping("/{teacherId}/courses/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCourse(@PathVariable long teacherId, @PathVariable long courseId) {
        teacherService.removeCourse(teacherId, courseId);
    }


}
