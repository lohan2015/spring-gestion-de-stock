package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.ElementSalaireDto;

import java.util.List;

public interface ElementSalaireService {

    ElementSalaireDto save(ElementSalaireDto dto);

    ElementSalaireDto findById(Integer id);

    List<ElementSalaireDto> findAll();

    List<ElementSalaireDto> findByCode(String code);

    List<ElementSalaireDto> findByLibelle(String libelle);

    void delete(Integer id);

    List<ElementSalaireDto> findDataByKeyWord(String keyword);
}
