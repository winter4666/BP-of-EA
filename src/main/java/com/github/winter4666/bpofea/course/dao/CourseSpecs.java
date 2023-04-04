package com.github.winter4666.bpofea.course.dao;

import com.github.winter4666.bpofea.course.domain.model.Course;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CourseSpecs {

    public static Specification<Course> hasNameStartingWith(String namePrefix) {
        return (root, query, builder) -> builder.like(root.get("name"), namePrefix + "%");
    }
}
