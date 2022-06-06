package com.kinart.api.gestiondepaie.report.service;

import com.kinart.api.gestiondepaie.dto.ParamEFCMRDto;
import com.kinart.api.gestiondepaie.report.LigneDeclarationVersement;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;

public interface EFCMRService {

    /**
     *
     * @param service
     * @param dto
     * @return
     */
    LigneDeclarationVersement loadDataDeclarationVersement(GeneriqueConnexionService service, ParamEFCMRDto dto);

}
