package com.kinart.paie.business.model;

import java.io.Serializable;
import java.util.Date;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/** @author Hibernate CodeGenerator */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Auxiliaire")
@Table(name = "auxiliaire")
public class Auxiliaire extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "numtie", length = 20)
    private String numtie;

    @Column(name = "numcpt", length = 20)
    private String numcpt;

    @Column(name = "inttie", length = 200)
    private String inttie;

    @Column(name = "ractie", length = 20)
    private String ractie;

    @Column(name = "coddev", length = 10)
    private String coddev;

    @Column(name = "gesech", length = 20)
    private String gesech;

    @Column(name = "echnbj")
    private Integer echnbj;

    @Column(name = "echdep", length = 20)
    private String echdep;

    @Column(name = "echjou")
    private Integer echjou;

    @Column(name = "codmdr", length = 20)
    private String codmdr;

    @Column(name = "bqecod", length = 20)
    private String bqecod;

    @Column(name = "bqegui", length = 20)
    private String bqegui;

    @Column(name = "bqecpt", length = 20)
    private String bqecpt;

    @Column(name = "bqerib", length = 20)
    private String bqerib;

    @Column(name = "bqedom", length = 20)
    private String bqedom;

    @Column(name = "comm", length = 20)
    private String comm;

    @Column(name = "qualite", length = 20)
    private String qualite;

    @Column(name = "contact", length = 20)
    private String contact;

    @Column(name = "adresse1", length = 20)
    private String adresse1;

    @Column(name = "adresse2", length = 20)
    private String adresse2;

    @Column(name = "adresse3", length = 20)
    private String adresse3;

    @Column(name = "cpostal", length = 10)
    private String cpostal;

    @Column(name = "localite", length = 10)
    private String localite;

    @Column(name = "pays", length = 10)
    private String pays;

    @Column(name = "tel", length = 10)
    private String tel;

    @Column(name = "fax", length = 10)
    private String fax;

    @Column(name = "tlx", length = 10)
    private String tlx;

    @Column(name = "internet", length = 10)
    private String internet;

    @Column(name = "coduti", length = 20)
    private String coduti;

}
