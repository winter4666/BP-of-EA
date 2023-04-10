package com.github.winter4666.bpofea.config;

import com.github.winter4666.bpofea.common.dao.CaptureDomainEventListener;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private CaptureDomainEventListener captureDomainEventListener;

    @PostConstruct
    private void configureSessionFactory() {
        SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        EventListenerRegistry eventListenerRegistry = sessionFactory
                .getServiceRegistry()
                .getService(EventListenerRegistry.class);
        eventListenerRegistry.prependListeners(EventType.POST_COMMIT_UPDATE, captureDomainEventListener);
    }
}
