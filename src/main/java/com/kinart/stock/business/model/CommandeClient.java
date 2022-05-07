package com.kinart.stock.business.model;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "commandeclient")
public class CommandeClient extends AbstractEntity {

  @Column(name = "code")
  private String code;

  @Column(name = "datecommande")
  private Date dateCommande;

  @Column(name = "etatcommande")
  @Enumerated(EnumType.STRING)
  private EtatCommande etatCommande;

  @Column(name = "identreprise")
  private Integer idEntreprise;

  @ManyToOne
  @JoinColumn(name = "idclient")
  private Client client;

  @OneToMany(mappedBy = "commandeClient")
  private List<LigneCommandeClient> ligneCommandeClients;

}
