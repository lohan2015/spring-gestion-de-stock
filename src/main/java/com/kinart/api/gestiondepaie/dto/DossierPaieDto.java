package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.DossierPaie;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DossierPaieDto {
    private Integer id;
    private Integer idEntreprise;

    private String nred;

    private String drsc;

    private String dad1;

    private String dad2;

    private String dad3;

    private String dad4;

    private String dcpo;

    private String dbdi;

    private String dpay;

    private String dnrc;

    private String dtst;

    private String dnst;

    private String dnac;

    private String dncp;

    private Date ddex;

    private Date dfex;

    private Date ddes;

    private Date dfes;

    private String dcjr;

    private String ddev;

    private String dcan;

    private String dnpe;

    private String dueb;

    private Date ddpa;

    private Integer dnsa;

    private Date ddcd;

    private Integer nddd;

    private Integer dmpa;

    private Integer npce;

    private Integer carr;

    private String gcli;

    private String gfou;

    private String rapp;

    private String maji;

    private Integer nbjv;

    private Integer ntmp;

    private Integer tmmp;

    private String serveur;

    private BigDecimal port;

    private String utilisateur;

    private String motpasse;

    private String mail;

    private String dnss;

    private String dncr;

    private String dnii;

    private String com1;

    private String com2;

    private Date ddmp;

    private String dseb;

    private String comp;

    private String dccg;

    private String dniv1;

    private String rniv1;

    private String bgnv1;

    private String bdnv1;

    private String dniv2;

    private String rniv2;

    private String bgnv2;

    private String bdnv2;

    private String dniv3;

    private String rniv3;

    private String bgnv3;

    private String bdnv3;

    private String dz1;

    private String tz1;

    private String dz2;

    private String tz2;

    private Integer dnbu;

    private String dniv4;

    private String rniv4;

    private String bgnv4;

    private String bdnv4;

    private int tbn4;

    public static DossierPaieDto fromEntity(DossierPaie dossierPaie) {
        if (dossierPaie == null) {
            return null;
        }

        DossierPaieDto dto = new  DossierPaieDto();
        BeanUtils.copyProperties(dossierPaie, dto);
        return dto;
    }

    public static DossierPaie toEntity(DossierPaieDto dossierPaieDto) {
        if (dossierPaieDto == null) {
            return null;
        }

        DossierPaie dossierPaie = new DossierPaie();
        BeanUtils.copyProperties(dossierPaieDto, dossierPaie);
        return dossierPaie;
    }
}
