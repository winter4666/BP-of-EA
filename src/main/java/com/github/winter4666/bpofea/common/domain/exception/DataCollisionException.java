package com.github.winter4666.bpofea.common.domain.exception;

public class DataCollisionException extends BusinessException {

    public DataCollisionException(String messagePattern, Object...args) {
        super(messagePattern, args);
    }
}
