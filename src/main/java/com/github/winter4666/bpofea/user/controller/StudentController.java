package com.github.winter4666.bpofea.user.controller;

import com.github.winter4666.bpofea.user.domain.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/{studentId}/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public void chooseCourse(@PathVariable long studentId, @RequestBody ChooseCourseRequest chooseCourseRequest) {
        studentService.chooseCourse(studentId, chooseCourseRequest.id());
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeChoice(@PathVariable long studentId, @PathVariable long courseId) {
        studentService.revokeChoice(studentId, courseId);
    }

    record ChooseCourseRequest(Long id) {
    }

}
