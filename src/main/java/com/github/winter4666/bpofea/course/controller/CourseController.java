package com.github.winter4666.bpofea.course.controller;

import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    private final CourseResponseMapper courseResponseMapper;

    private final CourseMapper courseMapper;

    @GetMapping()
    public List<Course> getCourses() {
        return courseService.getCourses();
    }

    @PatchMapping("/{courseId}")
    public CourseResponse updateCourse(@PathVariable long courseId, @RequestBody UpdateCourseRequest updateCourseRequest) {
        return courseResponseMapper.courseToCourseResponse(courseService.updateCourse(courseId, updateCourseRequest.startDate, updateCourseRequest.stopDate,
                courseMapper.classTimeDtoListToClassTimeList(updateCourseRequest.classTimes)));
    }

    record UpdateCourseRequest(LocalDate startDate, LocalDate stopDate, List<ClassTimeDto> classTimes) {

    }

}
