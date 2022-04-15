package com.cmbassi.gestiondepaie.services;

import com.cmbassi.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.cmbassi.gestiondepaie.dto.ElementVariableEnteteMoisDto;

import java.util.List;

public interface ElementVariableService {

    ElementVariableDetailMoisDto save(ElementVariableDetailMoisDto dto);

    ElementVariableDetailMoisDto findDetailById(Integer id);

    List<ElementVariableDetailMoisDto> findDetailAll();

    void delete(Integer id);
}
