package com.github.winter4666.bpofea.common.domain.model;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record Page<T>(List<T> content, Long totalElements) {

    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new Page<>(content.stream().map(converter).collect(Collectors.toList()), totalElements);
    }
}