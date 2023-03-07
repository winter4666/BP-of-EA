package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.course.domain.model.Course;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TeacherTest {

    @Test
    void should_start_course_successfully() {
        Teacher teacher = Teacher.builder().courses(new ArrayList<>()).build();
        Course course = mock(Course.class);

        teacher.startCourse(course);

        assertThat(teacher.getCourses().get(0), equalTo(course));
        verify(course).onStarted(teacher);
    }

}