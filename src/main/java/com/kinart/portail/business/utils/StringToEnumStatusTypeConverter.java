package com.kinart.portail.business.utils;

import org.springframework.core.convert.converter.Converter;


@RequestParameterConverter
public class StringToEnumStatusTypeConverter  implements Converter<String, EnumStatusType> {

    @Override
    public EnumStatusType convert(String source) {
        return EnumStatusType.decode(source);
    }


}
