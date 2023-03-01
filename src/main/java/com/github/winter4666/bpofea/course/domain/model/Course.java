package com.github.winter4666.bpofea.course.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Course {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date startDate;

    private Date stopDate;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<ClassTime> classTimes;

    public Course(String name, Date startDate, Date stopDate, List<ClassTime> classTimes) {
        this.name = name;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.classTimes = classTimes;
    }

}
