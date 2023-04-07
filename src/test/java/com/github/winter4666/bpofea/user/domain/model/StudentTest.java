package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.common.domain.exception.DataCollisionException;
import com.github.winter4666.bpofea.course.domain.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class StudentTest {

    @Test
    void should_choose_course_successfully() {
        Student student = new Student();
        Course course = mock(Course.class);

        student.chooseCourse(course);

        verify(course).onChosen();
        assertThat(student.getCourses().iterator().next(), equalTo(course));
    }

    @Test
    void should_throw_exception_when_start_course_given_collision_existed() {
        Student student = spy(Student.class);
        Course course = mock(Course.class);
        doReturn(true).when(student).haveAnyCourseCollidingWith(course);

        assertThrows(DataCollisionException.class, () -> student.chooseCourse(course));
    }

    @Test
    void should_revoke_choice_successfully() {
        Course course = mock(Course.class);
        Student student = Student.builder().build();
        ReflectionTestUtils.setField(student, "courses", new HashSet<>(){{add(course);}});

        student.revokeChoice(course);

        verify(course).onRevoked();
        assertThat(student.getCourses(), is(empty()));
    }
}