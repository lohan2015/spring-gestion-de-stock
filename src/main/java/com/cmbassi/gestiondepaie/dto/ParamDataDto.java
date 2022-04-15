package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.ParamData;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class ParamDataDto {
    private Integer id;
    private Integer idEntreprise;

    private Integer ctab;

    private String cacc;

    private Integer nume;

    private String vall;

    private Long valm;

    private BigDecimal valt;

    private String duti;

    private Date vald;

    public ParamDataDto() {
    }

    public ParamDataDto(String cacc, String vall, Long valm, BigDecimal valt, Date vald) {
        this.cacc = cacc;
        this.vall = vall;
        this.valm = valm;
        this.valt = valt;
        this.vald = vald;
    }

    public ParamDataDto(Integer id, Integer idEntreprise, Integer ctab, String cacc, Integer nume, String vall, Long valm, BigDecimal valt, String duti, Date vald) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.ctab = ctab;
        this.cacc = cacc;
        this.nume = nume;
        this.vall = vall;
        this.valm = valm;
        this.valt = valt;
        this.duti = duti;
        this.vald = vald;
    }

    public static ParamDataDto fromEntity(ParamData paramData) {
        if (paramData == null) {
            return null;
        }
        ParamDataDto dto = new  ParamDataDto();
        BeanUtils.copyProperties(paramData, dto);
        return dto;
    }

    public static ParamData toEntity(ParamDataDto dto) {
        if (dto == null) {
            return null;
        }

        ParamData sal = new ParamData();
        BeanUtils.copyProperties(dto, sal);
        return sal;
    }
}
