package com.github.winter4666.bpofea.course.controller;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ClassTimeDto(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime stopTime){
}