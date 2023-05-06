package com.github.winter4666.bpofea.user.remoteservice;

import com.github.winter4666.bpofea.user.domain.model.TeacherMoreInfo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface TeacherInfoClient {

    @GetExchange("/teachers")
    List<TeacherMoreInfo> getTeacherInfo(@RequestParam String jobNumber);
}
