package com.github.winter4666.bpofea.user.controller;

import com.github.winter4666.bpofea.user.domain.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    private final CourseMapper courseMapper;

    @PostMapping("/teachers")
    public ResponseEntity<?> addTeacher(@RequestBody CreateTeacherRequest createTeacherRequest) {
        teacherService.addTeacher(createTeacherRequest.name(), createTeacherRequest.jobNumber());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/teachers/{teacherId}/courses")
    public ResponseEntity<?> startCourse(@PathVariable Long teacherId, @RequestBody CreateCourseRequest createCourseRequest) {
        teacherService.startCourse(teacherId, courseMapper.createCourseRequestToCourse(createCourseRequest));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public record CreateTeacherRequest(String name, String jobNumber) {
    }

    public record CreateCourseRequest(String name, LocalDate startDate, LocalDate stopDate, List<ClassTime> classTimes) {

        public record ClassTime(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime stopTime){

        }

    }

}
