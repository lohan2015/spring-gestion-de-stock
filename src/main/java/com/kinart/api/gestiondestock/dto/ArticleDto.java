package com.kinart.api.gestiondestock.dto;

import com.kinart.stock.business.model.Article;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ArticleDto {

  private Integer id;

  private String codeArticle;

  private String designation;

  private BigDecimal prixUnitaireHt;

  private BigDecimal tauxTva;

  private BigDecimal prixUnitaireTtc;

  private String photo;

  private CategoryDto category;

  private Integer idEntreprise;

  public ArticleDto() {
  }

  public ArticleDto(Integer id, String codeArticle, String designation, BigDecimal prixUnitaireHt, BigDecimal tauxTva, BigDecimal prixUnitaireTtc, String photo, CategoryDto category, Integer idEntreprise) {
    this.id = id;
    this.codeArticle = codeArticle;
    this.designation = designation;
    this.prixUnitaireHt = prixUnitaireHt;
    this.tauxTva = tauxTva;
    this.prixUnitaireTtc = prixUnitaireTtc;
    this.photo = photo;
    this.category = category;
    this.idEntreprise = idEntreprise;
  }

  public static ArticleDto fromEntity(Article article) {
    if (article == null) {
      return null;
    }
    return ArticleDto.builder()
        .id(article.getId())
        .codeArticle(article.getCodeArticle())
        .designation(article.getDesignation())
        .photo(article.getPhoto())
        .prixUnitaireHt(article.getPrixUnitaireHt())
        .prixUnitaireTtc(article.getPrixUnitaireTtc())
        .tauxTva(article.getTauxTva())
        .idEntreprise(article.getIdEntreprise())
        .category(CategoryDto.fromEntity(article.getCategory()))
        .build();
  }

  public static Article toEntity(ArticleDto articleDto) {
    if (articleDto == null) {
      return null;
    }
    Article article = new Article();
    article.setId(articleDto.getId());
    article.setCodeArticle(articleDto.getCodeArticle());
    article.setDesignation(articleDto.getDesignation());
    article.setPhoto(articleDto.getPhoto());
    article.setPrixUnitaireHt(articleDto.getPrixUnitaireHt());
    article.setPrixUnitaireTtc(articleDto.getPrixUnitaireTtc());
    article.setTauxTva(articleDto.getTauxTva());
    article.setIdEntreprise(articleDto.getIdEntreprise());
    article.setCategory(CategoryDto.toEntity(articleDto.getCategory()));
    return article;
  }

}
