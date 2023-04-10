package com.github.winter4666.bpofea.common.domain.model;

import java.util.List;

public interface DomainEventPublishable<T> {

    List<T> getDomainEvents();

    default void registerEvent(T event) {
        getDomainEvents().add(event);
    }

}
