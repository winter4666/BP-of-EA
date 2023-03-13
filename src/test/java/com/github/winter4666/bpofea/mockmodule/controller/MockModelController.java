package com.github.winter4666.bpofea.mockmodule.controller;

import com.github.winter4666.bpofea.mockmodule.domain.model.MockModel;
import com.github.winter4666.bpofea.mockmodule.domain.service.MockModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mock_models")
@RequiredArgsConstructor
public class MockModelController {

    private final MockModelService mockModelService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MockModel addMockModel(@RequestBody CreateMockModelRequest createMockModelRequest) {
        return mockModelService.addMockModel(createMockModelRequest.name());
    }

    @GetMapping("/{mockModelId}")
    public MockModel getMockModel(@PathVariable long mockModelId) {
        return mockModelService.getMockModel(mockModelId);
    }

    public record CreateMockModelRequest(String name) {
    }


}
