package com.github.winter4666.bpofea.course.message;

import com.github.winter4666.bpofea.course.datafaker.CourseBuilder;
import com.github.winter4666.bpofea.course.domain.event.CourseFullEvent;
import com.github.winter4666.bpofea.course.domain.model.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
class MessageSenderIT {

    @Autowired
    private MessageSender messageSender;

    @ExtendWith(OutputCaptureExtension.class)
    @Test
    void should_send_message_successfully(CapturedOutput output) {
        Course course = new CourseBuilder().build();
        CourseFullEvent courseFullEvent = new CourseFullEvent(course);

        messageSender.sendCourseFullMessage(courseFullEvent);

        assertThat(output.getOut(), containsString(MessageFormatter.format("Course {} is full", course.getName()).getMessage()));
    }

    @Configuration
    @ComponentScan
    static class TestConfig {
    }

}