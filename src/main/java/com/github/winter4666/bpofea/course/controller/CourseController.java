package com.github.winter4666.bpofea.course.controller;

import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    private final CourseMapper courseMapper;

    @GetMapping("/courses")
    public List<Course> getCourses() {
        return courseService.getCourses();
    }

    @PostMapping("/courses")
    public ResponseEntity<?> addCourse(@RequestBody CreateCourseRequest createCourseRequest) {
        courseService.addCourse(courseMapper.createCourseRequestToCourse(createCourseRequest));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public record CreateCourseRequest(String name, LocalDate startDate, LocalDate stopDate, List<ClassTime> classTimes) {

        public record ClassTime(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime stopTime){

        }

    }

}
