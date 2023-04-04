package com.github.winter4666.bpofea.common.domain.validation;


import com.github.javafaker.Faker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.validation.metadata.BeanDescriptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomValidatorTest {

    @Mock
    private Validator validator;

    @InjectMocks
    private CustomValidator customValidator;

    @SuppressWarnings("unchecked")
    @Test
    void should_throw_exception_when_validate_and_throw_exception_if_not_valid_given_not_valid() {
        Object object = mock(Object.class);
        Set<ConstraintViolation<Object>> constraintViolations = Set.of(mock(ConstraintViolation.class));
        when(validator.validate(object)).thenReturn(constraintViolations);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> customValidator.validateAndThrowExceptionIfNotValid(object));

        assertThat(exception.getConstraintViolations(), equalTo(constraintViolations));
    }

    @Test
    void should_not_throw_exception_when_validate_and_throw_exception_if_not_valid_given_not_valid() {
        Object object = mock(Object.class);
        when(validator.validate(object)).thenReturn(new HashSet<>());

        assertDoesNotThrow(() -> customValidator.validateAndThrowExceptionIfNotValid(object));
    }

    @SuppressWarnings("unchecked")
    @Test
    void should_delegate_when_validate() {
        Object object = mock(Object.class);
        Set<ConstraintViolation<Object>> constraintViolations = Set.of(mock(ConstraintViolation.class));
        when(validator.validate(object)).thenReturn(constraintViolations);

        Set<ConstraintViolation<Object>> returnedConstraintViolations = customValidator.validate(object);

        assertThat(returnedConstraintViolations, equalTo(constraintViolations));
    }

    @SuppressWarnings("unchecked")
    @Test
    void should_delegate_when_validate_property() {
        Object object = mock(Object.class);
        String property = new Faker().lorem().characters();
        Set<ConstraintViolation<Object>> constraintViolations = Set.of(mock(ConstraintViolation.class));
        when(validator.validateProperty(object, property)).thenReturn(constraintViolations);

        Set<ConstraintViolation<Object>> returnedConstraintViolations = customValidator.validateProperty(object, property);

        assertThat(returnedConstraintViolations, equalTo(constraintViolations));
    }

    @SuppressWarnings("unchecked")
    @Test
    void should_delegate_when_validate_value() {
        Class<Object> beanType = Object.class;
        String property = new Faker().lorem().characters();
        Object value = mock(Object.class);
        Set<ConstraintViolation<Object>> constraintViolations = Set.of(mock(ConstraintViolation.class));
        when(validator.validateValue(beanType, property, value)).thenReturn(constraintViolations);

        Set<ConstraintViolation<Object>> returnedConstraintViolations = customValidator.validateValue(beanType, property, value);

        assertThat(returnedConstraintViolations, equalTo(constraintViolations));
    }

    @Test
    void should_delegate_when_get_constraints_for_class() {
        Class<Object> clazz = Object.class;
        BeanDescriptor beanDescriptor = mock(BeanDescriptor.class);
        when(validator.getConstraintsForClass(clazz)).thenReturn(beanDescriptor);

        BeanDescriptor returnedBeanDescriptor = customValidator.getConstraintsForClass(clazz);

        assertThat(returnedBeanDescriptor, equalTo(beanDescriptor));
    }

    @Test
    void should_delegate_when_unwrap() {
        Class<Object> type = Object.class;
        Object unwrappedObject = mock(Object.class);
        when(validator.unwrap(type)).thenReturn(unwrappedObject);

        Object returnedObject = customValidator.unwrap(type);

        assertThat(returnedObject, equalTo(unwrappedObject));
    }

    @Test
    void should_delegate_when_invoke_for_executables() {
        ExecutableValidator executableValidator = mock(ExecutableValidator.class);
        when(validator.forExecutables()).thenReturn(executableValidator);

        ExecutableValidator returnedExecutableValidator = customValidator.forExecutables();

        assertThat(returnedExecutableValidator, equalTo(executableValidator));
    }

}