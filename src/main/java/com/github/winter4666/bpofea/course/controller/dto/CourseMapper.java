package com.github.winter4666.bpofea.course.controller.dto;

import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.user.controller.TeacherController;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "currentStudentNumber", ignore = true)
    Course createCourseRequestToCourse(TeacherController.CreateCourseRequest createCourseRequest);

    List<ClassTime> classTimeDtoListToClassTimeList(List<ClassTimeDto> classTimes);

}
