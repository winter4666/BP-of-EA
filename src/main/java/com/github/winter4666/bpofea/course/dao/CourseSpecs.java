package com.github.winter4666.bpofea.course.dao;

import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.model.Course_;
import com.github.winter4666.bpofea.user.domain.model.Student;
import com.github.winter4666.bpofea.user.domain.model.Student_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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

    public static Specification<Course> isNotRelatedToStudent(Long studentId) {
        return Specification.where(studentId == null ? null : (root, query, builder) -> {
            query.distinct(true);
            Join<Course, Student> student = root.join(Course_.STUDENTS, JoinType.LEFT);
            return builder.or(builder.notEqual(student.get(Student_.ID), studentId), builder.isNull(student.get(Student_.ID)));
        });
    }
}
