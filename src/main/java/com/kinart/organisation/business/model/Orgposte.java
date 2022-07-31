package com.kinart.organisation.business.model;

import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;


/** 
 *     Métier ou Postes
 *     
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Orgposte")
@Table(name = "orgposte")
public class Orgposte extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codeposte", length = 10)
    private String codeposte;

    @Column(name = "codeorganigramme", length = 10)
    private String codeorganigramme;

    @Column(name = "niv1", length = 10)
    private String niv1;

    @Column(name = "niv2", length = 10)
    private String niv2;

    @Column(name = "niv3", length = 10)
    private String niv3;

    @Column(name = "fonc", length = 10)
    private String fonc;

    @Column(name = "clas", length = 10)
    private String clas;

    @Column(name = "cdfi", length = 10)
    private String cdfi;

    @Column(name = "cdsp", length = 10)
    private String cdsp;

    @Column(name = "tyfo", length = 10)
    private String tyfo;

    @Column(name = "nifo", length = 10)
    private String nifo;

    @Column(name = "agmin")
    private Integer agmin;

    @Column(name = "durmin")
    private Integer durmin;

    @Column(name = "durmax")
    private Integer durmax;

    @Column(name = "espmin")
    private BigDecimal espmin;

    @Column(name = "hora", length = 30)
    private String hora;

    @Column(name = "dispo", length = 10)
    private String dispo;

    @Column(name = "mobil", length = 10)
    private String mobil;

    @Column(name = "nuis", length = 10)
    private String nuis;

    @Column(name = "sexe", length = 10)
    private String sexe;

    @Column(name = "cat", length = 10)
    private String cat;

    @Column(name = "ech", length = 10)
    private String ech;

    @Column(name = "role", length = 2000)
    private String role;

    @Column(name = "resp", length = 2000)
    private String resp;

    @Column(name = "ccout", length = 20)
    private String ccout;

    @Column(name = "dtopo")
    private Date dtopo;

    @Column(name = "dtfpo")
    private Date dtfpo;

    @Column(name = "poids")
    private Integer poids;

    @Column(name = "site", length = 10)
    private String site;

    @Column(name = "lieutravail", length = 50)
    private String lieutravail;

    @Column(name = "libelle", length = 100)
    private String libelle;

    @Column(name = "codesite", length = 10)
    private String codesite;

    @Column(name = "fich")
    private Blob fich;

    @Column(name = "typmime", length = 30)
    private String typmime;

    @Column(name = "format", length = 30)
    private String format;

    @Column(name = "nomfich", length = 30)
    private String nomfich;

    @Column(name = "taille")
    private BigDecimal taille;

    @Column(name = "red", length = 50)
    private String red;

    @Column(name = "ver", length = 50)
    private String ver;

    @Column(name = "app", length = 50)
    private String app;

    @Column(name = "nomred", length = 50)
    private String nomred;

    @Column(name = "nomver", length = 50)
    private String nomver;

    @Column(name = "nomapp", length = 50)
    private String nomapp;

    @Column(name = "dtred")
    private Date dtred;

    @Column(name = "dtver")
    private Date dtver;

    @Column(name = "dtapp")
    private Date dtapp;

    @Column(name = "interim", length = 100)
    private String interim;
}
