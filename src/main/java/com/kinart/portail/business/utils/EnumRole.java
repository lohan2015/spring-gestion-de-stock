package com.kinart.portail.business.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum EnumNotificationType {
    NONE("NONE"),
    ABSENCE_CONGE("ABSENCE_CONGE"),
    HABILITATION("HABILITATION"),
    PRET("PRET"),
    MODIF_INFO("MODIF_INFO"),
    ATTESTATION("ATTESTATION");

    private String code;

    private EnumNotificationType(String code) {
        this.code=code;
    }

    @JsonCreator
    public static EnumNotificationType decode(final String code) {
        return Stream.of(EnumNotificationType.values()).filter(targetEnum -> targetEnum.code.equals(code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
