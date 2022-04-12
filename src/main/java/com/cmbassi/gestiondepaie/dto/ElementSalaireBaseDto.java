package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.ElementSalaire;
import com.cmbassi.gestiondepaie.model.ElementSalaireBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@Builder
public class ElementSalaireBaseDto {

    private Integer idEntreprise;

    private String crub;

    private Integer nume;

    private String sign;

    private String rubk;

    @JsonIgnore
    private ElementSalaire elementSalaire;

    public ElementSalaireBaseDto() {
    }

    public ElementSalaireBaseDto(Integer idEntreprise, String crub, Integer nume, String sign, String rubk, ElementSalaire elementSalaire) {
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
