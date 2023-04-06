package com.github.winter4666.bpofea.user.domain.model;

import com.github.winter4666.bpofea.common.domain.exception.DataCollisionException;
import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.user.datafaker.TeacherBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TeacherTest {

    @Test
    void should_create_course_successfully_given_no_collision() {
        Teacher teacher = new Teacher();
        Course course = mock(Course.class);

        teacher.createCourse(course);

        assertThat(teacher.getCourses().iterator().next(), equalTo(course));
    }

    @Test
    void should_remove_course_successfully() {
        CourseBuilder courseBuilder = new CourseBuilder();
        Teacher teacher = new TeacherBuilder().coursesFromBuilders(new HashSet<>(){{add(courseBuilder);}}).build();

        teacher.removeCourse(courseBuilder.build());

        assertThat(teacher.getCourses(), is(empty()));
    }

    @Test
    void should_throw_exception_when_create_course_given_collision_existed() {
        Teacher teacher = spy(Teacher.class);
        Course course = mock(Course.class);
        doReturn(true).when(teacher).haveAnyCourseCollidingWith(course);

        assertThrows(DataCollisionException.class, () -> teacher.createCourse(course));
    }

    @Test
    void should_return_ture_when_invoke_have_any_course_colliding_with_given_collision_existed() {
        Course course1 = mock(Course.class);
        Course course2 = new CourseBuilder().build();
        Teacher teacher = new TeacherBuilder().courses(Set.of(course1)).build();
        when(course1.collideWith(course2)).thenReturn(true);

        boolean result = teacher.haveAnyCourseCollidingWith(course2);

        assertThat(result, equalTo(true));
    }

}