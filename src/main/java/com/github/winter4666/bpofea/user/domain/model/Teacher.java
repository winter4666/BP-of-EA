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

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor
public class Teacher extends User {

    private String jobNumber;

    @Builder.Default
    @OneToMany(mappedBy = "teacher", cascade = {CascadeType.PERSIST})
    private List<Course> courses = new ArrayList<>();

    public void startCourse(Course course) {
        if(haveAnyCourseCollidingWith(course)) {
            throw new DataCollisionException("The teacher has some course colliding with the new course. Teacher id = {} ", getId());
        }
        course.onStarted(this);
        courses.add(course);
    }

    public boolean haveAnyCourseCollidingWith(Course course) {
        return courses.stream().anyMatch(c -> c.collideWith(course));
    }
}
