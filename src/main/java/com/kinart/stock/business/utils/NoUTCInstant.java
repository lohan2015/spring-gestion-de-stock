package com.kinart.stock.business.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class NoUTCInstant implements Converter<LocalDateTime, Instant> {

    @Override
    public Instant convert(LocalDateTime value) {
        return value.toInstant(ZoneOffset.UTC);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(LocalDateTime.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Instant.class);
    }

}
