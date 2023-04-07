package com.github.winter4666.bpofea.common.springmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.winter4666.bpofea.common.domain.exception.DataCollisionException;
import com.github.winter4666.bpofea.common.domain.exception.DataInvalidException;
import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.mockmodule.controller.MockModelController;
import com.github.winter4666.bpofea.mockmodule.domain.model.MockModel;
import com.github.winter4666.bpofea.mockmodule.domain.service.MockModelService;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MockModelController.class)
class GlobalExceptionHandlerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MockModelService mockModelService;

    @Autowired
    private ObjectMapper objectMapper;

    @ExtendWith(OutputCaptureExtension.class)
    @Test
    void should_return_500_status_code_and_log_exception_when_call_restful_api_given_exception_occurred(CapturedOutput output) throws Exception {
        String errorMessage = "Some exception occurred";
        MockModel mockModel = MockModel.builder().name(new Faker().name().fullName()).build();
        when(mockModelService.addMockModel(mockModel.getName())).thenThrow(new RuntimeException(errorMessage));

        mvc.perform(post("/mock_models").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mockModel)))
                .andExpectAll(status().isInternalServerError());
        assertThat(output.getOut(), allOf(containsString("Error occurred while api POST /mock_models is called"),
                containsString(errorMessage)));
    }

    @Test
    void should_return_422_status_code_and_error_message_when_call_restful_api_given_constraint_violation_exception_occurred() throws Exception {
        String errorMessage = "Some constraint is violated";
        MockModel mockModel = MockModel.builder().name(new Faker().name().fullName()).build();
        when(mockModelService.addMockModel(mockModel.getName())).thenThrow(new ConstraintViolationException(errorMessage, null));

        MvcResult mvcResult =  mvc.perform(post("/mock_models").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mockModel)))
                .andExpectAll(status().isUnprocessableEntity()).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(errorMessage));
    }

    @Test
    void should_return_422_status_code_and_error_message_when_call_restful_api_given_data_invalid_exception_occurred() throws Exception {
        String errorMessage = "Data is invalid";
        MockModel mockModel = MockModel.builder().name(new Faker().name().fullName()).build();
        when(mockModelService.addMockModel(mockModel.getName())).thenThrow(new DataInvalidException(errorMessage));

        MvcResult mvcResult =  mvc.perform(post("/mock_models").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mockModel)))
                .andExpectAll(status().isUnprocessableEntity()).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(errorMessage));
    }

    @Test
    void should_return_404_status_code_and_error_message_when_call_restful_api_given_data_not_found_exception_occurred() throws Exception {
        long mockModelId = new Faker().number().randomNumber();
        String errorMessage = "Something not found";
        when(mockModelService.getMockModel(mockModelId)).thenThrow(new DataNotFoundException(errorMessage));

        MvcResult mvcResult = mvc.perform(get("/mock_models/{mockModelId}", mockModelId))
                .andExpectAll(status().isNotFound()).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(errorMessage));
    }

    @Test
    void should_return_409_status_code_and_error_message_when_call_restful_api_given_data_collision_exception_occurred() throws Exception {
        String errorMessage = "data collided";
        MockModel mockModel = MockModel.builder().name(new Faker().name().fullName()).build();
        when(mockModelService.addMockModel(mockModel.getName())).thenThrow(new DataCollisionException(errorMessage));

        MvcResult mvcResult =  mvc.perform(post("/mock_models").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mockModel)))
                .andExpectAll(status().isConflict()).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(errorMessage));
    }

    @Test
    void should_return_409_status_code_and_error_message_when_call_restful_api_given_optimistic_lock_exception_occurred() throws Exception {
        String errorMessage = "data collided";
        MockModel mockModel = MockModel.builder().name(new Faker().name().fullName()).build();
        when(mockModelService.addMockModel(mockModel.getName())).thenThrow(new OptimisticLockException(errorMessage));

        MvcResult mvcResult =  mvc.perform(post("/mock_models").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mockModel)))
                .andExpectAll(status().isConflict()).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(errorMessage));
    }
}