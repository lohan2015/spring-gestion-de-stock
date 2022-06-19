package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.VirementSalarieDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

public interface VirementSalaireService {

    VirementSalarieDto save(VirementSalarieDto dto);

    VirementSalarieDto findById(Integer id);

    VirementSalarieDto findByMatricule(String matricule);

    List<VirementSalarieDto> findAll();

    void delete(Integer id);

    List<VirementSalarieDto> findDetailByMatricule(String matricule);
}
