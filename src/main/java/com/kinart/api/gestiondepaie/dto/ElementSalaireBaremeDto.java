package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.ElementSalaire;
import com.kinart.paie.business.model.ElementSalaireBareme;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder@ApiModel(description = "Model de gestion des barèmes")
public class ElementSalaireBaremeDto {
    private Integer id;
    private Integer idEntreprise;

    private String crub;

    private Integer nume;

    private String val1;

    private String val2;

    private BigDecimal taux;

    private BigDecimal mont;

    private String mode;

    @JsonIgnore
    private ElementSalaire elementSalaire;

    public static ElementSalaireBaremeDto fromEntity(ElementSalaireBareme elementSalaireBareme) {
        if (elementSalaireBareme == null) {
            return null;
        }

        ElementSalaireBaremeDto dto = new  ElementSalaireBaremeDto();
        BeanUtils.copyProperties(elementSalaireBareme, dto);
        return dto;
    }

    public static ElementSalaireBareme toEntity(ElementSalaireBaremeDto elementSalaireBaremeDto) {
        if (elementSalaireBaremeDto == null) {
            return null;
        }

        ElementSalaireBareme elementSalaireBareme = new ElementSalaireBareme();
        BeanUtils.copyProperties(elementSalaireBaremeDto, elementSalaireBareme);
        return elementSalaireBareme;
    }
}
