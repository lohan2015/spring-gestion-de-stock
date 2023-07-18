package com.kinart.stock.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.List;
import javax.persistence.*;

import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.model.DemandeAttestation;
import com.kinart.portail.business.model.DemandeHabilitation;
import com.kinart.portail.business.model.DemandePret;
import com.kinart.portail.business.utils.EnumOuiNon;
import com.kinart.portail.business.utils.EnumStatusType;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Utilisateur")
@Table(name = "utilisateur")
public class Utilisateur extends AbstractEntity {

  @Column(name = "nom")
  private String nom;

  @Column(name = "prenom")
  private String prenom;

  @Column(name = "email")
  private String email;

  @Column(name = "datedenaissance")
  private Instant dateDeNaissance;

  @Column(name = "motdepasse")
  private String moteDePasse;

  @Column(name = "clang", length = 10)
  private String clang;

  @Embedded
  private Adresse adresse;

  @Column(name = "photo")
  private String photo;

  @Column(name = "valid1", length = 100)
  private String valid1;

  @Column(name = "valid2", length = 100)
  private String valid2;

  @Column(name = "valid3", length = 100)
  private String valid3;

  @Column(name = "valid4", length = 100)
  private String valid4;

  @Column(name = "scepersonnel", length = 5)
  @Enumerated(EnumType.STRING)
  private EnumOuiNon scepersonnel = EnumOuiNon.NON;

  @Column(name = "drhl", length = 5)
  @Enumerated(EnumType.STRING)
  private EnumOuiNon drhl = EnumOuiNon.NON;

  @Column(name = "dga", length = 5)
  @Enumerated(EnumType.STRING)
  private EnumOuiNon dga = EnumOuiNon.NON;

  @Column(name = "dg", length = 5)
  @Enumerated(EnumType.STRING)
  private EnumOuiNon dg = EnumOuiNon.NON;

  @ManyToOne
  @JoinColumn(name = "identreprise")
  private Entreprise entreprise;


  @OneToMany(fetch = FetchType.EAGER, mappedBy = "utilisateur")
  @JsonIgnore
  private List<Roles> roles;

  /*@OneToOne
  private Roles role;*/

 /* @OneToMany(fetch = FetchType.LAZY, mappedBy = "userDemPret")
  @JsonIgnore
  private List<DemandePret> demandesPret;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "userDemAbsCg")
  @JsonIgnore
  private List<DemandeAbsenceConge> demandesAbsConge;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "userDemAttest")
  @JsonIgnore
  private List<DemandeAttestation> demandesAttestation;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "userDemHabil")
  @JsonIgnore
  private List<DemandeHabilitation> demandesHabilitation;*/

}
