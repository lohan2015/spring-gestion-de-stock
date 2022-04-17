package com.kinart.stock.business.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.*;

/**
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category")
public class Category extends AbstractEntity {

  @Column(name = "code")
  private String code;

  @Column(name = "designation")
  private String designation;

  @Column(name = "identreprise")
  private Integer idEntreprise;

  @OneToMany(mappedBy = "category")
  private List<Article> articles;

}
