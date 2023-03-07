package com.github.winter4666.bpofea.course.domain.model;

import com.github.winter4666.bpofea.user.domain.model.Teacher;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class CourseTest {

    @Test
    void should_react_correctly_on_started() {
        Teacher teacher =  Mockito.mock(Teacher.class);
        Course course = new Course();

        course.onStarted(teacher);

        assertThat(course.getTeacher(), equalTo(teacher));
    }

}