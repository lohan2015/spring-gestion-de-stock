package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.VirementSalarie;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des entêtes des virements")
public class VirementSalarieDto implements Serializable {
    private Integer id;
    private Integer nlig;
    private Integer idEntreprise;
    private String nmat;
    private String bqag;
    private String guic;
    private String comp;
    private String cle;
    private String bqso;
    private Integer pourc;
    private BigDecimal mont;
    private String dvd;
    private BigDecimal txchg;
    private BigDecimal mntdb;
    private BigDecimal mntdvd;
    private String aamm;
    private String princ;
    private String swift;
    private String titu;

    private String nomSalarie;
    private String nomBanqueAgent;
    private String nomBanqueSociete;
    private String mode;

    public static VirementSalarieDto fromEntity(VirementSalarie virementSalarie) {
        if (virementSalarie == null) {
            return null;
        }
        VirementSalarieDto dto = new  VirementSalarieDto();
        BeanUtils.copyProperties(virementSalarie, dto);
        return dto;
    }

    public static VirementSalarie toEntity(VirementSalarieDto dto) {
        if (dto == null) {
            return null;
        }
        VirementSalarie virementSalarie = new VirementSalarie();
        BeanUtils.copyProperties(dto, virementSalarie);
        return virementSalarie;
    }
}
