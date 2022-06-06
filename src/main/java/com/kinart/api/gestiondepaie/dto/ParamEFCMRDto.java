package com.kinart.api.gestiondepaie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamEFCMRDto {
    Integer idEntreprise;
    String periodePaie;
    String vildMin;
    String vildMax;
    String libVildMin;
    String libVildMax;
}
