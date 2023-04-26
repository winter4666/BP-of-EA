package com.github.winter4666.bpofea.common.domain.model;

import com.github.winter4666.bpofea.common.domain.exception.DataInvalidException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageOptionsTest {

    @Test
    void should_throw_exception_given_per_page_not_greater_than_zero() {
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> new PageOptions(0, 1));

        assertThat(exception.getMessage(), equalTo("Parameter perPage must be greater than 0"));
    }

    @Test
    void should_throw_exception_given_page_not_greater_than_zero() {
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> new PageOptions(1, 0));

        assertThat(exception.getMessage(), equalTo("Parameter page must be greater than 0"));
    }

}