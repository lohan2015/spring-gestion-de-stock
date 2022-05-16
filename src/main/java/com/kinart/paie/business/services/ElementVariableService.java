package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.ElementVariableDetailMoisDto;

import java.util.List;
import java.util.Optional;

public interface ElementVariableService {

    ElementVariableDetailMoisDto save(ElementVariableDetailMoisDto dto);

    ElementVariableDetailMoisDto findDetailById(Integer id);

    List<ElementVariableDetailMoisDto> findDetailAll();

    void delete(Integer id);

    List<ElementVariableDetailMoisDto> findEVByFilter(String matricule, String coderub);
}
