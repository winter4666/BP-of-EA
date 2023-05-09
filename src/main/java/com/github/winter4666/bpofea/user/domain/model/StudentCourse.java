package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.common.domain.util.EqualsTemplate;
import com.github.winter4666.bpofea.course.domain.model.Course;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;

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
            return EqualsTemplate.equals(this, o, (t, other) -> new EqualsBuilder()
                    .append(t.student.getId(), other.student.getId())
                    .append(t.course.getId(), other.course.getId())
                    .isEquals());
        }

        @Override
        public int hashCode() {
            return Objects.hash(student.getId(), course.getId());
        }

    }

}
