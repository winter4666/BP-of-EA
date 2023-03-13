package com.github.winter4666.bpofea.common.domain.exception;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class BusinessExceptionTest {

    @Test
    void should_set_message_to_message_pattern_given_args_is_empty() {
        BusinessException businessException = new BusinessException("Some error occurred");

        assertThat(businessException.getMessage(), equalTo("Some error occurred"));
    }

    @Test
    void should_set_message_to_message_pattern_given_args_is_not_empty() {
        BusinessException businessException = new BusinessException("Some error occurred. Arg1 is {}, arg2 is {} and arg3 is {}", "a", "b", "c");

        assertThat(businessException.getMessage(), equalTo("Some error occurred. Arg1 is a, arg2 is b and arg3 is c"));
    }

}