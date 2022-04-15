package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.DossierPaie;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
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

    public DossierPaieDto() {
    }

    public DossierPaieDto(Integer id, Integer idEntreprise, String nred, String drsc, String dad1, String dad2, String dad3, String dad4, String dcpo, String dbdi, String dpay, String dnrc, String dtst, String dnst, String dnac, String dncp, Date ddex, Date dfex, Date ddes, Date dfes, String dcjr, String ddev, String dcan, String dnpe, String dueb, Date ddpa, Integer dnsa, Date ddcd, Integer nddd, Integer dmpa, Integer npce, Integer carr, String gcli, String gfou, String rapp, String maji, Integer nbjv, Integer ntmp, Integer tmmp, String serveur, BigDecimal port, String utilisateur, String motpasse, String mail, String dnss, String dncr, String dnii, String com1, String com2, Date ddmp, String dseb, String comp, String dccg, String dniv1, String rniv1, String bgnv1, String bdnv1, String dniv2, String rniv2, String bgnv2, String bdnv2, String dniv3, String rniv3, String bgnv3, String bdnv3, String dz1, String tz1, String dz2, String tz2, Integer dnbu, String dniv4, String rniv4, String bgnv4, String bdnv4, int tbn4) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.nred = nred;
        this.drsc = drsc;
        this.dad1 = dad1;
        this.dad2 = dad2;
        this.dad3 = dad3;
        this.dad4 = dad4;
        this.dcpo = dcpo;
        this.dbdi = dbdi;
        this.dpay = dpay;
        this.dnrc = dnrc;
        this.dtst = dtst;
        this.dnst = dnst;
        this.dnac = dnac;
        this.dncp = dncp;
        this.ddex = ddex;
        this.dfex = dfex;
        this.ddes = ddes;
        this.dfes = dfes;
        this.dcjr = dcjr;
        this.ddev = ddev;
        this.dcan = dcan;
        this.dnpe = dnpe;
        this.dueb = dueb;
        this.ddpa = ddpa;
        this.dnsa = dnsa;
        this.ddcd = ddcd;
        this.nddd = nddd;
        this.dmpa = dmpa;
        this.npce = npce;
        this.carr = carr;
        this.gcli = gcli;
        this.gfou = gfou;
        this.rapp = rapp;
        this.maji = maji;
        this.nbjv = nbjv;
        this.ntmp = ntmp;
        this.tmmp = tmmp;
        this.serveur = serveur;
        this.port = port;
        this.utilisateur = utilisateur;
        this.motpasse = motpasse;
        this.mail = mail;
        this.dnss = dnss;
        this.dncr = dncr;
        this.dnii = dnii;
        this.com1 = com1;
        this.com2 = com2;
        this.ddmp = ddmp;
        this.dseb = dseb;
        this.comp = comp;
        this.dccg = dccg;
        this.dniv1 = dniv1;
        this.rniv1 = rniv1;
        this.bgnv1 = bgnv1;
        this.bdnv1 = bdnv1;
        this.dniv2 = dniv2;
        this.rniv2 = rniv2;
        this.bgnv2 = bgnv2;
        this.bdnv2 = bdnv2;
        this.dniv3 = dniv3;
        this.rniv3 = rniv3;
        this.bgnv3 = bgnv3;
        this.bdnv3 = bdnv3;
        this.dz1 = dz1;
        this.tz1 = tz1;
        this.dz2 = dz2;
        this.tz2 = tz2;
        this.dnbu = dnbu;
        this.dniv4 = dniv4;
        this.rniv4 = rniv4;
        this.bgnv4 = bgnv4;
        this.bdnv4 = bdnv4;
        this.tbn4 = tbn4;
    }

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
