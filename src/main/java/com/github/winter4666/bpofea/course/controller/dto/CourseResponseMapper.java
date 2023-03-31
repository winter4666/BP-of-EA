package com.github.winter4666.bpofea.course.controller.dto;

import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseResponseMapper {

    @Mapping(source = "teacher.id", target = "teacherId")
    CourseResponse courseToCourseResponse(Course course);

    ClassTimeDto classTimeToClassTimeDto(ClassTime classTime);

}
