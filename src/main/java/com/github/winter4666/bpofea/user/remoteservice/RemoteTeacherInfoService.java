package com.github.winter4666.bpofea.user.remoteservice;

import com.github.winter4666.bpofea.user.domain.model.TeacherMoreInfo;
import com.github.winter4666.bpofea.user.domain.service.TeacherInfoService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class RemoteTeacherInfoService implements TeacherInfoService {
    @Override
    public TeacherMoreInfo getTeacherInfo(String jobNumber) {
        throw new NotImplementedException();
    }
}
