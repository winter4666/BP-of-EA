package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.course.domain.model.Course;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.NotImplementedException;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor
public class Student extends User {

    private String studentNumber;

    public void chooseCourse(Course course) {
        throw new NotImplementedException();
    }
}
