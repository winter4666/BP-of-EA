package com.github.winter4666.bpofea.user.datafaker;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class TeacherBuilder {

    private static final Faker FAKER = new Faker();

    private Long id;

    private String name = FAKER.name().name();

    private String jobNumber = String.valueOf(FAKER.number().randomNumber());

    private Set<Course> courses;

    public TeacherBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TeacherBuilder coursesFromBuilders(Set<CourseBuilder> courseBuilders) {
        this.courses = courseBuilders == null ? null : courseBuilders.stream().map(CourseBuilder::build).collect(Collectors.toSet());
        return this;
    }

    public TeacherBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TeacherBuilder jobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
        return this;
    }

    public TeacherBuilder courses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Teacher build() {
        return createTeacherBuilder().build();
    }

    public Teacher.TeacherBuilder createTeacherBuilder() {
        return Teacher.builder()
                .id(id)
                .name(name)
                .jobNumber(jobNumber)
                .courses(courses);
    }

    public Map<String, Object> buildArgsForDbInsertion() {
        return new HashMap<>(){
            {
                put("name", name);
                put("job_number", jobNumber);
            }
        };
    }

}
