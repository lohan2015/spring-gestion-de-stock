package com.cmbassi.gestiondepaie.services;

import com.cmbassi.gestiondepaie.dto.SalarieDto;

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
