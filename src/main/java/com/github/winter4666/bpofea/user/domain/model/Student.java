package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.common.domain.exception.DataCollisionException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends User {

    private String studentNumber;

    @ManyToMany
    @JoinTable(name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    @Builder
    Student(Long id, String name, String studentNumber) {
        super(id, name);
        this.studentNumber = studentNumber;
    }

    public void chooseCourse(Course course) {
        if(haveAnyCourseCollidingWith(course)) {
            throw new DataCollisionException("The student has chosen some course colliding with the new course. Student id = {} ", getId());
        }
        courses.add(course);
    }

    public void revokeChoice(Course course) {
        getCourses().remove(course);
    }
}
