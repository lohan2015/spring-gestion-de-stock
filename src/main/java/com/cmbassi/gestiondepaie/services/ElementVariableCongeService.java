package com.cmbassi.gestiondepaie.services;

import com.cmbassi.gestiondepaie.dto.ElementVariableCongeDto;
import com.cmbassi.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.cmbassi.gestiondepaie.model.ElementVariableConge;

import java.util.List;
import java.util.Optional;

public interface ElementVariableCongeService {

    ElementVariableCongeDto save(ElementVariableCongeDto dto, String dateFormat, String typeBD);

    ElementVariableCongeDto findById(Integer id);

    List<ElementVariableCongeDto> findAll();

    void delete(Integer id);

    List<ElementVariableCongeDto> findEVCongeByFilter(Optional<String> matricule, Optional<String> codemotif);
}
