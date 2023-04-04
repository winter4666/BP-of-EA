package com.github.winter4666.bpofea.common.domain.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.validation.metadata.BeanDescriptor;

import java.util.Set;

public class CustomValidator implements Validator {

    private final Validator validator;

    public CustomValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void validateAndThrowExceptionIfNotValid(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object, groups);
        if(!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return validator.validate(object, groups);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
        return validator.validateProperty(object, propertyName, groups);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        return validator.validateValue(beanType, propertyName, value, groups);
    }

    @Override
    public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
        return validator.getConstraintsForClass(clazz);
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return validator.unwrap(type);
    }

    @Override
    public ExecutableValidator forExecutables() {
        return validator.forExecutables();
    }
}
