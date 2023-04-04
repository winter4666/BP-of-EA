package com.github.winter4666.bpofea.course.domain.model;

import com.github.winter4666.bpofea.common.domain.validation.ValidatorHolder;

public class CourseBuilder extends Course.CourseBuilder {
    @Override
    public Course build() {
        Course course = super.build();
        ValidatorHolder.get().validateAndThrowExceptionIfNotValid(course);
        return course;
    }
}
