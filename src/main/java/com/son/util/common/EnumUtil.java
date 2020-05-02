package com.son.util.common;

import java.util.Arrays;

public class EnumUtil {

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

    public static String getJoinedNames(Class<? extends Enum<?>> e) {
        return String.join(", ", getNames(e));
    }

    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String value) {
        if (value == null) {
            return null;
        }

        return Enum.valueOf(enumClass, value);
    }
}
