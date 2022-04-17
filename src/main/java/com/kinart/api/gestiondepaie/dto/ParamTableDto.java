package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.ParamTable;
import lombok.*;
import org.springframework.beans.BeanUtils;


@Data
@Builder
public class ParamTableDto {
    private Integer id;
    private Integer idEntreprise;

    private Integer ctab;

    private String libe;

    private int nccl;

    private String typc;

    private String duti;

    private String profil;

    public ParamTableDto() {
    }

    public ParamTableDto(Integer id, Integer idEntreprise, Integer ctab, String libe, int nccl, String typc, String duti, String profil) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.ctab = ctab;
        this.libe = libe;
        this.nccl = nccl;
        this.typc = typc;
        this.duti = duti;
        this.profil = profil;
    }

    public static ParamTableDto fromEntity(ParamTable paramTable) {
        if (paramTable == null) {
            return null;
        }
        ParamTableDto dto = new  ParamTableDto();
        BeanUtils.copyProperties(paramTable, dto);
        return dto;
    }

    public static ParamTable toEntity(ParamTableDto dto) {
        if (dto == null) {
            return null;
        }

        ParamTable sal = new ParamTable();
        BeanUtils.copyProperties(dto, sal);
        return sal;
    }
}
