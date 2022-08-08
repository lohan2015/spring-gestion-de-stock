package com.kinart.api.organisation.dto;

import com.kinart.organisation.business.model.Orgniveau;
import com.kinart.organisation.business.model.Orgniveauemploitype;
import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NiveauemploitypeDto {

    private Integer idEntreprise;
    private String codeniveau;
    private String codeemploitype;
    private String libelleemploitype;
    private String libelleeniveau;

    public static NiveauemploitypeDto fromEntity(Orgniveauemploitype orgniveau) {
        if (orgniveau == null) {
            return null;
        }

        NiveauemploitypeDto dto = new  NiveauemploitypeDto();
        BeanUtils.copyProperties(orgniveau, dto);
        return dto;
    }

    public static Orgniveauemploitype toEntity(NiveauemploitypeDto dto) {
        if (dto == null) {
            return null;
        }

        Orgniveauemploitype orgniveau = new Orgniveauemploitype();
        BeanUtils.copyProperties(dto, orgniveau);
        return orgniveau;
    }
}
