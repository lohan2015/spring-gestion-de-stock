package com.kinart.api.portail.dto;

import com.kinart.portail.business.model.NotifAttestation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Cyrille MBASSI
 * @since 04.07.23
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifattestationDto implements Serializable {

    private Integer id;

    private Date date_created;

    private String sender;

    private String recipient;

    private String message;

    public static NotifattestationDto fromEntity(NotifAttestation entity){
        NotifattestationDto dto = new NotifattestationDto();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    public static NotifAttestation fromEntity(NotifattestationDto dto){
        NotifAttestation entity = new NotifAttestation();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }
}
