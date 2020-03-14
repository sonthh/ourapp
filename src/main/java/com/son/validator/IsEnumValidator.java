package com.son.validator;

import com.son.util.common.EnumUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IsEnumValidator implements ConstraintValidator<IsEnum, CharSequence> {
    private IsEnum annotation;
 
    @Override
    public void initialize(IsEnum annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        List<String> acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());

        String message = annotation.message();
        boolean isValid = acceptedValues.contains(value.toString());

        if (!isValid) {
            message = "must be any of enum: [" + EnumUtil.getJoinedNames(annotation.enumClass()) + "]";
        }

        // disable existing violation message
        context.disableDefaultConstraintViolation();
        // build new violation message and add it
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        return isValid;
    }
}