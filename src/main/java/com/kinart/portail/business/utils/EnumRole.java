package com.kinart.portail.business.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum EnumRole {
    USER("USER"),
    VALIDABS1("VALIDABS1"),
    VALIDABS2("VALIDABS2"),
    VALIDABS3("VALIDABS3"),
    VALIDABS4("VALIDABS4"),
    SCE_PERSONNEL("SCE_PERSONNEL"),
    DRHL("DRHL"),
    DGA("DGA"),
    DG("DG"),
    ADMIN("ADMIN");

    private String code;

    private EnumRole(String code) {
        this.code=code;
    }

    @JsonCreator
    public static EnumRole decode(final String code) {
        return Stream.of(EnumRole.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
