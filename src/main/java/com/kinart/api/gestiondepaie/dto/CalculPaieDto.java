package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.CalculPaie;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des calculs")
public class CalculPaieDto {
    private Integer id;
    private Integer idEntreprise;

    private Integer nlig;

    private String nmat;

    private String aamm;

    private Integer nbul;

    private String rubq;

    private BigDecimal basc;

    private BigDecimal basp;

    private BigDecimal taux;

    private BigDecimal mont;

    private String nprt;

    private String ruba;

    private String argu;

    private String clas;

    private String trtb;

    private String nomSalarie;

    private String libRubrique;

    private String typeRubrique;

    public static CalculPaieDto fromEntity(CalculPaie calculPaie) {
        if (calculPaie == null) {
            return null;
        }

        CalculPaieDto dto = new  CalculPaieDto();
        BeanUtils.copyProperties(calculPaie, dto);
        return dto;
    }

    public static CalculPaie toEntity(CalculPaieDto calculPaieDto) {
        if (calculPaieDto == null) {
            return null;
        }

        CalculPaie calculPaie = new CalculPaie();
        BeanUtils.copyProperties(calculPaieDto, calculPaie);
        return calculPaie;
    }
}
