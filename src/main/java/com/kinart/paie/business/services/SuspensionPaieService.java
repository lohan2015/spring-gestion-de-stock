package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.SuspensionPaieDto;

import java.util.List;

public interface SuspensionPaieService {

    SuspensionPaieDto save(SuspensionPaieDto dto);

    SuspensionPaieDto findById(Integer id);

    List<SuspensionPaieDto> findAll();

    List<SuspensionPaieDto> findByMatricule(String matricule);

    void delete(Integer id);
}
