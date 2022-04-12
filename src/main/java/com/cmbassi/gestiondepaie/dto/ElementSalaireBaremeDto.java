package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.ElementSalaire;
import com.cmbassi.gestiondepaie.model.ElementSalaireBareme;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@Builder
public class ElementSalaireBaremeDto {

    private Integer idEntreprise;

    private String crub;

    private Integer nume;

    private String val1;

    private String val2;

    private BigDecimal taux;

    private BigDecimal mont;

    @JsonIgnore
    private ElementSalaire elementSalaire;

    public ElementSalaireBaremeDto() {
    }

    public ElementSalaireBaremeDto(Integer idEntreprise, String crub, Integer nume, String val1, String val2, BigDecimal taux, BigDecimal mont, ElementSalaire elementSalaire) {
        this.idEntreprise = idEntreprise;
        this.crub = crub;
        this.nume = nume;
        this.val1 = val1;
        this.val2 = val2;
        this.taux = taux;
        this.mont = mont;
        this.elementSalaire = elementSalaire;
    }

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
