package com.cmbassi.gestiondestock.dto;

import com.cmbassi.gestiondestock.model.Ventes;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VentesDto {

  private Integer id;

  private String code;

  private Instant dateVente;

  private String commentaire;

  private List<LigneVenteDto> ligneVentes;

  private Integer idEntreprise;

  public VentesDto() {
  }

  public VentesDto(Integer id, String code, Instant dateVente, String commentaire, List<LigneVenteDto> ligneVentes, Integer idEntreprise) {
    this.id = id;
    this.code = code;
    this.dateVente = dateVente;
    this.commentaire = commentaire;
    this.ligneVentes = ligneVentes;
    this.idEntreprise = idEntreprise;
  }

  public static VentesDto fromEntity(Ventes vente) {
    if (vente == null) {
      return null;
    }
    return VentesDto.builder()
        .id(vente.getId())
        .code(vente.getCode())
        .commentaire(vente.getCommentaire())
        .idEntreprise(vente.getIdEntreprise())
        .build();
  }

  public static Ventes toEntity(VentesDto dto) {
    if (dto == null) {
      return null;
    }
    Ventes ventes = new Ventes();
    ventes.setId(dto.getId());
    ventes.setCode(ventes.getCode());
    ventes.setCommentaire(dto.getCommentaire());
    ventes.setIdEntreprise(dto.getIdEntreprise());
    return ventes;
  }
}
