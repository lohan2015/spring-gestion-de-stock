package com.cmbassi.gestiondestock.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChangerMotDePasseUtilisateurDto {

  private Integer id;

  private String motDePasse;

  private String confirmMotDePasse;

  public ChangerMotDePasseUtilisateurDto() {
  }

  public ChangerMotDePasseUtilisateurDto(Integer id, String motDePasse, String confirmMotDePasse) {
    this.id = id;
    this.motDePasse = motDePasse;
    this.confirmMotDePasse = confirmMotDePasse;
  }
}
