package com.github.winter4666.bpofea.common.domain.model;

import com.github.winter4666.bpofea.common.domain.exception.DataInvalidException;

public record PageOptions(int perPage, int page) {

    public PageOptions {
        if(perPage <= 0) {
            throw new DataInvalidException("Parameter perPage must be greater than 0");
        }
        if(page <= 0) {
            throw new DataInvalidException("Parameter page must be greater than 0");
        }
    }

}
