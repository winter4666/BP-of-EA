package com.github.winter4666.bpofea.common.dao;

import com.github.winter4666.bpofea.common.domain.model.DomainEventPublishable;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CaptureDomainEventListener implements PostUpdateEventListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if(event.getEntity() instanceof DomainEventPublishable<?> domainEventPublishable) {
            List<?> domainEvents = domainEventPublishable.getDomainEvents();
            if(domainEvents.size() == 0) {
                return;
            }
            domainEvents.forEach(applicationEventPublisher::publishEvent);
            domainEvents.clear();
        }
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return true;
    }
}
