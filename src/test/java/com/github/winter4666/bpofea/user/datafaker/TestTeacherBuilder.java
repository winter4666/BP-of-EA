package com.github.winter4666.bpofea.user.datafaker;

import com.github.winter4666.bpofea.user.domain.model.Teacher;

public class TestTeacherBuilder {

    private Long id;

    public TestTeacherBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public Teacher build() {
        return Teacher.builder().id(id).build();
    }

}
