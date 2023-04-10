package com.github.winter4666.bpofea.course.message;

import com.github.winter4666.bpofea.course.domain.event.CourseFullEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageSender {

    @EventListener
    public void sendCourseFullMessage(CourseFullEvent courseFullEvent) {
        log.info("Course {} is full", courseFullEvent.course().getName());
    }

}
