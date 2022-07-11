package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.ElementSalaireBaseDto;
import com.kinart.api.gestiondepaie.dto.PretInterneDto;

import java.util.List;

public interface ElementBaseCalculService {

    ElementSalaireBaseDto save(ElementSalaireBaseDto dto);

    ElementSalaireBaseDto findById(Integer id);

    void delete(Integer id);

    List<ElementSalaireBaseDto> findByCodeRub(String crub);
}
