package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.ElementFixeSalaireDto;
import com.kinart.api.gestiondepaie.dto.ElementSalaireDto;

import java.util.List;

public interface ElementFixeSalaireService {

    ElementFixeSalaireDto save(ElementFixeSalaireDto dto);

    ElementFixeSalaireDto findById(Integer id);

    List<ElementFixeSalaireDto> findAll();

    List<ElementFixeSalaireDto> findByMatricule(String matricule);

    void delete(Integer id);
}
