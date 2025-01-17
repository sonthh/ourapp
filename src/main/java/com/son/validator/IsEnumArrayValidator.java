package com.son.validator;

import com.son.util.common.EnumUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IsEnumArrayValidator implements ConstraintValidator<IsEnumArray, List<String>> {
    private IsEnumArray annotation;

    @Override
    public void initialize(IsEnumArray annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        if (values == null || values.isEmpty()) {
            return true;
        }

        List<String> acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());

        String message = annotation.message();

        boolean isValid = acceptedValues.containsAll(values);

        if (!isValid) {
            message = "each value in array must be any of enum: ["
                    + EnumUtil.getJoinedNames(annotation.enumClass()) + "]";
        }

        // disable existing violation message
        context.disableDefaultConstraintViolation();
        // build new violation message and add it
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        return isValid;
    }
}
