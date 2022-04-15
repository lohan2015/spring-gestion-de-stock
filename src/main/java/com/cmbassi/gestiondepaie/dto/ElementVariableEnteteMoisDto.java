package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.ElementVariableEnteteMois;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class ElementVariableEnteteMoisDto implements Serializable {
    private Integer id;
    private Integer idEntreprise;
    private String aamm;
    private String nmat;
    private Integer nbul;
    private Date ddpa;
    private Date dfpa;
    private String bcmo;

    @JsonIgnore
    private List<ElementVariableDetailMoisDto> elementVariableDetailMois;

    public ElementVariableEnteteMoisDto() {
    }

    public ElementVariableEnteteMoisDto(Integer id, Integer idEntreprise, String aamm, String nmat, Integer nbul, Date ddpa, Date dfpa, String bcmo, List<ElementVariableDetailMoisDto> elementVariableDetailMois) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.aamm = aamm;
        this.nmat = nmat;
        this.nbul = nbul;
        this.ddpa = ddpa;
        this.dfpa = dfpa;
        this.bcmo = bcmo;
        this.elementVariableDetailMois = elementVariableDetailMois;
    }

    public static ElementVariableEnteteMoisDto fromEntity(ElementVariableEnteteMois elementVariableEnteteMois) {
        if (elementVariableEnteteMois == null) {
            return null;
        }

        ElementVariableEnteteMoisDto dto = new  ElementVariableEnteteMoisDto();
        BeanUtils.copyProperties(elementVariableEnteteMois, dto);
        return dto;
    }

    public static ElementVariableEnteteMoisDto toEntity(ElementSalaireDto dto) {
        if (dto == null) {
            return null;
        }
        ElementVariableEnteteMoisDto eltVar = new ElementVariableEnteteMoisDto();
        BeanUtils.copyProperties(dto, eltVar, "elementVariableDetailMois");
        return eltVar;
    }
}
