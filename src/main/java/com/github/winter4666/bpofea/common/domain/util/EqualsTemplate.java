package com.github.winter4666.bpofea.common.domain.util;

import lombok.experimental.UtilityClass;

import java.util.function.BiFunction;

@UtilityClass
public class EqualsTemplate {

    @SuppressWarnings("unchecked")
    public static <T> boolean equals(T nonNullObject, Object nullableObject, BiFunction<T, T, Boolean> biFunction) {
        if (nonNullObject == nullableObject) return true;
        if (nullableObject == null || nonNullObject.getClass() != nullableObject.getClass()) return false;
        return biFunction.apply(nonNullObject, (T)nullableObject);
    }

}
