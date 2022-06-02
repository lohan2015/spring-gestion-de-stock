package com.kinart.paie.business.services;

import com.kinart.api.gestiondepaie.dto.ParamColumnDto;
import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.gestiondepaie.dto.ParamTableDto;

import java.util.List;

public interface ParamService {

    ParamColumnDto save(ParamColumnDto dto);

    ParamTableDto save(ParamTableDto dto);

    ParamDataDto save(ParamDataDto dto);

    ParamColumnDto findColumById(Integer id);

    ParamTableDto findTableById(Integer id);

    ParamDataDto findDataById(Integer id);

    List<ParamColumnDto> findColumnAll();

    List<ParamDataDto> findDataAll();

    List<ParamTableDto> findTableAll();

    List<ParamColumnDto> findColumnByCodeTable(Integer ctab);

    List<ParamDataDto> findDataByCodeTable(Integer ctab);

    List<ParamDataDto> findDataByCle(Integer ctab, String cacc);

    ParamDataDto findDataByNumeroLigne(Integer ctab, String cacc, Integer nume);

    void deleteColumn(Integer id);

    void deleteTable(Integer id);

    void deleteData(Integer id);

    List<ParamTableDto> findTableByKeyWord(String keyword);

    List<ParamDataDto> findDataByKeyWord(ParamDataDto search);
}
