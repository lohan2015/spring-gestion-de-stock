package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.VirementSalarieDto;

import java.util.List;

public interface VirementSalaireService {

    VirementSalarieDto save(VirementSalarieDto dto);

    VirementSalarieDto findById(Integer id);

    VirementSalarieDto findByMatricule(String matricule);

    List<VirementSalarieDto> findAll();

    void delete(Integer id);
}
