package com.kinart.api.gestiondestock.dto;

import com.kinart.stock.business.model.CommandeFournisseur;
import com.kinart.stock.business.model.LigneCommandeFournisseur;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LigneCommandeFournisseurDto {

  private Integer id;

  private ArticleDto article;

  private CommandeFournisseur commandeFournisseur;

  private BigDecimal quantite;

  private BigDecimal prixUnitaire;

  private Integer idEntreprise;

  private String codeArticle;

  private String libelleArticle;

  public LigneCommandeFournisseurDto() {
  }

  public LigneCommandeFournisseurDto(Integer id, ArticleDto article, CommandeFournisseur commandeFournisseur, BigDecimal quantite, BigDecimal prixUnitaire, Integer idEntreprise) {
    this.id = id;
    this.article = article;
    this.commandeFournisseur = commandeFournisseur;
    this.quantite = quantite;
    this.prixUnitaire = prixUnitaire;
    this.idEntreprise = idEntreprise;
  }

  public LigneCommandeFournisseurDto(Integer id, ArticleDto article, CommandeFournisseur commandeFournisseur, BigDecimal quantite, BigDecimal prixUnitaire, Integer idEntreprise, String codeArticle, String libelleArticle) {
    this.id = id;
    this.article = article;
    this.commandeFournisseur = commandeFournisseur;
    this.quantite = quantite;
    this.prixUnitaire = prixUnitaire;
    this.idEntreprise = idEntreprise;
    this.codeArticle = codeArticle;
    this.libelleArticle = libelleArticle;
  }

  public static LigneCommandeFournisseurDto fromEntity(LigneCommandeFournisseur ligneCommandeFournisseur) {
    if (ligneCommandeFournisseur == null) {
      return null;
    }
    return LigneCommandeFournisseurDto.builder()
        .id(ligneCommandeFournisseur.getId())
        .article(ArticleDto.fromEntity(ligneCommandeFournisseur.getArticle()))
        .quantite(ligneCommandeFournisseur.getQuantite())
        .prixUnitaire(ligneCommandeFournisseur.getPrixUnitaire())
        .idEntreprise(ligneCommandeFournisseur.getIdEntreprise())
        .build();
  }

  public static LigneCommandeFournisseur toEntity(LigneCommandeFournisseurDto dto) {
    if (dto == null) {
      return null;
    }

    LigneCommandeFournisseur ligneCommandeFournisseur = new LigneCommandeFournisseur();
    ligneCommandeFournisseur.setId(dto.getId());
    ligneCommandeFournisseur.setArticle(ArticleDto.toEntity(dto.getArticle()));
    ligneCommandeFournisseur.setPrixUnitaire(dto.getPrixUnitaire());
    ligneCommandeFournisseur.setQuantite(dto.getQuantite());
    ligneCommandeFournisseur.setIdEntreprise(dto.getIdEntreprise());
    return ligneCommandeFournisseur;
  }

}
