package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.PretInterne;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class PretInterneDto implements Serializable {
    private Integer id;
    private Integer lg;
    private Integer idEntreprise;
    private String nmat;
    private Integer nprt;
    private String crub;
    private String com1;
    private String com2;
    private Date dmep;
    private Date dpec;
    private Date per1;
    private BigDecimal mntp;
    private BigDecimal resr;
    private Integer nbec;
    private Integer nber;
    private BigDecimal mtec;
    private BigDecimal tint;
    private BigDecimal ttax;
    private String pact;
    private String etpr;
    private Date dcrp;
    private String ncpt;

    public PretInterneDto() {
    }

    public PretInterneDto(Integer id, Integer lg, Integer idEntreprise, String nmat, Integer nprt, String crub, String com1, String com2, Date dmep, Date dpec, Date per1, BigDecimal mntp, BigDecimal resr, Integer nbec, Integer nber, BigDecimal mtec, BigDecimal tint, BigDecimal ttax, String pact, String etpr, Date dcrp, String ncpt) {
        this.id = id;
        this.lg = lg;
        this.idEntreprise = idEntreprise;
        this.nmat = nmat;
        this.nprt = nprt;
        this.crub = crub;
        this.com1 = com1;
        this.com2 = com2;
        this.dmep = dmep;
        this.dpec = dpec;
        this.per1 = per1;
        this.mntp = mntp;
        this.resr = resr;
        this.nbec = nbec;
        this.nber = nber;
        this.mtec = mtec;
        this.tint = tint;
        this.ttax = ttax;
        this.pact = pact;
        this.etpr = etpr;
        this.dcrp = dcrp;
        this.ncpt = ncpt;
    }

    public static PretInterneDto fromEntity(PretInterne pretInterne) {
        if (pretInterne == null) {
            return null;
        }
        PretInterneDto dto = new  PretInterneDto();
        BeanUtils.copyProperties(pretInterne, dto);
        return dto;
    }

    public static PretInterne toEntity(PretInterneDto dto) {
        if (dto == null) {
            return null;
        }
        PretInterne pretInterne = new PretInterne();
        BeanUtils.copyProperties(dto, pretInterne);
        return pretInterne;
    }
}
