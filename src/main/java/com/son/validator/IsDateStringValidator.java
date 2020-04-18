package com.son.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public class IsDateStringValidator implements ConstraintValidator<IsDateString, String> {
    private IsDateString annotation;

    @Override
    public void initialize(IsDateString annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String message = annotation.message();
        boolean isValid = true;

        try {
            new SimpleDateFormat(annotation.pattern()).parse(value);
        } catch (Exception e) {
            isValid = false;
        }

        if (!isValid) {
            message = "Is not date string pattern: " + annotation.pattern();
        }

        // disable existing violation message
        context.disableDefaultConstraintViolation();
        // build new violation message and add it
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        return isValid;
    }
}
