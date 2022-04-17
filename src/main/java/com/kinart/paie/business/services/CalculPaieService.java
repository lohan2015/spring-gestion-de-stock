package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.CalculPaieDto;

import java.util.List;

public interface CalculPaieService {

    CalculPaieDto save(CalculPaieDto dto);

    CalculPaieDto findById(Integer id);

    List<CalculPaieDto> findAll();

    List<CalculPaieDto> findByMatriculeAndPeriod(String nmat, String aamm, Integer nbul);

    void delete(Integer id);

    boolean calculPaieSalarie(String nmat, String aamm, Integer nbul);
}
