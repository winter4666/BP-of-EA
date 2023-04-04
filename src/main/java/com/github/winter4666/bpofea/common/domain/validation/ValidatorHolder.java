package com.github.winter4666.bpofea.common.domain.validation;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorHolder {

    private static final CustomValidator validator;

    static {
        try(ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = new CustomValidator(validatorFactory.getValidator());
        }
    }

    public static CustomValidator get() {
        return validator;
    }

}
