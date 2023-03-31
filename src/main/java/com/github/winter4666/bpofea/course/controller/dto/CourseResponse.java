package com.github.winter4666.bpofea.course.controller.dto;

import java.time.LocalDate;
import java.util.List;

public record CourseResponse(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate stopDate,
        List<ClassTimeDto> classTimes,
        Long capacity,
        Long teacherId,
        Long currentStudentNumber) {
}