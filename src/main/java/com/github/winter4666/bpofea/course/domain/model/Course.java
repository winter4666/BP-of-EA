package com.github.winter4666.bpofea.course.domain.model;

import com.github.winter4666.bpofea.user.domain.model.Teacher;
import jakarta.persistence.*;
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

    private String name;

    private LocalDate startDate;

    private LocalDate stopDate;

    @ManyToOne
    private Teacher teacher;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<ClassTime> classTimes;

}
