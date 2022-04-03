package com.cmbassi.gestiondestock.dto;

import com.cmbassi.gestiondestock.model.MvtStk;
import com.cmbassi.gestiondestock.model.SourceMvtStk;
import com.cmbassi.gestiondestock.model.TypeMvtStk;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvtStkDto {

  private Integer id;

  private Instant dateMvt;

  private BigDecimal quantite;

  private ArticleDto article;

  private TypeMvtStk typeMvt;

  private SourceMvtStk sourceMvt;

  private Integer idEntreprise;

  public MvtStkDto() {
  }

  public MvtStkDto(Integer id, Instant dateMvt, BigDecimal quantite, ArticleDto article, TypeMvtStk typeMvt, SourceMvtStk sourceMvt, Integer idEntreprise) {
    this.id = id;
    this.dateMvt = dateMvt;
    this.quantite = quantite;
    this.article = article;
    this.typeMvt = typeMvt;
    this.sourceMvt = sourceMvt;
    this.idEntreprise = idEntreprise;
  }

  public static MvtStkDto fromEntity(MvtStk mvtStk) {
    if (mvtStk == null) {
      return null;
    }

    return MvtStkDto.builder()
        .id(mvtStk.getId())
        .dateMvt(mvtStk.getDateMvt())
        .quantite(mvtStk.getQuantite())
        .article(ArticleDto.fromEntity(mvtStk.getArticle()))
        .typeMvt(mvtStk.getTypeMvt())
        .sourceMvt(mvtStk.getSourceMvt())
        .idEntreprise(mvtStk.getIdEntreprise())
        .build();
  }

  public static MvtStk toEntity(MvtStkDto dto) {
    if (dto == null) {
      return null;
    }

    MvtStk mvtStk = new MvtStk();
    mvtStk.setId(dto.getId());
    mvtStk.setDateMvt(dto.getDateMvt());
    mvtStk.setQuantite(dto.getQuantite());
    mvtStk.setArticle(ArticleDto.toEntity(dto.getArticle()));
    mvtStk.setTypeMvt(dto.getTypeMvt());
    mvtStk.setSourceMvt(dto.getSourceMvt());
    mvtStk.setIdEntreprise(dto.getIdEntreprise());
    return mvtStk;
  }
}
