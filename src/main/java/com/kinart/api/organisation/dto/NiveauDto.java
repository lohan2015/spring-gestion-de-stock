package com.kinart.api.organisation.dto;

import com.kinart.organisation.business.model.Orgniveau;
import com.kinart.organisation.business.model.Orgposte;
import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NiveauDto {
    private Integer id;
    private Integer idEntreprise;
    private String codeniveau;
    private String libelle;
    private String codecouleur;
    private String priseencomptecouleur;

    public static NiveauDto fromEntity(Orgniveau orgniveau) {
        if (orgniveau == null) {
            return null;
        }

        NiveauDto dto = new  NiveauDto();
        BeanUtils.copyProperties(orgniveau, dto);
        return dto;
    }

    public static Orgniveau toEntity(NiveauDto dto) {
        if (dto == null) {
            return null;
        }

        Orgniveau orgniveau = new Orgniveau();
        BeanUtils.copyProperties(dto, orgniveau);
        return orgniveau;
    }
}
