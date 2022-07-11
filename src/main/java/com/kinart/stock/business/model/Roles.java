package com.kinart.stock.business.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "roles")
public class Roles extends AbstractEntity {

  @Column(name = "rolename")
  private String roleName;

  @Column(name = "module", length = 20)
  private String module;

  @Column(name = "droit", length = 1)
  private String droit;

  @Column(name = "actif", length = 1)
  private String actif;

  @Column(name = "dateDeb")
  private Date dateDeb;

  @Column(name = "dateFin")
  private Date dateFin;

  @ManyToOne
  @JoinColumn(name = "idutilisateur")
  private Utilisateur utilisateur;

}
