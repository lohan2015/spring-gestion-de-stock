package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.ElementVariableCongeDto;

import java.util.List;

public interface ElementVariableCongeService {

    ElementVariableCongeDto save(ElementVariableCongeDto dto, String dateFormat, String typeBD);

    ElementVariableCongeDto findById(Integer id);

    List<ElementVariableCongeDto> findAll();

    void delete(Integer id);

    List<ElementVariableCongeDto> findEVCongeByFilter(String matricule, String codemotif);
}
