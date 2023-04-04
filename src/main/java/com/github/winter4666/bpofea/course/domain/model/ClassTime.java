package com.github.winter4666.bpofea.course.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ClassTime(@NotNull DayOfWeek dayOfWeek, @NotNull LocalTime startTime, @NotNull LocalTime stopTime) {

    @JsonCreator
    public ClassTime(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime stopTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public boolean collideWith(ClassTime classTime) {
        if (dayOfWeek != classTime.dayOfWeek) {
            return false;
        }
        if (startTime.isBefore(classTime.startTime)) {
            return stopTime.isAfter(classTime.startTime);
        } else {
            return classTime.stopTime.isAfter(startTime);
        }
    }

}
