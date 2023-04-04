package com.github.winter4666.bpofea.course.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Builder
public class ClassTime {

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime stopTime;

    public boolean collideWith(ClassTime classTime) {
        if(dayOfWeek != classTime.dayOfWeek) {
            return false;
        }
        if(startTime.isBefore(classTime.startTime)) {
            return stopTime.isAfter(classTime.startTime);
        } else {
            return classTime.stopTime.isAfter(startTime);
        }
    }

}
