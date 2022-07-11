package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.ElementSalaire;
import com.kinart.paie.business.model.ElementSalaireBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des bases de calcul")
public class ElementSalaireBaseDto {
    private Integer id;
    private Integer idEntreprise;

    private String crub;

    private Integer nume;

    private String sign;

    private String rubk;

    private String libRubrique;
    private String mode;

    @JsonIgnore
    private ElementSalaire elementSalaire;

    public static ElementSalaireBaseDto fromEntity(ElementSalaireBase elementSalaireBase) {
        if (elementSalaireBase == null) {
            return null;
        }

        ElementSalaireBaseDto dto = new  ElementSalaireBaseDto();
        BeanUtils.copyProperties(elementSalaireBase, dto);
        return dto;
    }

    public static ElementSalaireBase toEntity(ElementSalaireBaseDto elementSalaireBaseDto) {
        if (elementSalaireBaseDto == null) {
            return null;
        }

        ElementSalaireBase elementSalaireBase = new ElementSalaireBase();
        BeanUtils.copyProperties(elementSalaireBaseDto, elementSalaireBase);
        return elementSalaireBase;
    }
}
