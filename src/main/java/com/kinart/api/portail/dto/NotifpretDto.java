package com.kinart.api.portail.dto;

import com.kinart.portail.business.model.NotifPret;
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
public class NotifpretDto implements Serializable {

    private Integer id;

    private Date date_created;

    private String sender;

    private String recipient;

    private String message;

    private String senderName;

    private String recipientName;

    public static NotifpretDto fromEntity(NotifPret entity){
        NotifpretDto dto = new NotifpretDto();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    public static NotifPret fromEntity(NotifpretDto dto){
        NotifPret entity = new NotifPret();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }
}
