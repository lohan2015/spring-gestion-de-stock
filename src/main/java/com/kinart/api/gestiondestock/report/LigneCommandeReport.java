package com.kinart.api.gestiondestock.report;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LigneCommandeReport {

    private String codeArticle;
    private String libelleArticle;
    private BigDecimal quantite = BigDecimal.ZERO;
    private BigDecimal prixUnitaire = BigDecimal.ZERO;
    private BigDecimal prixTtc = BigDecimal.ZERO;

    public LigneCommandeReport() {

    }

    public LigneCommandeReport(String codeArticle, String libelleArticle, BigDecimal quantite, BigDecimal prixUnitaire, BigDecimal prixTtc) {
        this.codeArticle = codeArticle;
        this.libelleArticle = libelleArticle;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.prixTtc = prixTtc;
    }
}
