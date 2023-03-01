package com.github.winter4666.bpofea.common;

import org.hibernate.type.FormatMapper;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.jackson.JacksonJsonFormatMapper;

public class CustomJacksonJsonFormatMapper implements FormatMapper {

    private JacksonJsonFormatMapper jacksonJsonFormatMapper;

    public CustomJacksonJsonFormatMapper() {
        jacksonJsonFormatMapper = new JacksonJsonFormatMapper(HibernateObjectMapperHolder.get());
    }

    @Override
    public <T> T fromString(CharSequence charSequence, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        return jacksonJsonFormatMapper.fromString(charSequence, javaType, wrapperOptions);
    }

    @Override
    public <T> String toString(T t, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        return jacksonJsonFormatMapper.toString(t, javaType, wrapperOptions);
    }
}
