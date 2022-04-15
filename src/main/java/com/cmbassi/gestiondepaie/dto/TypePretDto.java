package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.TypePret;
import com.cmbassi.gestiondepaie.model.VirementSalarie;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class TypePretDto implements Serializable {
    private Integer id;
    private Integer idEntreprise;
    private String codenatpret;
    private String libeenatpret;
    private String codecompteur;
    private String crub;
    private String ncpt;
    private String refunique;
    private String monoecheance;
    private int nbechmax;
    private String intauto;
    private String taxauto;
    private String moisnremb1;
    private String moisnremb2;
    private String moisnremb3;
    private String moisnremb4;
    private String moisnremb5;
    private String moisnremb6;
    private String listsal1;
    private String listsal2;
    private String listsal3;
    private String listsal4;
    private String listsal5;
    private String listsal6;
    private String modecalcul;
    private Integer tauxint;
    private Integer tauxtaxe;
    private Long montantpremin;
    private Long montantpremax;
    private String wffilename;
    private String codeetbprt;
    private BigDecimal plafondmnt;
    private String plafondrub;

    public TypePretDto() {
    }

    public TypePretDto(Integer id, Integer idEntreprise, String codenatpret, String libeenatpret, String codecompteur, String crub, String ncpt, String refunique, String monoecheance, int nbechmax, String intauto, String taxauto, String moisnremb1, String moisnremb2, String moisnremb3, String moisnremb4, String moisnremb5, String moisnremb6, String listsal1, String listsal2, String listsal3, String listsal4, String listsal5, String listsal6, String modecalcul, Integer tauxint, Integer tauxtaxe, Long montantpremin, Long montantpremax, String wffilename, String codeetbprt, BigDecimal plafondmnt, String plafondrub) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.codenatpret = codenatpret;
        this.libeenatpret = libeenatpret;
        this.codecompteur = codecompteur;
        this.crub = crub;
        this.ncpt = ncpt;
        this.refunique = refunique;
        this.monoecheance = monoecheance;
        this.nbechmax = nbechmax;
        this.intauto = intauto;
        this.taxauto = taxauto;
        this.moisnremb1 = moisnremb1;
        this.moisnremb2 = moisnremb2;
        this.moisnremb3 = moisnremb3;
        this.moisnremb4 = moisnremb4;
        this.moisnremb5 = moisnremb5;
        this.moisnremb6 = moisnremb6;
        this.listsal1 = listsal1;
        this.listsal2 = listsal2;
        this.listsal3 = listsal3;
        this.listsal4 = listsal4;
        this.listsal5 = listsal5;
        this.listsal6 = listsal6;
        this.modecalcul = modecalcul;
        this.tauxint = tauxint;
        this.tauxtaxe = tauxtaxe;
        this.montantpremin = montantpremin;
        this.montantpremax = montantpremax;
        this.wffilename = wffilename;
        this.codeetbprt = codeetbprt;
        this.plafondmnt = plafondmnt;
        this.plafondrub = plafondrub;
    }

    public static TypePretDto fromEntity(TypePret typePret) {
        if (typePret == null) {
            return null;
        }
        TypePretDto dto = new  TypePretDto();
        BeanUtils.copyProperties(typePret, dto);
        return dto;
    }

    public static TypePret toEntity(TypePretDto dto) {
        if (dto == null) {
            return null;
        }
        TypePret virementSalarie = new TypePret();
        BeanUtils.copyProperties(dto, virementSalarie);
        return virementSalarie;
    }
}
