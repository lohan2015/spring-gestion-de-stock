package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.DossierPaieDto;

import java.util.Date;
import java.util.List;

public interface DossierPaieService {

    DossierPaieDto save(DossierPaieDto dto);

    DossierPaieDto findById(Integer id);

    List<DossierPaieDto> findAll();

    void delete(Integer id);

    Date getMoisDePaieCourant(Integer idEntreprise);

    Integer getNumeroBulletinPaie(Integer idEntreprise);
}
