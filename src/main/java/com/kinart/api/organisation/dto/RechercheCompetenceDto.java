package com.kinart.api.organisation.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de recherche des compétences")
public class RechercheCompetenceDto {
    private String codeposte;
    private String typeinfo;
    private String codedossier;
}
