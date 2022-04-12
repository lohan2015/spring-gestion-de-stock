package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.Salarie;
import com.cmbassi.gestiondepaie.model.VirementSalarie;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class VirementSalarieDto implements Serializable {
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

    public VirementSalarieDto() {
    }

    public VirementSalarieDto(Integer idEntreprise, String nmat, String bqag, String guic, String comp, String cle, String bqso, Integer pourc, BigDecimal mont, String dvd, BigDecimal txchg, BigDecimal mntdb, BigDecimal mntdvd, String aamm, String princ, String swift, String titu) {
        this.idEntreprise = idEntreprise;
        this.nmat = nmat;
        this.bqag = bqag;
        this.guic = guic;
        this.comp = comp;
        this.cle = cle;
        this.bqso = bqso;
        this.pourc = pourc;
        this.mont = mont;
        this.dvd = dvd;
        this.txchg = txchg;
        this.mntdb = mntdb;
        this.mntdvd = mntdvd;
        this.aamm = aamm;
        this.princ = princ;
        this.swift = swift;
        this.titu = titu;
    }

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
