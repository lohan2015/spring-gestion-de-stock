package com.kinart.api.gestiondepaie.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des résultats de la précentralisation")
public class ResultatPrecentralisationDto {
    public String message = "CENTRA";
    public BigDecimal totalDebit = BigDecimal.ZERO;
    public BigDecimal totalCredit = BigDecimal.ZERO;
}
