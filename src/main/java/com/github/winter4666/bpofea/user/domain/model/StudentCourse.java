package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.course.domain.model.Course;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(StudentCourse.PK.class)
public class StudentCourse implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class PK implements Serializable {

        private Student student;

        private Course course;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PK pk = (PK) o;
            return Objects.equals(pk.student.getId(), student.getId()) &&
                    Objects.equals(pk.course.getId(), course.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(student.getId(), course.getId());
        }

    }

}
