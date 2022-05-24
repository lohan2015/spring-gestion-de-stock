package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.HistoConnexionDto;
import com.kinart.api.gestiondepaie.dto.LogMessageDto;
import com.kinart.paie.business.model.HistoConnexion;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoConnexionService {

    HistoConnexionDto save(HistoConnexionDto dto);

    HistoConnexionDto findById(Integer id);

    List<HistoConnexionDto> findAll();

    void delete(Integer id);

    List<HistoConnexionDto>  findByUser(String user);

    List<HistoConnexionDto> findByUserAndOperation(String user, String optype);

    List<HistoConnexionDto> findByUserAndNoOperation(String user, String optype);

    List<HistoConnexionDto> findByOperation(String optype);

    List<HistoConnexionDto> findByNoOperation(String optype);
}
