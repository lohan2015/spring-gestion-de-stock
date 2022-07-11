package com.kinart.api.gestiondepaie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RechercheDto {
    public String periodeDePaie = StringUtils.EMPTY;
    public String numeroBulletin = StringUtils.EMPTY;
    public String nmatMin = StringUtils.EMPTY;
    public String libNmatMin = StringUtils.EMPTY;
    public String nomNmatMin = StringUtils.EMPTY;
    public String nmatMax = StringUtils.EMPTY;
    public String libNmatMax = StringUtils.EMPTY;
    public String nomNmatMax = StringUtils.EMPTY;
    public String niv1Min = StringUtils.EMPTY;
    public String libNiv1Min = StringUtils.EMPTY;
    public String niv1Max = StringUtils.EMPTY;
    public String niv2Max = StringUtils.EMPTY;
    public String niv3Max = StringUtils.EMPTY;
    public String niv2Min = StringUtils.EMPTY;
    public String libNiv2Min = StringUtils.EMPTY;
    public String niv3Min = StringUtils.EMPTY;
    public String libNiv3Min = StringUtils.EMPTY;
    public String agceMin = StringUtils.EMPTY;
    public String libAgceMin = StringUtils.EMPTY;
    public String agceMax = StringUtils.EMPTY;
    public String libAgceMax = StringUtils.EMPTY;
    public String rubMin = StringUtils.EMPTY;
    public String libRubMin = StringUtils.EMPTY;
    public String rubMax = StringUtils.EMPTY;
    public String libRubMax = StringUtils.EMPTY;
    public String codeMotifCg = StringUtils.EMPTY;
    public String libMotifCg = StringUtils.EMPTY;
    public boolean allRub = false;
    public String user = StringUtils.EMPTY;
    public String identreprise = StringUtils.EMPTY;
    public String dateformat = "dd/MM/yyyy";
    public String typetrt = "CENTRA";
    public String libNiv1Max = StringUtils.EMPTY;
    public String libNiv2Max = StringUtils.EMPTY;
    public String libNiv3Max = StringUtils.EMPTY;
}
