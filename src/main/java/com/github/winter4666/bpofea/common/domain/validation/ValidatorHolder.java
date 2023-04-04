package com.github.winter4666.bpofea.common.domain.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorHolder {

    private static final Validator validator;

    static {
        try(ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    public static Validator get() {
        return validator;
    }

}
