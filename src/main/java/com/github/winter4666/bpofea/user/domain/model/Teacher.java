package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.common.domain.exception.DataCollisionException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teacher extends User {

    private String jobNumber;

    @OneToMany(orphanRemoval = true, mappedBy = "teacher", cascade = {CascadeType.PERSIST})
    private Set<Course> courses = new HashSet<>();

    @Builder
    public Teacher(Long id, String name, String jobNumber, Set<Course> courses) {
        super(id, name);
        this.jobNumber = jobNumber;
        this.courses = courses;
    }

    public void createCourse(Course course) {
        if(haveAnyCourseCollidingWith(course)) {
            throw new DataCollisionException("The teacher has some course colliding with the new course. Teacher id = {} ", getId());
        }
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }
}
