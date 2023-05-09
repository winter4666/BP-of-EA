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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends User {

    @Getter
    private String studentNumber;

    @OneToMany(mappedBy = "student", cascade = CascadeType.PERSIST)
    private List<StudentCourse> studentCourses = new ArrayList<>();

    @Builder
    Student(Long id, String name, String studentNumber) {
        super(id, name);
        this.studentNumber = studentNumber;
    }

    public void chooseCourse(Course course) {
        if(haveAnyCourseCollidingWith(course)) {
            throw new DataCollisionException("The student has chosen some course colliding with the new course. Student id = {} ", getId());
        }
        course.onChosen();
        studentCourses.add(new StudentCourse(this, course));
    }

    @Override
    public Set<Course> getCourses() {
        return studentCourses.stream().map(StudentCourse::getCourse).collect(Collectors.toSet());
    }
}
