package com.son.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class IsNotNullItemInArrayValidator implements ConstraintValidator<IsNotNullItemInArray, List<?>> {
    private IsNotNullItemInArray annotation;

    @Override
    public void initialize(IsNotNullItemInArray annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(List<?> values, ConstraintValidatorContext context) {
        if (values == null || values.isEmpty()) {
            return true;
        }

        boolean isValid = !values.contains(null);
        String message = annotation.message();

        if (!isValid) {
            message = "each value in array must not be null, maybe the item is mismatch type";
        }

        // disable existing violation message
        context.disableDefaultConstraintViolation();
        // build new violation message and add it
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        return isValid;
    }
}
