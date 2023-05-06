package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.user.domain.model.TeacherMoreInfo;

import java.util.Optional;

public interface TeacherInfoService {
    Optional<TeacherMoreInfo> getTeacherInfo(String jobNumber);
}
