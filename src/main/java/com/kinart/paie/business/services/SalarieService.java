package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.SalarieDto;

import java.util.List;

public interface SalarieService {

    SalarieDto save(SalarieDto dto);

    SalarieDto findById(Integer id);

    List<SalarieDto> findAll();

    List<SalarieDto> findByMatricule(String nmat);

    List<SalarieDto> findByName(String nom);

    List<SalarieDto> findByMatriculeInactif(String nmat);

    void delete(Integer id);
}
