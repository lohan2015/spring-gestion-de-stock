package com.kinart.api.gestiondepaie.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LigneDeclarationVersement {
    BigDecimal cnps = BigDecimal.ZERO;
    BigDecimal irpp = BigDecimal.ZERO;
    BigDecimal cacirpp = BigDecimal.ZERO;
    BigDecimal cfcsal = BigDecimal.ZERO;
    BigDecimal cfcpat = BigDecimal.ZERO;
    BigDecimal td = BigDecimal.ZERO;
    BigDecimal rav = BigDecimal.ZERO;
    BigDecimal fne = BigDecimal.ZERO;
}
