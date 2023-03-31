package com.github.winter4666.bpofea.testsupport;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class SameFieldValuesAs<T> extends TypeSafeMatcher<T> {

    private final T expected;

    private final String[] excludeFields;

    public SameFieldValuesAs(T expected, String[] excludeFields) {
        this.expected = expected;
        this.excludeFields = excludeFields;
    }

    @Override
    protected boolean matchesSafely(T actual) {
        return EqualsBuilder.reflectionEquals(actual, expected, excludeFields);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("same field values as " + expected.getClass().getSimpleName());
        if (ArrayUtils.isNotEmpty(excludeFields)) {
            description.appendText(" ignoring ")
                    .appendValueList("[", ", ", "]", excludeFields);
        }
    }

    public static <T>  Matcher<T> sameFieldValuesAs(T expected, String...excludeFields) {
        return new SameFieldValuesAs<>(expected, excludeFields);
    }
}
