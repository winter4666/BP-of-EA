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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;

@Entity
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

}
