package com.cmbassi.gestiondestock.dto;

import com.cmbassi.gestiondestock.model.CommandeFournisseur;
import com.cmbassi.gestiondestock.model.LigneCommandeFournisseur;
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
