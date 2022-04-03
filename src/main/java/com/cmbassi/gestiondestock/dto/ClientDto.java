package com.cmbassi.gestiondestock.dto;

import com.cmbassi.gestiondestock.model.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDto {

  private Integer id;

  private String nom;

  private String prenom;

  private AdresseDto adresse;

  private String photo;

  private String mail;

  private String numTel;

  private Integer idEntreprise;

  @JsonIgnore
  private List<CommandeClientDto> commandeClients;

  public ClientDto() {
  }

  public ClientDto(Integer id, String nom, String prenom, AdresseDto adresse, String photo, String mail, String numTel, Integer idEntreprise, List<CommandeClientDto> commandeClients) {
    this.id = id;
    this.nom = nom;
    this.prenom = prenom;
    this.adresse = adresse;
    this.photo = photo;
    this.mail = mail;
    this.numTel = numTel;
    this.idEntreprise = idEntreprise;
    this.commandeClients = commandeClients;
  }

  public static ClientDto fromEntity(Client client) {
    if (client == null) {
      return null;
    }
    return ClientDto.builder()
        .id(client.getId())
        .nom(client.getNom())
        .prenom(client.getPrenom())
        .adresse(AdresseDto.fromEntity(client.getAdresse()))
        .photo(client.getPhoto())
        .mail(client.getMail())
        .numTel(client.getNumTel())
        .idEntreprise(client.getIdEntreprise())
        .build();
  }

  public static Client toEntity(ClientDto dto) {
    if (dto == null) {
      return null;
    }
    Client client = new Client();
    client.setId(dto.getId());
    client.setNom(dto.getNom());
    client.setPrenom(dto.getPrenom());
    client.setAdresse(AdresseDto.toEntity(dto.getAdresse()));
    client.setPhoto(dto.getPhoto());
    client.setMail(dto.getMail());
    client.setNumTel(dto.getNumTel());
    client.setIdEntreprise(dto.getIdEntreprise());
    return client;
  }

}
