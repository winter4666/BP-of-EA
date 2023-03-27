package com.github.winter4666.bpofea.common.domain.model;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

class PageTest {

    @Test
    void should_get_content_and_total() {
        List<Object> content = List.of(mock(Object.class));
        long totalElements = new Faker().random().nextLong();

        Page<Object> page = new Page<>(content, totalElements);

        assertThat(page.content(), equalTo(content));
        assertThat(page.totalElements(), equalTo(totalElements));
    }

}