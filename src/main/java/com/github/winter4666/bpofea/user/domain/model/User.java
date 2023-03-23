package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.course.domain.model.Course;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@MappedSuperclass
@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    public abstract Set<Course> getCourses();

    public boolean haveAnyCourseCollidingWith(Course course) {
        return getCourses().stream().anyMatch(c -> c.collideWith(course));
    }

}
