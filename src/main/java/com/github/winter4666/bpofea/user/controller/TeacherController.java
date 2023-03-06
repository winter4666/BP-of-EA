package com.github.winter4666.bpofea.user.controller;

import com.github.winter4666.bpofea.user.domain.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/teachers")
    public ResponseEntity<?> addTeacher(@RequestBody CreateTeacherRequest createTeacherRequest) {
        teacherService.addTeacher(createTeacherRequest.name(), createTeacherRequest.jobNumber());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public record CreateTeacherRequest(String name, String jobNumber) {
    }

}
