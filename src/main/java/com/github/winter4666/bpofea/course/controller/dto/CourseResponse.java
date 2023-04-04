package com.github.winter4666.bpofea.course.controller.dto;

import com.github.winter4666.bpofea.course.domain.model.ClassTime;

import java.time.LocalDate;
import java.util.List;

public record CourseResponse(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate stopDate,
        List<ClassTime> classTimes,
        Long capacity,
        Long teacherId,
        Long currentStudentNumber) {
}