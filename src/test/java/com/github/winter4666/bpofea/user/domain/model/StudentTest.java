package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.course.domain.model.Course;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

class StudentTest {

    @Test
    void should_choose_course_successfully() {
        Student student = new Student();
        Course course = mock(Course.class);
        student.chooseCourse(course);
        assertThat(student.getCourses().iterator().next(), equalTo(course));
    }
}