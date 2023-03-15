package com.github.winter4666.bpofea.course.datafaker;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CourseBuilder {

    private static final Faker FAKER = new Faker();

    private Long id;

    private String name = FAKER.educator().course();

    private LocalDate startDate = LocalDate.of(2023, 1, 1);

    private LocalDate stopDate = LocalDate.of(2023, 5, 1);

    private List<ClassTimeBuilder> classTimeBuilders = List.of(new ClassTimeBuilder());

    public CourseBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public CourseBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CourseBuilder startDate(LocalDate startDate)  {
        this.startDate = startDate;
        return this;
    }

    public CourseBuilder stopDate(LocalDate stopDate) {
        this.stopDate = stopDate;
        return this;
    }

    public CourseBuilder classTimes(List<ClassTimeBuilder> classTimeBuilders) {
        this.classTimeBuilders = classTimeBuilders;
        return this;
    }

    public Course build() {
        return Course.builder()
                .id(id)
                .name(name)
                .startDate(startDate)
                .stopDate(stopDate)
                .classTimes(classTimeBuilders == null ? null : classTimeBuilders.stream().map(ClassTimeBuilder::build).toList())
                .build();
    }

    public static class ClassTimeBuilder {
        private DayOfWeek dayOfWeek = DayOfWeek.MONDAY;

        private LocalTime startTime = LocalTime.of(9,0);

        private LocalTime stopTime = LocalTime.of(10, 0);

        public ClassTimeBuilder dayOfWeek(DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
        }

        public ClassTimeBuilder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ClassTimeBuilder stopTime(LocalTime stopTime) {
            this.stopTime = stopTime;
            return this;
        }

        public ClassTime build() {
            return new ClassTime(dayOfWeek, startTime, stopTime);
        }
    }


}
