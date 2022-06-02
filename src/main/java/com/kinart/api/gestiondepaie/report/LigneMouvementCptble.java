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
public class LigneMouvementCptble {
    public String numcpte;
    public String sens;
    public String libecriture;
    //public BigDecimal mntpce;
    public String mntpce;
}
