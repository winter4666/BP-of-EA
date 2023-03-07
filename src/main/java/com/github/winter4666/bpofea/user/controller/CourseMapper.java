package com.github.winter4666.bpofea.user.controller;

import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    Course createCourseRequestToCourse(TeacherController.CreateCourseRequest createCourseRequest);

    ClassTime classTimeInRequestToClassTime(TeacherController.CreateCourseRequest.ClassTime classTime);

}
