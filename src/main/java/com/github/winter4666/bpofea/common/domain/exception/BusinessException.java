package com.github.winter4666.bpofea.common.domain.exception;

import org.slf4j.helpers.MessageFormatter;

public class BusinessException extends RuntimeException {

    public BusinessException(String messagePattern, Object...args) {
        super(getMessage(messagePattern, args));
    }

    private static String getMessage(String messagePattern, Object...args) {
        if(args.length == 0) {
            return messagePattern;
        }
        return MessageFormatter.arrayFormat(messagePattern, args).getMessage();
    }

}
