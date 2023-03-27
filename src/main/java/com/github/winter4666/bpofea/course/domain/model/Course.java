package com.github.winter4666.bpofea.course.domain.model;

import com.github.winter4666.bpofea.user.domain.model.Teacher;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @Valid
    @NotEmpty
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ClassTime> classTimes;

    public void onStarted(Teacher teacher) {
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
}
