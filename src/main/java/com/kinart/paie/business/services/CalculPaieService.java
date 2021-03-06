package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.CalculPaieDto;
import com.kinart.api.gestiondepaie.dto.CumulPaieDto;
import com.kinart.api.gestiondepaie.dto.RechercheDto;
import com.kinart.api.gestiondepaie.dto.SalarieDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CalculPaieService {

    CalculPaieDto save(CalculPaieDto dto);

    CalculPaieDto findById(Integer id);

    List<CalculPaieDto> findAll();

    List<CalculPaieDto> findByMatriculeAndPeriod(String nmat, String aamm, Integer nbul);

    void delete(Integer id);

    boolean calculPaieSalarie(RechercheDto dto);

    List<CalculPaieDto> findResultCalculByFilter(RechercheDto dto);

    List<SalarieDto> findListeSalarieByFilter(RechercheDto dto);

    boolean cloturePaie(RechercheDto dto, HttpServletRequest request);

    List<CumulPaieDto> findCumulByFilter(RechercheDto dto);

}
