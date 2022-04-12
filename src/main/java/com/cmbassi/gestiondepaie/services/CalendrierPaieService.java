package com.cmbassi.gestiondepaie.services;

import com.cmbassi.gestiondepaie.dto.CalendrierPaieDto;

import java.util.List;

public interface CalendrierPaieService {

    CalendrierPaieDto save(CalendrierPaieDto dto);

    CalendrierPaieDto findById(Integer id);

    List<CalendrierPaieDto> findAll();

    void delete(Integer id);
}
