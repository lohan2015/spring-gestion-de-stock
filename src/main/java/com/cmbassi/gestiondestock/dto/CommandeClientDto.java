package com.cmbassi.gestiondestock.dto;

import com.cmbassi.gestiondestock.model.CommandeClient;
import com.cmbassi.gestiondestock.model.EtatCommande;

import java.time.Instant;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommandeClientDto {

  private Integer id;

  private String code;

  private Instant dateCommande;

  private EtatCommande etatCommande;

  private ClientDto client;

  private Integer idEntreprise;

  private List<LigneCommandeClientDto> ligneCommandeClients;

  public CommandeClientDto() {
  }

  public CommandeClientDto(Integer id, String code, Instant dateCommande, EtatCommande etatCommande, ClientDto client, Integer idEntreprise, List<LigneCommandeClientDto> ligneCommandeClients) {
    this.id = id;
    this.code = code;
    this.dateCommande = dateCommande;
    this.etatCommande = etatCommande;
    this.client = client;
    this.idEntreprise = idEntreprise;
    this.ligneCommandeClients = ligneCommandeClients;
  }

  public static CommandeClientDto fromEntity(CommandeClient commandeClient) {
    if (commandeClient == null) {
      return null;
    }
    return CommandeClientDto.builder()
        .id(commandeClient.getId())
        .code(commandeClient.getCode())
        .dateCommande(commandeClient.getDateCommande())
        .etatCommande(commandeClient.getEtatCommande())
        .client(ClientDto.fromEntity(commandeClient.getClient()))
        .idEntreprise(commandeClient.getIdEntreprise())
        .build();

  }

  public static CommandeClient toEntity(CommandeClientDto dto) {
    if (dto == null) {
      return null;
    }
    CommandeClient commandeClient = new CommandeClient();
    commandeClient.setId(dto.getId());
    commandeClient.setCode(dto.getCode());
    commandeClient.setClient(ClientDto.toEntity(dto.getClient()));
    commandeClient.setDateCommande(dto.getDateCommande());
    commandeClient.setEtatCommande(dto.getEtatCommande());
    commandeClient.setIdEntreprise(dto.getIdEntreprise());
    return commandeClient;
  }

  public boolean isCommandeLivree() {
    return EtatCommande.LIVREE.equals(this.etatCommande);
  }
}
