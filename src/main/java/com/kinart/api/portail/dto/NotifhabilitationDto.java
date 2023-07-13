package com.kinart.api.portail.dto;

import com.kinart.portail.business.model.NotifHabilitation;
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
public class NotifhabilitationDto implements Serializable {

    private Integer id;

    private Date date_created;

    private String sender;

    private String recipient;

    private String message;

    public static NotifhabilitationDto fromEntity(NotifHabilitation entity){
        NotifhabilitationDto dto = new NotifhabilitationDto();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    public static NotifHabilitation fromEntity(NotifhabilitationDto dto){
        NotifHabilitation entity = new NotifHabilitation();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }
}
