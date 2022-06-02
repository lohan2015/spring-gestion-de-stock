package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.*;
import com.kinart.paie.business.model.CumulPaie;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.ParamTable;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.repository.ParamColumnRepository;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.repository.ParamTableRepository;
import com.kinart.paie.business.services.ParamService;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.validator.ParamColumnValidator;
import com.kinart.paie.business.validator.ParamDataValidator;
import com.kinart.paie.business.validator.ParamTableValidator;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ParamServiceImpl implements ParamService {

    private ParamDataRepository paramDataRepository;
    private ParamColumnRepository paramColumnRepository;
    private ParamTableRepository paramTableRepository;
    private GeneriqueConnexionService generiqueConnexionService;

    @Autowired
    public ParamServiceImpl(GeneriqueConnexionService generiqueConnexionService, ParamDataRepository paramDataRepository, ParamColumnRepository paramColumnRepository, ParamTableRepository paramTableRepository) {
        this.paramDataRepository = paramDataRepository;
        this.paramColumnRepository = paramColumnRepository;
        this.paramTableRepository = paramTableRepository;
        this.generiqueConnexionService = generiqueConnexionService;
    }

    @Override
    public ParamColumnDto save(ParamColumnDto dto) {
        List<String> errors = ParamColumnValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Colonne de paramétrage non valide {}", dto);
            throw new InvalidEntityException("La colonne n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        return ParamColumnDto.fromEntity(
                paramColumnRepository.save(
                        ParamColumnDto.toEntity(dto)
                )
        );
    }

    @Override
    public ParamTableDto save(ParamTableDto dto) {
        List<String> errors = ParamTableValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Table de paramétrage non valide {}", dto);
            throw new InvalidEntityException("Le table n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        return ParamTableDto.fromEntity(
                paramTableRepository.save(
                        ParamTableDto.toEntity(dto)
                )
        );
    }

    @Override
    public ParamDataDto save(ParamDataDto dto) {
        List<String> errors = ParamDataValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Donnée de paramétrage non valide {}", dto);
            throw new InvalidEntityException("La donnée n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        return ParamDataDto.fromEntity(
                paramDataRepository.save(
                        ParamDataDto.toEntity(dto)
                )
        );
    }

    @Override
    public ParamColumnDto findColumById(Integer id) {
        return paramColumnRepository.findById(id).map(ParamColumnDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune colonne avec l'ID = " + id + " n' ete trouve dans la BDD",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );
    }

    @Override
    public ParamTableDto findTableById(Integer id) {
        return paramTableRepository.findById(id).map(ParamTableDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune table avec l'ID = " + id + " n' ete trouve dans la BDD",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );
    }

    @Override
    public ParamDataDto findDataById(Integer id) {
        return paramDataRepository.findById(id).map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = " + id + " n' ete trouve dans la BDD",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );
    }

    @Override
    public List<ParamColumnDto> findColumnAll() {
        return paramColumnRepository.findAll().stream()
                .map(ParamColumnDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParamDataDto> findDataAll() {
        return paramDataRepository.findAll().stream()
                .map(ParamDataDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParamTableDto> findTableAll() {
        return paramTableRepository.findAll().stream()
                .map(ParamTableDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParamColumnDto> findColumnByCodeTable(Integer ctab) {
        return paramColumnRepository.findByCodeTable(ctab).stream()
                .map(ParamColumnDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParamDataDto> findDataByCodeTable(Integer ctab) {
        return paramDataRepository.findByCodeTable(ctab).stream()
                .map(ParamDataDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParamDataDto> findDataByCle(Integer ctab, String cacc) {
        return paramDataRepository.findByCle(ctab, cacc).stream()
                .map(ParamDataDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ParamDataDto findDataByNumeroLigne(Integer ctab, String cacc, Integer nume) {
        return paramDataRepository.findByNumeroLigne(ctab, cacc, nume).map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = " + cacc + " n' ete trouve dans la BDD",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );
    }

    @Override
    public void deleteColumn(Integer id) {
        paramColumnRepository.deleteById(id);
    }

    @Override
    public void deleteTable(Integer id) {
        paramTableRepository.deleteById(id);
    }

    @Override
    public void deleteData(Integer id) {
        paramDataRepository.deleteById(id);
    }

    @Override
    public List<ParamTableDto> findTableByKeyWord(String keyword) {
        List<ParamTable> result = generiqueConnexionService.find("FROM ParamTable WHERE (ctab like '%"+keyword+"%' OR upper(libe) like UPPER('%"+keyword+"%') )");
        return result.stream().map(ParamTableDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<ParamDataDto> findDataByKeyWord(ParamDataDto search) {
        String queryString = "select d.*, c.libe as libcolonne  from ParamData d, ParamColumn c "+
                       "WHERE d.idEntreprise=c.idEntreprise and d.ctab=c.ctab and d.nume=c.nume and d.ctab = "+search.getCtab().intValue();
        if(StringUtils.isNotEmpty(search.getCacc()) || StringUtils.isNotEmpty(search.getVall())) queryString += " AND (upper(d.cacc) like UPPER('%"+search.getCacc()+"%') OR upper(d.vall) like UPPER('%\"+search.getVall()+\"%'))";
        //if(StringUtils.isNotEmpty(search.getVall())) queryString += " ";
        queryString += " order by d.ctab, d.cacc, d.nume";
        Session session = generiqueConnexionService.getSession();
        Query query  = session.createSQLQuery(queryString)
                .addEntity("e", ParamData.class)
                .addScalar("libcolonne", StandardBasicTypes.STRING);

        List<Object[]> lst = query.getResultList();
        generiqueConnexionService.closeSession(session);
        List<ParamDataDto> liste = new ArrayList<ParamDataDto>();
        for (Object[] o : lst)
        {
            ParamData evDB = (ParamData)o[0];
            ParamDataDto evDto = ParamDataDto.fromEntity(evDB);
            if(o[1]!=null) evDto.setLibcolonne(o[1].toString());

            liste.add(evDto);
        }

        return liste;
    }
}
