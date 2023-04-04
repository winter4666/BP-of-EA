package com.github.winter4666.bpofea.course.domain.model;

import com.github.winter4666.bpofea.common.domain.validation.ValidatorHolder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

public class CourseBuilder extends Course.CourseBuilder {
    @Override
    public Course build() {
        Course course = super.build();
        Set<ConstraintViolation<Course>> constraintViolations = ValidatorHolder.get().validate(course);
        if(!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
        return course;
    }
}
