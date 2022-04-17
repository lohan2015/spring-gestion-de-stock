package com.kinart.paie.business.model;

import java.math.BigDecimal;
import java.util.Date;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dossierpaie")
public class DossierPaie extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "nred", length = 20)
    private String nred;

    @Column(name = "drsc", length = 30)
    private String drsc;

    @Column(name = "dad1", length = 50)
    private String dad1;

    @Column(name = "dad2", length = 50)
    private String dad2;

    @Column(name = "dad3", length = 50)
    private String dad3;

    @Column(name = "dad4", length = 50)
    private String dad4;

    @Column(name = "dcpo", length = 30)
    private String dcpo;

    @Column(name = "dbdi", length = 30)
    private String dbdi;

    @Column(name = "dpay", length = 30)
    private String dpay;

    @Column(name = "dnrc", length = 30)
    private String dnrc;

    @Column(name = "dtst", length = 30)
    private String dtst;

    @Column(name = "dnst", length = 30)
    private String dnst;

    @Column(name = "dnac", length = 30)
    private String dnac;

    @Column(name = "dncp", length = 30)
    private String dncp;

    @Column(name = "ddex")
    private Date ddex;

    @Column(name = "dfex")
    private Date dfex;

    @Column(name = "ddes")
    private Date ddes;

    @Column(name = "dfes")
    private Date dfes;

    @Column(name = "dcjr", length = 30)
    private String dcjr;

    @Column(name = "ddev", length = 10)
    private String ddev;

    @Column(name = "dcan", length = 30)
    private String dcan;

    @Column(name = "dnpe", length = 30)
    private String dnpe;

    @Column(name = "dueb", length = 30)
    private String dueb;

    @Column(name = "ddpa")
    private Date ddpa;

    @Column(name = "dnsa")
    private Integer dnsa;

    @Column(name = "ddcd")
    private Date ddcd;

    @Column(name = "nddd")
    private Integer nddd;

    @Column(name = "dmpa")
    private Integer dmpa;

    @Column(name = "npce")
    private Integer npce;

    @Column(name = "carr")
    private Integer carr;

    @Column(name = "gcli", length = 30)
    private String gcli;

    @Column(name = "gfou", length = 30)
    private String gfou;

    @Column(name = "rapp", length = 30)
    private String rapp;

    @Column(name = "maji", length = 30)
    private String maji;

    @Column(name = "nbjv")
    private Integer nbjv;

    @Column(name = "ntmp")
    private Integer ntmp;

    @Column(name = "tmmp")
    private Integer tmmp;

    @Column(name = "serveur", length = 30)
    private String serveur;

    @Column(name = "port")
    private BigDecimal port;

    @Column(name = "utilisateur", length = 30)
    private String utilisateur;

    @Column(name = "motpasse", length = 30)
    private String motpasse;

    @Column(name = "mail", length = 30)
    private String mail;

    @Column(name = "dnss", length = 30)
    private String dnss;

    @Column(name = "dncr", length = 30)
    private String dncr;

    @Column(name = "dnii", length = 30)
    private String dnii;

    @Column(name = "com1", length = 30)
    private String com1;

    @Column(name = "com2", length = 30)
    private String com2;

    @Column(name = "ddmp")
    private Date ddmp;

    @Column(name = "dseb", length = 30)
    private String dseb;

    @Column(name = "comp", length = 30)
    private String comp;

    @Column(name = "dccg", length = 30)
    private String dccg;

    @Column(name = "dniv1", length = 30)
    private String dniv1;

    @Column(name = "rniv1", length = 30)
    private String rniv1;

    @Column(name = "bgnv1", length = 30)
    private String bgnv1;

    @Column(name = "bdnv1", length = 30)
    private String bdnv1;

    @Column(name = "dniv2", length = 30)
    private String dniv2;

    @Column(name = "rniv2", length = 30)
    private String rniv2;

    @Column(name = "bgnv2", length = 30)
    private String bgnv2;

    @Column(name = "bdnv2", length = 30)
    private String bdnv2;

    @Column(name = "dniv3", length = 30)
    private String dniv3;

    @Column(name = "rniv3", length = 30)
    private String rniv3;

    @Column(name = "bgnv3", length = 30)
    private String bgnv3;

    @Column(name = "bdnv3", length = 30)
    private String bdnv3;

    @Column(name = "dz1", length = 30)
    private String dz1;

    @Column(name = "tz1", length = 30)
    private String tz1;

    @Column(name = "dz2", length = 30)
    private String dz2;

    @Column(name = "tz2", length = 30)
    private String tz2;

    @Column(name = "dnbu")
    private Integer dnbu;

    @Column(name = "dniv4", length = 30)
    private String dniv4;

    @Column(name = "rniv4", length = 30)
    private String rniv4;

    @Column(name = "bgnv4")
    private String bgnv4;

    @Column(name = "bdnv4", length = 30)
    private String bdnv4;

    @Column(name = "tbn4")
    private int tbn4;
}
