package com.kinart.api.gestiondestock.dto;

import com.kinart.stock.business.model.Utilisateur;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtilisateurDto {

  private Integer id;

  private String nom;

  private String prenom;

  private String email;

  private Instant dateDeNaissance;

  private String moteDePasse;

  private AdresseDto adresse;

  private String photo;

  private EntrepriseDto entreprise;

  private List<RolesDto> roles;

  public UtilisateurDto() {
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
  }

  public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
    if (utilisateur == null) {
      return null;
    }

    return UtilisateurDto.builder()
        .id(utilisateur.getId())
        .nom(utilisateur.getNom())
        .prenom(utilisateur.getPrenom())
        .email(utilisateur.getEmail())
        .moteDePasse(utilisateur.getMoteDePasse())
        .dateDeNaissance(utilisateur.getDateDeNaissance())
        .adresse(AdresseDto.fromEntity(utilisateur.getAdresse()))
        .photo(utilisateur.getPhoto())
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

    return utilisateur;
  }
}