package com.kinart.stock.business.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lignevente")
public class LigneVente extends AbstractEntity {

  @ManyToOne
  @JoinColumn(name = "idvente")
  private Ventes vente;

  @ManyToOne
  @JoinColumn(name = "idarticle")
  private Article article;

  @Column(name = "quantite")
  private BigDecimal quantite;

  @Column(name = "prixunitaire")
  private BigDecimal prixUnitaire;

  @Column(name = "identreprise")
  private Integer idEntreprise;

}
