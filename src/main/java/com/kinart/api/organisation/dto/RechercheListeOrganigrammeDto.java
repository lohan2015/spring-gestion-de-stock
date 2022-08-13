package com.kinart.api.organisation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechercheListeOrganigrammeDto
{
	public String libelle;
	public String code;
	public String cdos;
	public String clang;
	public String cuti;
	public String niveau;
	public String site;
	public String fictive;
	public String prestataire;
}
