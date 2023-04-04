package com.github.winter4666.bpofea.course.domain.model;

import com.github.winter4666.bpofea.common.domain.exception.DataInvalidException;
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
import java.util.ArrayList;
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

    @Builder.Default
    @Valid
    @NotEmpty
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ClassTime> classTimes = new ArrayList<>();

    @NotNull
    private Long capacity;

    private Long currentStudentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    public void onCreated(Teacher teacher) {
        if(!startDate.isBefore(stopDate)) {
            throw new DataInvalidException("Stop data should be later than start data in a course");
        }
        if(classTimes.stream().distinct().count() < classTimes.size()) {
            throw new DataInvalidException("Duplicated class times existed");
        }
        currentStudentNumber = 0L;
        this.teacher = teacher;
    }

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

    public static com.github.winter4666.bpofea.course.domain.model.CourseBuilder builder() {
        return new com.github.winter4666.bpofea.course.domain.model.CourseBuilder();
    }
}
