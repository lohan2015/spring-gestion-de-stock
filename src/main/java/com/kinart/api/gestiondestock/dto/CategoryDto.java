package com.kinart.api.gestiondestock.dto;

import com.kinart.stock.business.model.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {

  private Integer id;

  private String code;

  private String designation;

  private Integer idEntreprise;

  @JsonIgnore
  private List<ArticleDto> articles;

  public CategoryDto() {
  }

  public CategoryDto(Integer id, String code, String designation, Integer idEntreprise, List<ArticleDto> articles) {
    this.id = id;
    this.code = code;
    this.designation = designation;
    this.idEntreprise = idEntreprise;
    this.articles = articles;
  }

  public static CategoryDto fromEntity(Category category) {
    if (category == null) {
      return null;
      // TODO throw an exception
    }

    return CategoryDto.builder()
        .id(category.getId())
        .code(category.getCode())
        .designation(category.getDesignation())
        .idEntreprise(category.getIdEntreprise())
        .build();
  }

  public static Category toEntity(CategoryDto categoryDto) {
    if (categoryDto == null) {
      return null;
      // TODO throw an exception
    }

    Category category = new Category();
    category.setId(categoryDto.getId());
    category.setCode(categoryDto.getCode());
    category.setDesignation(categoryDto.getDesignation());
    category.setIdEntreprise(categoryDto.getIdEntreprise());

    return category;
  }
}
