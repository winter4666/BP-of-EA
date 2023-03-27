package com.github.winter4666.bpofea.common.domain.model;

import java.util.List;

public record Page<T>(List<T> content, Long totalElements) {

}