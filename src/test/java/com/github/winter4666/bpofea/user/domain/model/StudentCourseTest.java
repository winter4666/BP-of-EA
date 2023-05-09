package com.github.winter4666.bpofea.user.domain.model;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class StudentCourseTest {

    @Test
    void should_return_true_when_invoke_equals_given_pk_is_the_same() {
        Faker faker = new Faker();
        long studentId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();
        StudentCourse.PK pk1 = new StudentCourse.PK(Student.builder().id(studentId).build(),
                new CourseBuilder().id(courseId).build());
        StudentCourse.PK pk2 = new StudentCourse.PK(Student.builder().id(studentId).build(),
                new CourseBuilder().id(courseId).build());

        boolean result = pk1.equals(pk2);

        assertThat(result, equalTo(true));
    }

    @Test
    void should_return_true_when_invoke_equals_given_pk_is_not_the_same() {
        Faker faker = new Faker();
        long studentId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();
        StudentCourse.PK pk1 = new StudentCourse.PK(Student.builder().id(studentId).build(),
                new CourseBuilder().id(courseId).build());
        StudentCourse.PK pk2 = new StudentCourse.PK(Student.builder().id(studentId).build(),
                new CourseBuilder().id(null).build());

        boolean result = pk1.equals(pk2);

        assertThat(result, equalTo(false));
    }

    @Test
    void should_return_correct_hash_code_when_invoke_hash_code() {
        Faker faker = new Faker();
        long studentId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();
        StudentCourse.PK pk = new StudentCourse.PK(Student.builder().id(studentId).build(),
                new CourseBuilder().id(courseId).build());

        int result = pk.hashCode();

        assertThat(result, equalTo(Objects.hash(studentId, courseId)));
    }

}