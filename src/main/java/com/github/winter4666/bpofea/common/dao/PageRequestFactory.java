package com.github.winter4666.bpofea.common.dao;

import com.github.winter4666.bpofea.common.domain.model.PageOptions;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

@UtilityClass
public class PageRequestFactory {

    public static PageRequest createPageRequestFrom(PageOptions pageOptions) {
        return PageRequest.of(pageOptions.page() - 1, pageOptions.perPage());
    }

}
