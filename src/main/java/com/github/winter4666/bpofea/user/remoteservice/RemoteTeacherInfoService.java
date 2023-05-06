package com.github.winter4666.bpofea.user.remoteservice;

import com.github.winter4666.bpofea.user.domain.model.TeacherMoreInfo;
import com.github.winter4666.bpofea.user.domain.service.TeacherInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RemoteTeacherInfoService implements TeacherInfoService {

    private final TeacherInfoClient teacherInfoClient;

    @Override
    public Optional<TeacherMoreInfo> getTeacherInfo(String jobNumber) {
        List<TeacherMoreInfo> teacherMoreInfoList = teacherInfoClient.getTeacherInfo(jobNumber);
        return teacherMoreInfoList.isEmpty() ? Optional.empty() : Optional.of(teacherMoreInfoList.get(0));
    }
}