package com.son.util.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        if (list.isEmpty()) {
            return null;
        }

        return String.join(",", list);
    }

    @Override
    public List<String> convertToEntityAttribute(String joined) {
        if (joined == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(joined.split(",")));
    }
}
