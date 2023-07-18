package com.kinart.stock.business.model;

import javax.persistence.*;

import com.kinart.portail.business.utils.EnumOuiNon;
import com.kinart.portail.business.utils.EnumRole;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Roles")
@Table(name = "roles")
public class Roles extends AbstractEntity {

  @Column(name = "rolename", length = 20)
  @Enumerated(EnumType.STRING)
  private EnumRole roleName = EnumRole.USER;

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
