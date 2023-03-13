package com.github.winter4666.bpofea.mockmodule.domain.service;

import com.github.winter4666.bpofea.mockmodule.domain.model.MockModel;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MockModelService {

    public MockModel addMockModel(String name) {
        throw new NotImplementedException();
    }

    public MockModel getMockModel(long mockModelId) {
        throw new NotImplementedException();
    }
}
