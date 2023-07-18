package com.kinart.portail.business.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.stream.Stream;

public enum EnumOuiNon implements Serializable {
    NONE("NONE"),
    OUI("OUI"),
    NON("NON");

    private String code;

    private EnumOuiNon(String code) {
        this.code=code;
    }

    @JsonCreator
    public static EnumOuiNon decode(final String code) {
        return Stream.of(EnumOuiNon.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
