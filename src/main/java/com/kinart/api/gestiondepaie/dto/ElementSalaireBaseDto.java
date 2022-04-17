package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.ElementSalaire;
import com.kinart.paie.business.model.ElementSalaireBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@Builder
public class ElementSalaireBaseDto {
    private Integer id;
    private Integer idEntreprise;

    private String crub;

    private Integer nume;

    private String sign;

    private String rubk;

    @JsonIgnore
    private ElementSalaire elementSalaire;

    public ElementSalaireBaseDto() {
    }

    public ElementSalaireBaseDto(Integer id, Integer idEntreprise, String crub, Integer nume, String sign, String rubk, ElementSalaire elementSalaire) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.crub = crub;
        this.nume = nume;
        this.sign = sign;
        this.rubk = rubk;
        this.elementSalaire = elementSalaire;
    }

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
