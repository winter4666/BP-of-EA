package com.github.winter4666.bpofea.course.domain.model;

import com.github.winter4666.bpofea.common.domain.exception.DataCollisionException;
import com.github.winter4666.bpofea.common.domain.exception.DataInvalidException;
import com.github.winter4666.bpofea.common.domain.validation.ValidatorHolder;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Getter
public class Course {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate stopDate;

    @Valid
    @NotEmpty
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ClassTime> classTimes;

    @NotNull
    private Long capacity;

    private Long currentStudentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    private State state;

    public boolean collideWith(Course course) {
        if(startDate.isBefore(course.startDate)) {
            if(stopDate.isBefore(course.startDate)) {
                return false;
            }
        } else {
            if(course.stopDate.isBefore(startDate)) {
                return false;
            }
        }
        for(ClassTime classTime : classTimes) {
            if(course.classTimes.stream().anyMatch(t -> t.collideWith(classTime))) {
                return true;
            }
        }
        return false;
    }

    public void setStartDateIfNotNull(LocalDate startDate) {
        if(startDate == null) {
            return;
        }
        this.startDate = startDate;
    }

    public void setStopDateIfNotNull(LocalDate stopDate) {
        if(stopDate == null) {
            return;
        }
        this.stopDate = stopDate;
    }

    public void setClassTimesIfNotNull(List<ClassTime> classTimes) {
        if(classTimes == null) {
            return;
        }
        this.classTimes = classTimes;
    }

    public void onChosen() {
        validateStateIsPublished();
        if(currentStudentNumber >= capacity) {
            throw new DataCollisionException("The people number in the course has reached to capacity. CourseId = {}", id);
        }
        currentStudentNumber++;
    }

    public void onRevoked() {
        validateStateIsPublished();
        currentStudentNumber--;
    }

    private void validateStateIsPublished() {
        if(state != State.PUBLISHED) {
            throw new DataCollisionException("The state of the course is not published. CourseId = {}", id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return name.equals(course.name) && teacher.getId().equals(course.teacher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teacher.getId());
    }

    public static class CourseBuilder {
        public Course build() {
            Course course = new Course(id, name, startDate, stopDate, classTimes, capacity, Objects.requireNonNullElse(currentStudentNumber, 0L), teacher,
                    Objects.requireNonNullElse(state, State.DRAFT));
            ValidatorHolder.get().validateAndThrowExceptionIfNotValid(course);
            if(!startDate.isBefore(stopDate)) {
                throw new DataInvalidException("Stop data should be later than start data in a course");
            }
            if(course.classTimes.stream().distinct().count() < course.classTimes.size()) {
                throw new DataInvalidException("Duplicated class times existed");
            }
            return course;
        }
    }

    public enum State {
        DRAFT, PUBLISHED
    }
}
