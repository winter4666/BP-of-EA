package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.course.domain.model.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@SuperBuilder
@Getter
public class Teacher extends User {

    private String jobNumber;

    @OneToMany(mappedBy = "teacher")
    private List<Course> courses;

}
