package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.ElementFixeSalaire;
import com.kinart.paie.business.model.ElementSalaire;
import com.kinart.paie.business.model.Salarie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des éléments de salaire")
public class ElementFixeSalaireDto {
    private Integer id;
    private Integer idEntreprise;

    private String nmat;

    private String codp;

    private BigDecimal monp;

    private Date ddeb;

    private Date dfin;
    private String nomSalarie;
    private String librubrique;

    public static ElementFixeSalaireDto fromEntity(ElementFixeSalaire elementSalaire) {
        if (elementSalaire == null) {
            return null;
        }

        ElementFixeSalaireDto dto = new  ElementFixeSalaireDto();
        BeanUtils.copyProperties(elementSalaire, dto);
        return dto;
    }

    public static ElementFixeSalaire toEntity(ElementFixeSalaireDto elementFixeSalaireDto) {
        if (elementFixeSalaireDto == null) {
            return null;
        }

        ElementFixeSalaire elementFixeSalaire = new ElementFixeSalaire();
        BeanUtils.copyProperties(elementFixeSalaireDto, elementFixeSalaire);
        return elementFixeSalaire;
    }
}
