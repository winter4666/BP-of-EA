package com.github.winter4666.bpofea.user.remoteservice;

import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.winter4666.bpofea.config.WebClientConfig;
import com.github.winter4666.bpofea.user.domain.model.Gender;
import com.github.winter4666.bpofea.user.domain.model.TeacherMoreInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(classes = WebClientConfig.class)
@Import(RemoteTeacherInfoService.class)
@WireMockTest(httpPort = 8090)
class RemoteTeacherInfoServiceIT {

    @Autowired
    private RemoteTeacherInfoService remoteTeacherInfoService;

    @Test
    void should_return_teacher_info_when_get_teacher_info_given_teacher_info_found() {
        String jobNumber = String.valueOf(new Faker().number().randomNumber());
        stubFor(get(urlPathEqualTo("/teachers")).withQueryParam("jobNumber", WireMock.equalTo(jobNumber))
                .willReturn(okJson("[{\"gender\": \"MAN\"}]")));

        TeacherMoreInfo teacherMoreInfo = remoteTeacherInfoService.getTeacherInfo(jobNumber).orElseThrow();

        assertThat(teacherMoreInfo.gender(), is(Gender.MAN));
    }

    @Test
    void should_return_null_when_get_teacher_info_given_teacher_info_not_found() {
        String jobNumber = String.valueOf(new Faker().number().randomNumber());
        stubFor(get(urlPathEqualTo("/teachers")).withQueryParam("jobNumber", WireMock.equalTo(jobNumber))
                .willReturn(okJson("[]")));

        TeacherMoreInfo teacherMoreInfo = remoteTeacherInfoService.getTeacherInfo(jobNumber).orElse(null);

        assertThat(teacherMoreInfo, is(nullValue()));
    }

}