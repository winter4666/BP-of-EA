package com.github.winter4666.bpofea.user.datafaker;

import com.github.winter4666.bpofea.user.domain.model.Teacher;

public class TeacherBuilder {

    private Long id;

    public TeacherBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public Teacher build() {
        return Teacher.builder().id(id).build();
    }

}
