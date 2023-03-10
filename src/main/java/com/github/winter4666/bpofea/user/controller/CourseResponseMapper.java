package com.github.winter4666.bpofea.user.controller;

import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseResponseMapper {

    TeacherController.CourseResponse courseToCourseResponse(Course course);

    TeacherController.ClassTime classTimeToClassTimeRecord(ClassTime classTime);

}
