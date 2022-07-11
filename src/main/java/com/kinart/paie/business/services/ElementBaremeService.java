package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.ElementSalaireBaremeDto;
import com.kinart.api.gestiondepaie.dto.ElementSalaireBaseDto;

import java.util.List;

public interface ElementBaremeService {

    ElementSalaireBaremeDto save(ElementSalaireBaremeDto dto);

    ElementSalaireBaremeDto findById(Integer id);

    void delete(Integer id);

    List<ElementSalaireBaremeDto> findByCodeRub(String crub);
}
