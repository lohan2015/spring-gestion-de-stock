package com.kinart.api.portail.dto;

import com.kinart.portail.business.model.NotifModifInfo;
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
public class NotifmodifinfoDto implements Serializable {

    private Integer id;

    private Date date_created;

    private String sender;

    private String recipient;

    private String message;

    public static NotifmodifinfoDto fromEntity(NotifModifInfo entity){
        NotifmodifinfoDto dto = new NotifmodifinfoDto();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    public static NotifModifInfo fromEntity(NotifmodifinfoDto dto){
        NotifModifInfo entity = new NotifModifInfo();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }
}
