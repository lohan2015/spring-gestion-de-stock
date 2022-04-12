package com.cmbassi.gestiondepaie.services;

import com.cmbassi.gestiondepaie.dto.ElementSalaireDto;

import java.util.List;

public interface ElementSalaireService {

    ElementSalaireDto save(ElementSalaireDto dto);

    ElementSalaireDto findById(Integer id);

    List<ElementSalaireDto> findAll();

    List<ElementSalaireDto> findByCode(String code);

    List<ElementSalaireDto> findByLibelle(String libelle);

    void delete(Integer id);
}
