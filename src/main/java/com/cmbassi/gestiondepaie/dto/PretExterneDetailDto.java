package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.PretExterneDetail;
import com.cmbassi.gestiondepaie.model.PretExterneEntete;
import com.cmbassi.gestiondepaie.model.PretInterne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class PretExterneDetailDto implements Serializable {
    private Integer id;
    private Integer idEntreprise;
    private String nprt;
    private Date perb;
    private BigDecimal echo;
    private BigDecimal echr;
    private BigDecimal inte;
    private Integer nbul;
    @JsonIgnore
    private PretExterneEnteteDto entetePretExterne;

    public PretExterneDetailDto() {
    }

    public PretExterneDetailDto(Integer id, Integer idEntreprise, String nprt, Date perb, BigDecimal echo, BigDecimal echr, BigDecimal inte, Integer nbul, PretExterneEnteteDto entetePretExterne) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.nprt = nprt;
        this.perb = perb;
        this.echo = echo;
        this.echr = echr;
        this.inte = inte;
        this.nbul = nbul;
        this.entetePretExterne = entetePretExterne;
    }

    public static PretExterneDetailDto fromEntity(PretExterneDetail pretInterne) {
        if (pretInterne == null) {
            return null;
        }
        PretExterneDetailDto dto = new  PretExterneDetailDto();
        BeanUtils.copyProperties(pretInterne, dto);
        return dto;
    }

    public static PretExterneDetail toEntity(PretExterneDetailDto dto) {
        if (dto == null) {
            return null;
        }
        PretExterneDetail pretExterneDetail = new PretExterneDetail();
        BeanUtils.copyProperties(dto, pretExterneDetail);
        return pretExterneDetail;
    }
}
