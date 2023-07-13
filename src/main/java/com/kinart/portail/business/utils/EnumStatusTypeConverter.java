package com.kinart.portail.business.utils;

import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

/**
 * AttributeConvertEnItemTypeype, String>. Implements the following methods :
 * <ul>
 * <li>convertToDatabaseColumn : (given an Enum returns a String)
 * <li>convertToEntityAttribute : (given a String returns an Enum)
 * </ul>
 */
@Component
@Converter(autoApply = true)
public class EnumStatusTypeConverter  implements AttributeConverter<EnumStatusType, String> {

    @Override
    public String convertToDatabaseColumn(final EnumStatusType attribute) {
        return Optional.ofNullable(attribute).map(EnumStatusType::getCode).orElse(null);
    }

    @Override
    public EnumStatusType convertToEntityAttribute(final String dbData) {
        return EnumStatusType.decode(dbData);
    }
}
