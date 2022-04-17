package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.ElementVariableDetailMois;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class ElementVariableDetailMoisDto implements Serializable {
    private Integer id;
    private Integer nlig;
    private Integer idEntreprise;
    private String nmat;
    private Integer nbul;
    private String aamm;
    private String rubq;
    private String argu;
    private String nprt;
    private String ruba;
    private BigDecimal mont;
    private String cuti;
    private String nomsalarie;
    private String librubrique;

    @JsonIgnore
    private ElementVariableEnteteMoisDto enteteEltVar;

    public ElementVariableDetailMoisDto() {
    }

    public ElementVariableDetailMoisDto(Integer id, Integer nlig, Integer idEntreprise, String nmat, Integer nbul, String aamm, String rubq, String argu, String nprt, String ruba, BigDecimal mont, String cuti, String nomsalarie, String librubrique, ElementVariableEnteteMoisDto enteteEltVar) {
        this.id = id;
        this.nlig = nlig;
        this.idEntreprise = idEntreprise;
        this.nmat = nmat;
        this.nbul = nbul;
        this.aamm = aamm;
        this.rubq = rubq;
        this.argu = argu;
        this.nprt = nprt;
        this.ruba = ruba;
        this.mont = mont;
        this.cuti = cuti;
        this.nomsalarie = nomsalarie;
        this.librubrique = librubrique;
        this.enteteEltVar = enteteEltVar;
    }

    public static ElementVariableDetailMoisDto fromEntity(ElementVariableDetailMois elementVariableDetailMois) {
        if (elementVariableDetailMois == null) {
            return null;
        }

        ElementVariableDetailMoisDto dto = new ElementVariableDetailMoisDto();
        BeanUtils.copyProperties(elementVariableDetailMois, dto);
        return dto;
    }

    public static ElementVariableDetailMois toEntity(ElementVariableDetailMoisDto dto) {
        if (dto == null) {
            return null;
        }
        ElementVariableDetailMois eltVar = new ElementVariableDetailMois();
        BeanUtils.copyProperties(dto, eltVar, "enteteEltVar", "nomsalarie", "librubrique");
        return eltVar;
    }
}
