package com.github.winter4666.bpofea.common.domain.exception;

public class DataInvalidException extends BusinessException {

    public DataInvalidException(String messagePattern, Object...args) {
        super(messagePattern, args);
    }

}
