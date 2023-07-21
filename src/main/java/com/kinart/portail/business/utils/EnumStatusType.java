package com.kinart.portail.business.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.stream.Stream;

@ApiModel(description = "Enum status type")
public enum EnumStatusType implements Serializable {
    NONE("NONE"),
    ATTENTE_VALIDATION("ATTENTE_VALIDATION"),
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
