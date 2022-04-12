package com.cmbassi.gestiondepaie.services;

import com.cmbassi.gestiondepaie.dto.CalculPaieDto;

import java.util.List;

public interface CalculPaieService {

    CalculPaieDto save(CalculPaieDto dto);

    CalculPaieDto findById(Integer id);

    List<CalculPaieDto> findAll();

    List<CalculPaieDto> findByMatriculeAndPeriod(String nmat, String aamm, Integer nbul);

    void delete(Integer id);

    boolean calculPaieSalarie(String nmat, String aamm, Integer nbul);
}
