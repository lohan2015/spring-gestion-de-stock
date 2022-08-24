package com.kinart.api.organisation.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Contrôle, Affectation, permutation et rattachement sur les organigramme")
public class OperationOrganigrammeDto {
    private Integer identreprise;
    private String typeoperation; //Affectation poste=AFECPOSTE; Affectation salarié=AFECSAL;
                                  //Permutation cellule=PERMU; Rattachelent=RATTACH
                                  //Contrôle affectation poste=CTRLPOSTE; Contrôle affectation salarié=CTRLSAL
    private Integer idcellule;
    private String codeorganigramme;
    private String libelleorganigramme;
    private String codeposte;
    private String libelleposte;
    private String matricule;
    private String nomsalarie;
    private String codeorganigramme2;
    private String libelleorganigramme2;
}
