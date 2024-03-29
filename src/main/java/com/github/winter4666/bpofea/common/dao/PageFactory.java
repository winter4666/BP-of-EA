package com.github.winter4666.bpofea.common.dao;

import com.github.winter4666.bpofea.common.domain.model.Page;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PageFactory {

    public static <T> Page<T> createPageFrom(org.springframework.data.domain.Page<T> page) {
        return new Page<>(page.getContent(), page.getTotalElements());
    }

}
