package com.cmbassi.gestiondepaie.services;

import com.cmbassi.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.cmbassi.gestiondepaie.dto.ElementVariableEnteteMoisDto;

import java.util.List;
import java.util.Optional;

public interface ElementVariableService {

    ElementVariableDetailMoisDto save(ElementVariableDetailMoisDto dto);

    ElementVariableDetailMoisDto findDetailById(Integer id);

    List<ElementVariableDetailMoisDto> findDetailAll();

    void delete(Integer id);

    List<ElementVariableDetailMoisDto> findEVByFilter(Optional<String> matricule, Optional<String> coderub);
}
