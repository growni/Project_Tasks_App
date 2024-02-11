package com.ipsos.utils;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;




@Component
public class ValidationUtilsImpl implements ValidationUtils{

    private final Validator validator;

    public ValidationUtilsImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <E> boolean isValid(E entity) {
        return this.validator.validate(entity).isEmpty();
    }
}
