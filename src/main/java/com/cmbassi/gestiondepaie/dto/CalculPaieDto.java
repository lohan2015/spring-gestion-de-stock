package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.CaisseMutuelleSalarie;
import com.cmbassi.gestiondepaie.model.CalculPaie;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@Builder
public class CalculPaieDto {

    private Integer idEntreprise;

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

    public CalculPaieDto() {
    }

    public CalculPaieDto(Integer idEntreprise, String nmat, String aamm, Integer nbul, String rubq, BigDecimal basc, BigDecimal basp, BigDecimal taux, BigDecimal mont, String nprt, String ruba, String argu, String clas, String trtb) {
        this.idEntreprise = idEntreprise;
        this.nmat = nmat;
        this.aamm = aamm;
        this.nbul = nbul;
        this.rubq = rubq;
        this.basc = basc;
        this.basp = basp;
        this.taux = taux;
        this.mont = mont;
        this.nprt = nprt;
        this.ruba = ruba;
        this.argu = argu;
        this.clas = clas;
        this.trtb = trtb;
    }

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
