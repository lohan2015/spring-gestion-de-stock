package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.PretInterne;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des prêts internes")
public class PretInterneDto implements Serializable {

    private Integer idEntreprise;
    private Integer id;
    private Integer lg;
    private String nmat;
    private String crub;
    private BigDecimal mtpr;
    private BigDecimal nbmens;
    private BigDecimal mtmens;
    private String premrb;
    private BigDecimal mtremb;
    private String etatpr;

    private String nomSalarie;
    private String librubrique;
    private String mode;

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
