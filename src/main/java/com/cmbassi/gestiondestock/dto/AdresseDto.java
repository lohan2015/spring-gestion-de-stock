package com.cmbassi.gestiondestock.dto;

import com.cmbassi.gestiondestock.model.Adresse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdresseDto {

  private String adresse1;

  private String adresse2;

  private String ville;

  private String codePostale;

  private String pays;

  public AdresseDto() {
  }

  public AdresseDto(String adresse1, String adresse2, String ville, String codePostale, String pays) {
    this.adresse1 = adresse1;
    this.adresse2 = adresse2;
    this.ville = ville;
    this.codePostale = codePostale;
    this.pays = pays;
  }

  public static AdresseDto fromEntity(Adresse adresse) {
    if (adresse == null) {
      return null;
    }

    return AdresseDto.builder()
        .adresse1(adresse.getAdresse1())
        .adresse2(adresse.getAdresse2())
        .codePostale(adresse.getCodePostale())
        .ville(adresse.getVille())
        .pays(adresse.getPays())
        .build();
  }

  public static Adresse toEntity(AdresseDto adresseDto) {
    if (adresseDto == null) {
      return null;
    }
    Adresse adresse = new Adresse();
    adresse.setAdresse1(adresseDto.getAdresse1());
    adresse.setAdresse2(adresseDto.getAdresse2());
    adresse.setCodePostale(adresseDto.getCodePostale());
    adresse.setVille(adresseDto.getVille());
    adresse.setPays(adresseDto.getPays());
    return adresse;
  }

}
