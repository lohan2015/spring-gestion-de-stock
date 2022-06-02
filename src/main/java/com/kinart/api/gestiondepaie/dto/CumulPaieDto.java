package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.CalculPaie;
import com.kinart.paie.business.model.CumulPaie;
import com.kinart.paie.business.model.HistoCalculPaie;
import com.kinart.stock.business.model.AbstractEntity;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des historiques de paie")
public class CumulPaieDto {

    private Integer idEntreprise;
    private String nmat;
    private String aamm;
    private String rubq;
    private Integer nbul;
    private BigDecimal basc;
    private BigDecimal basp;
    private BigDecimal taux;
    private BigDecimal mont;
    private String nomSalarie;
    private String libRubrique;
    private String typeRubrique;

    public static CumulPaieDto fromEntity(CumulPaie cumulPaie) {
        if (cumulPaie == null) {
            return null;
        }

        CumulPaieDto dto = new  CumulPaieDto();
        BeanUtils.copyProperties(cumulPaie, dto);
        return dto;
    }

    public static CumulPaieDto fromEntity(HistoCalculPaie cumulPaie) {
        if (cumulPaie == null) {
            return null;
        }

        CumulPaieDto dto = new  CumulPaieDto();
        BeanUtils.copyProperties(cumulPaie, dto);
        return dto;
    }
}
