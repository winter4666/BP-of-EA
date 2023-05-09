package com.github.winter4666.bpofea.user.dao;

import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.testsupport.RdbDaoTest;
import com.github.winter4666.bpofea.user.domain.model.Student;
import com.github.winter4666.bpofea.user.domain.model.StudentCourse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

class RdbStudentCourseDaoIT extends RdbDaoTest {

    @Autowired
    private RdbStudentCourseDao rdbStudentCourseDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void should_find_student_course_successfully() {
        Faker faker = new Faker();
        long studentId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();
        new SimpleJdbcInsert(jdbcTemplate).withTableName("student_course")
                .execute(new HashMap<>(){
                    {put("student_id", studentId);}
                    {put("course_id", courseId);}
                });

        StudentCourse studentCourse = rdbStudentCourseDao.findByStudentIdAndCourseId(studentId, courseId).orElseThrow();

        assertAll(
                () -> assertThat(studentCourse.getStudent().getId(), equalTo(studentId)),
                () -> assertThat(studentCourse.getCourse().getId(), equalTo(courseId))
        );
    }

    @Test
    void should_delete_student_course_successfully() {
        Faker faker = new Faker();
        long studentId = faker.number().randomNumber();
        long courseId = faker.number().randomNumber();
        StudentCourse studentCourse = new StudentCourse(entityManager.getEntityManager().getReference(Student.class, studentId),
                entityManager.getEntityManager().getReference(Course.class, courseId));
        entityManager.persist(studentCourse);

        rdbStudentCourseDao.delete(studentCourse);

        entityManager.flush();
        Long count = jdbcTemplate.queryForObject("select count(1) from student_course where student_id = ? and course_id = ?",
                Long.class, studentId, courseId);
        assertThat(count, is(equalTo(0L)));
    }
}