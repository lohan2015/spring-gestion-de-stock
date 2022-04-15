package com.cmbassi.gestiondepaie.model;

import java.math.BigDecimal;

import com.cmbassi.gestiondestock.model.AbstractEntity;
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
@Table(name = "typepret")
public class TypePret extends AbstractEntity {

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @Column(name = "codenatpret", length = 10)
    private String codenatpret;

    @Column(name = "libeenatpret", length = 100)
    private String libeenatpret;

    @Column(name = "codecompteur", length = 10)
    private String codecompteur;

    @Column(name = "crub", length = 10)
    private String crub;

    @Column(name = "ncpt", length = 20)
    private String ncpt;

    @Column(name = "refunique", length = 10)
    private String refunique;

    @Column(name = "monoecheance", length = 10)
    private String monoecheance;

    @Column(name = "nbechmax")
    private int nbechmax;

    @Column(name = "intauto", length = 10)
    private String intauto;

    @Column(name = "taxauto", length = 10)
    private String taxauto;

    @Column(name = "moisnremb1", length = 10)
    private String moisnremb1;

    @Column(name = "moisnremb2", length = 10)
    private String moisnremb2;

    @Column(name = "moisnremb3", length = 10)
    private String moisnremb3;

    @Column(name = "moisnremb4", length = 10)
    private String moisnremb4;

    @Column(name = "moisnremb5", length = 10)
    private String moisnremb5;

    @Column(name = "moisnremb6", length = 10)
    private String moisnremb6;

    @Column(name = "listsal1", length = 10)
    private String listsal1;

    @Column(name = "listsal2", length = 10)
    private String listsal2;

    @Column(name = "listsal3", length = 10)
    private String listsal3;

    @Column(name = "listsal4", length = 10)
    private String listsal4;

    @Column(name = "listsal5", length = 10)
    private String listsal5;

    @Column(name = "listsal6", length = 10)
    private String listsal6;

    @Column(name = "modecalcul", length = 10)
    private String modecalcul;

    @Column(name = "tauxint")
    private Integer tauxint;

    @Column(name = "tauxtaxe")
    private Integer tauxtaxe;

    @Column(name = "montantpremin")
    private Long montantpremin;

    @Column(name = "montantpremax")
    private Long montantpremax;

    @Column(name = "wffilename", length = 10)
    private String wffilename;

    @Column(name = "codeetbprt", length = 10)
    private String codeetbprt;

    @Column(name = "plafondmnt")
    private BigDecimal plafondmnt;

    @Column(name = "plafondrub", length = 20)
    private String plafondrub;

}
