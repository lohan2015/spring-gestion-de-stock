package com.kinart.portail.business.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.stream.Stream;

public enum EnumStatusType implements Serializable {
    NONE("NONE"),
    EATTENTE_VALIDATION("ATTENTE_VALIDATION"),
    REJETEE("REJETEE"),
    VALIDEE("VALIDEE");

    private String code;

    private EnumStatusType(String code) {
        this.code=code;
    }

    @JsonCreator
    public static EnumStatusType decode(final String code) {
        return Stream.of(EnumStatusType.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
