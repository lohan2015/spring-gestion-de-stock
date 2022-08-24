package com.kinart.api.organisation.dto;

import com.kinart.organisation.business.model.Orgniveau;
import lombok.*;
import org.springframework.beans.BeanUtils;

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
