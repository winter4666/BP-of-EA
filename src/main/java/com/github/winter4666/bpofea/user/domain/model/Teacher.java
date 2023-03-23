package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.common.domain.exception.DataCollisionException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor
public class Teacher extends User {

    private String jobNumber;

    @Builder.Default
    @OneToMany(mappedBy = "teacher", cascade = {CascadeType.PERSIST})
    private Set<Course> courses = new HashSet<>();

    public void startCourse(Course course) {
        if(haveAnyCourseCollidingWith(course)) {
            throw new DataCollisionException("The teacher has some course colliding with the new course. Teacher id = {} ", getId());
        }
        course.onStarted(this);
        courses.add(course);
    }
}
