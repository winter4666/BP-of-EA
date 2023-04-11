package com.github.winter4666.bpofea.testsupport;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanRetriever implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanRetriever.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> classOfBean) {
        return applicationContext.getBean(classOfBean);
    }

}
