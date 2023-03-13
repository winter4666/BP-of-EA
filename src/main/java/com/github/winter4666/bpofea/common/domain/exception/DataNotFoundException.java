package com.github.winter4666.bpofea.common.domain.exception;

public class DataNotFoundException extends BusinessException {
    public DataNotFoundException(String messagePattern, Object...args) {
        super(messagePattern, args);
    }
}
