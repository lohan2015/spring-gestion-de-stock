package com.kinart.api.gestiondestock.dto;

import com.kinart.stock.business.model.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des rôles")
public class RolesDto {

  private Integer id;

  private String roleName;

  private String module;

  private String droit;

  private String actif;

  private Date dateDeb;

  private Date dateFin;

  private String mode;

  @JsonIgnore
  private UtilisateurDto utilisateur;

  public static RolesDto fromEntity(Roles roles) {
    if (roles == null) {
      return null;
    }
    return RolesDto.builder()
        .id(roles.getId())
        .roleName(roles.getRoleName())
        .build();
  }

  public static Roles toEntity(RolesDto dto) {
    if (dto == null) {
      return null;
    }
    Roles roles = new Roles();
    roles.setId(dto.getId());
    roles.setRoleName(dto.getRoleName());
    roles.setUtilisateur(UtilisateurDto.toEntity(dto.getUtilisateur()));
    return roles;
  }

}
