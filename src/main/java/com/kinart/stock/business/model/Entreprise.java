package com.kinart.stock.business.model;

import java.util.List;
import javax.persistence.*;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "entreprise")
public class Entreprise extends AbstractEntity {

  @Column(name = "nom")
  private String nom;

  @Column(name = "description")
  private String description;

  @Embedded
  private Adresse adresse;

  @Column(name = "codefiscal")
  private String codeFiscal;

  @Column(name = "photo")
  private String photo;

  @Column(name = "email")
  private String email;

  @Column(name = "numtel")
  private String numTel;

  @Column(name = "siteweb")
  private String steWeb;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "entreprise")
  private List<Utilisateur> utilisateurs;

}
