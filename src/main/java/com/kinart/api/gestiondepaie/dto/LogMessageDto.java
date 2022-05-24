package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.LogMessage;
import com.kinart.paie.business.model.ParamData;
import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessageDto {

    private Integer idEntreprise;

    private String cuti;

    private Date datc;

    private String ligne;

    public static LogMessageDto fromEntity(LogMessage logMessage) {
        if (logMessage == null) {
            return null;
        }
        LogMessageDto dto = new  LogMessageDto();
        BeanUtils.copyProperties(logMessage, dto);
        return dto;
    }

    public static LogMessage toEntity(LogMessageDto dto) {
        if (dto == null) {
            return null;
        }

        LogMessage sal = new LogMessage();
        BeanUtils.copyProperties(dto, sal);
        return sal;
    }
}
