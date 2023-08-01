package com.kinart.api.gestiondestock.dto;

import com.kinart.portail.business.utils.EnumOuiNon;
import com.kinart.stock.business.model.Utilisateur;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDto implements Serializable {

  private Integer id;

  private String nom;

  private String prenom;

  private String email;

  private Date dateDeNaissance;

  private String moteDePasse;

  private AdresseDto adresse;

  private String photo;

  private String valid1;

  private String valid2;

  private String valid3;

  private String valid4;

  private EnumOuiNon scepersonnel = EnumOuiNon.NON;
  private EnumOuiNon drhl = EnumOuiNon.NON;
  private EnumOuiNon dga = EnumOuiNon.NON;
  private EnumOuiNon dg = EnumOuiNon.NON;

  private EntrepriseDto entreprise;

  private List<RolesDto> roles;

  /*public UtilisateurDto() {
  }

  public UtilisateurDto(Integer id, String nom, String prenom, String email, Instant dateDeNaissance, String moteDePasse, AdresseDto adresse, String photo, EntrepriseDto entreprise, List<RolesDto> roles) {
    this.id = id;
    this.nom = nom;
    this.prenom = prenom;
    this.email = email;
    this.dateDeNaissance = dateDeNaissance;
    this.moteDePasse = moteDePasse;
    this.adresse = adresse;
    this.photo = photo;
    this.entreprise = entreprise;
    this.roles = roles;
  }*/

  public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
    System.out.println("USER..................."+utilisateur);
    if (utilisateur == null) {
      return null;
    }

    //System.out.println("ROLES..................."+utilisateur.getRoles().size());
    return UtilisateurDto.builder()
        .id(utilisateur.getId())
        .nom(utilisateur.getNom())
        .prenom(utilisateur.getPrenom())
        .email(utilisateur.getEmail())
        .moteDePasse(utilisateur.getMoteDePasse())
        .dateDeNaissance(utilisateur.getDateDeNaissance())
        .adresse(AdresseDto.fromEntity(utilisateur.getAdresse()))
        .photo(utilisateur.getPhoto())
            .dg(utilisateur.getDg())
            .dga(utilisateur.getDga())
            .drhl(utilisateur.getDrhl())
            .scepersonnel(utilisateur.getScepersonnel())
            .valid1(utilisateur.getValid1())
            .valid2(utilisateur.getValid2())
            .valid3(utilisateur.getValid3())
            .valid4(utilisateur.getValid4())
         .entreprise(EntrepriseDto.fromEntity(utilisateur.getEntreprise()))
        .roles(
                    utilisateur.getRoles() != null ?
                            utilisateur.getRoles().stream()
                                    .map(RolesDto::fromEntity)
                                    .collect(Collectors.toList()) : null
            )
        .build();
  }

  public static Utilisateur toEntity(UtilisateurDto dto) {
    if (dto == null) {
      return null;
    }

    Utilisateur utilisateur = new Utilisateur();
    utilisateur.setId(dto.getId());
    utilisateur.setNom(dto.getNom());
    utilisateur.setPrenom(dto.getPrenom());
    utilisateur.setEmail(dto.getEmail());
    utilisateur.setMoteDePasse(dto.getMoteDePasse());
    utilisateur.setDateDeNaissance(dto.getDateDeNaissance());
    utilisateur.setAdresse(AdresseDto.toEntity(dto.getAdresse()));
    utilisateur.setPhoto(dto.getPhoto());
    utilisateur.setEntreprise(EntrepriseDto.toEntity(dto.getEntreprise()));
    utilisateur.setDg(dto.getDg());
    utilisateur.setDga(dto.getDga());
    utilisateur.setScepersonnel(dto.getScepersonnel());
    utilisateur.setDrhl(dto.getDrhl());
    utilisateur.setValid1(dto.getValid1());
    utilisateur.setValid2(dto.getValid2());
    utilisateur.setValid3(dto.getValid3());
    utilisateur.setValid4(dto.getValid4());

    return utilisateur;
  }
}
