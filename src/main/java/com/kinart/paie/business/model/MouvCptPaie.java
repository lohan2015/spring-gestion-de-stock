package com.kinart.paie.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "MouvCptPaie")
@Table(name = "mouvcptpaie")
public class MouvCptPaie extends AbstractEntity {

    @Column(name = "age", length = 50)
    private String age;

    @Column(name = "dev", length = 10)
    private String dev;

    @Column(name = "cha", length = 20)
    private String cha;

    @Column(name = "ncp", length = 50)
    private String ncp;

    @Column(name = "suf", length = 20)
    private String suf;

    @Column(name = "ope", length = 20)
    private String ope;

    @Column(name = "mvt", length = 200)
    private String mvt;

    @Column(name = "rgp", length = 20)
    private String rgp;

    @Column(name = "uti", length = 20)
    private String uti;

    @Column(name = "eve", length = 20)
    private String eve;

    @Column(name = "clc", length = 20)
    private String clc;

    @Column(name = "dco")
    private Date dco;

    @Column(name = "ser", length = 20)
    private String ser;

    @Column(name = "dva")
    private Date dva;

    @Column(name = "mon")
    private BigDecimal mon;

    @Column(name = "sen", length = 20)
    private String sen;

    @Column(name = "lib", length = 200)
    private String lib;

    @Column(name = "exo", length = 20)
    private String exo;

    @Column(name = "pie", length = 20)
    private String pie;

    @Column(name = "rlet", length = 20)
    private String rlet;

    @Column(name = "des1", length = 20)
    private String des1;

    @Column(name = "des2", length = 20)
    private String des2;

    @Column(name = "des3", length = 20)
    private String des3;

    @Column(name = "des4", length = 20)
    private String des4;

    @Column(name = "des5", length = 20)
    private String des5;

    @Column(name = "utf", length = 20)
    private String utf;

    @Column(name = "uta", length = 20)
    private String uta;

    @Column(name = "tau")
    private BigDecimal tau;

    @Column(name = "din")
    private Date din;

    @Column(name = "tpr", length = 20)
    private String tpr;

    @Column(name = "npr")
    private Long npr;

    @Column(name = "ncc", length = 20)
    private String ncc;

    @Column(name = "suc", length = 20)
    private String suc;

    @Column(name = "esi", length = 20)
    private String esi;

    @Column(name = "imp", length = 20)
    private String imp;

    @Column(name = "cta", length = 20)
    private String cta;

    @Column(name = "mar", length = 20)
    private String mar;

    @Column(name = "dech")
    private Date dech;

    @Column(name = "agsa", length = 20)
    private String agsa;

    @Column(name = "agem", length = 20)
    private String agem;

    @Column(name = "agde", length = 20)
    private String agde;

    @Column(name = "devc", length = 20)
    private String devc;

    @Column(name = "mctv")
    private BigDecimal mctv;

    @Column(name = "pieo", length = 20)
    private String pieo;

    @Column(name = "iden", length = 20)
    private String iden;

    @Column(name = "noseq")
    private Integer noseq;

}
