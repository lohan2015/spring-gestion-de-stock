package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.ParamColumn;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@Builder
public class ParamColumnDto {
    private Integer id;
    private Integer idEntreprise;

    private Integer ctab;

    private String ctyp;

    private Integer nume;

    private String libe;

    private String duti;

    private Integer numeLien;

    private Integer ctabLien;

    private String codeProfil;

    private String ctypLien;

    private String obligatoire;

    public ParamColumnDto() {
    }

    public ParamColumnDto(Integer id, Integer idEntreprise, Integer ctab, String ctyp, Integer nume, String libe, String duti, Integer numeLien, Integer ctabLien, String codeProfil, String ctypLien, String obligatoire) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.ctab = ctab;
        this.ctyp = ctyp;
        this.nume = nume;
        this.libe = libe;
        this.duti = duti;
        this.numeLien = numeLien;
        this.ctabLien = ctabLien;
        this.codeProfil = codeProfil;
        this.ctypLien = ctypLien;
        this.obligatoire = obligatoire;
    }

    public static ParamColumnDto fromEntity(ParamColumn paramColumn) {
        if (paramColumn == null) {
            return null;
        }
        ParamColumnDto dto = new  ParamColumnDto();
        BeanUtils.copyProperties(paramColumn, dto);
        return dto;
    }

    public static ParamColumn toEntity(ParamColumnDto dto) {
        if (dto == null) {
            return null;
        }

        ParamColumn sal = new ParamColumn();
        BeanUtils.copyProperties(dto, sal);
        return sal;
    }
}
