package com.github.winter4666.bpofea.course.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClassTime {

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime stopTime;

}
