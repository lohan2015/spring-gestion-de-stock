package com.kinart.api.gestiondepaie.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class LigneEcheancierDto {
    private String strDate = null;
    private String strMontant = null;
    private String strAmortissement = null;
    private String strResteARembourser = null;
    private String strInteret = null;
    private String strTaxe = null;

    private Date dtDate;
    private BigDecimal bgMontant;
    private BigDecimal bgAmortissement;
    private BigDecimal bgResteARembourser;
    private BigDecimal bgInterest;
    private BigDecimal bgTaxe;
    private BigDecimal resteInitial;

    public LigneEcheancierDto()
    {

    }

    public LigneEcheancierDto(String strDate, String strMontant, String strAmortissement, String strResteARembourser, String strInteret, String strTaxe, Date dtDate, BigDecimal bgMontant, BigDecimal bgAmortissement, BigDecimal bgResteARembourser, BigDecimal bgInterest, BigDecimal bgTaxe, BigDecimal resteInitial) {
        this.strDate = strDate;
        this.strMontant = strMontant;
        this.strAmortissement = strAmortissement;
        this.strResteARembourser = strResteARembourser;
        this.strInteret = strInteret;
        this.strTaxe = strTaxe;
        this.dtDate = dtDate;
        this.bgMontant = bgMontant;
        this.bgAmortissement = bgAmortissement;
        this.bgResteARembourser = bgResteARembourser;
        this.bgInterest = bgInterest;
        this.bgTaxe = bgTaxe;
        this.resteInitial = resteInitial;
    }
}
