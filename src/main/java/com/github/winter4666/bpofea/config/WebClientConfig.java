package com.github.winter4666.bpofea.config;

import com.github.winter4666.bpofea.user.remoteservice.TeacherInfoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Value("${teacherInfoServer.baseUrl}")
    private String teacherInfoServerBaseUrl;

    @Bean
    public TeacherInfoClient teacherInfoClient() {
        WebClient client = WebClient.builder().baseUrl(teacherInfoServerBaseUrl).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
        return factory.createClient(TeacherInfoClient.class);
    }
}
