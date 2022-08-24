package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.ElementSalaireDto;
import com.kinart.api.gestiondepaie.dto.PretInterneDto;

import java.util.List;

public interface PretInterneService {

    PretInterneDto save(PretInterneDto dto);

    PretInterneDto findById(Integer id);

    void delete(Integer id);

    List<PretInterneDto> findByMatricule(String matricule);

    List<ElementSalaireDto> findRubriqueForPretInterne();
}
