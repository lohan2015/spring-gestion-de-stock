package com.cmbassi.gestiondepaie.services;

import com.cmbassi.gestiondepaie.dto.ParamColumnDto;
import com.cmbassi.gestiondepaie.dto.ParamDataDto;
import com.cmbassi.gestiondepaie.dto.ParamTableDto;
import com.cmbassi.gestiondepaie.dto.SalarieDto;
import com.cmbassi.gestiondepaie.model.ParamColumn;
import com.cmbassi.gestiondepaie.model.ParamData;
import org.springframework.data.repository.query.Param;

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
}
