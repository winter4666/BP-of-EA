package com.github.winter4666.bpofea.course.dao;

import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.model.Course_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CourseSpecs {

    public static Specification<Course> hasNameStartingWith(String namePrefix) {
        return Specification.where(namePrefix == null ? null : (root, query, builder) -> builder.like(root.get(Course_.NAME), namePrefix + "%"));
    }

    public static Specification<Course> hasStateEqualTo(Course.State state) {
        return Specification.where(state == null ? null : (root, query, builder) -> builder.equal(root.get(Course_.STATE), state));
    }
}
