package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.kinart.paie.business.model.ElementVariableDetailMois;
import com.kinart.paie.business.model.ElementVariableEnteteMois;
import com.kinart.paie.business.repository.ElementVariableDetailMoisRepository;
import com.kinart.paie.business.repository.ElementVariableEnteteMoisRepository;
import com.kinart.paie.business.services.ElementVariableService;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.validator.ElementVariableValidator;
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
public class ElementVariableServiceImpl implements ElementVariableService {

    private ElementVariableDetailMoisRepository elementVariableDetailMoisRepository;
    private ElementVariableEnteteMoisRepository elementVariableEnteteMoisRepository;
    private GeneriqueConnexionService service;

    @Autowired
    public ElementVariableServiceImpl(ElementVariableDetailMoisRepository elementVariableDetailMoisRepository, ElementVariableEnteteMoisRepository elementVariableEnteteMoisRepository, GeneriqueConnexionService service) {
        this.elementVariableDetailMoisRepository = elementVariableDetailMoisRepository;
        this.elementVariableEnteteMoisRepository = elementVariableEnteteMoisRepository;
        this.service = service;
    }

    @Override
    public ElementVariableDetailMoisDto save(ElementVariableDetailMoisDto dto) {
        List<String> errors = ElementVariableValidator.validateDetail(dto);
        if (!errors.isEmpty()) {
            log.error("EV non valide {}", dto);
            throw new InvalidEntityException("L'EV n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        ElementVariableEnteteMois elementVariableEnteteMois
                = elementVariableEnteteMoisRepository.findEntEVByMatricule(dto.getAamm(), dto.getNmat(), dto.getNbul());
        if(elementVariableEnteteMois == null) {
            elementVariableEnteteMois = new ElementVariableEnteteMois();
            elementVariableEnteteMois.setAamm(dto.getAamm());
            elementVariableEnteteMois.setIdEntreprise(dto.getIdEntreprise());
            elementVariableEnteteMois.setNbul(dto.getNbul());
            elementVariableEnteteMois.setNmat(dto.getNmat());
            elementVariableEnteteMois.setBcmo("N");
            elementVariableEnteteMois.setDdpa(new ClsDate(dto.getAamm(), "yyyyMM").getFirstDayOfMonth());
            elementVariableEnteteMois.setDfpa(new ClsDate(dto.getAamm(), "yyyyMM").getLastDayOfMonth());
            elementVariableEnteteMoisRepository.save(elementVariableEnteteMois);
        }

        return ElementVariableDetailMoisDto.fromEntity(
                elementVariableDetailMoisRepository.save(
                        ElementVariableDetailMoisDto.toEntity(dto)
                )
        );
    }

    @Override
    public ElementVariableDetailMoisDto findDetailById(Integer id) {
        if (id == null) {
            log.error("EV ID is null");
            return null;
        }

        return elementVariableDetailMoisRepository.findById(id).map(ElementVariableDetailMoisDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun EV avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<ElementVariableDetailMoisDto> findDetailAll() {
        return elementVariableDetailMoisRepository.findAll().stream()
                .map(ElementVariableDetailMoisDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("EV ID is null");
            return;
        }
        elementVariableDetailMoisRepository.deleteById(id);
    }

    @Override
    public List<ElementVariableDetailMoisDto> findEVByFilter(String matricule, String coderub) {
        List<ElementVariableDetailMoisDto> liste = new ArrayList<ElementVariableDetailMoisDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.lrub as librub " +
                        "FROM ElementVariableDetailMois e " +
                        "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                        "LEFT JOIN ElementSalaire t ON (t.identreprise=s.identreprise AND t.crub=e.rubq) "+
                        "WHERE 1=1";

        if(StringUtils.isNotEmpty(matricule)) sqlQuery += " AND upper(e.nmat) LIKE :matricule";
        if(StringUtils.isNotEmpty(coderub)) sqlQuery += " AND upper(e.rubq) LIKE :coderub";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", ElementVariableDetailMois.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("librub", StandardBasicTypes.STRING);

            //query.setParameter("identreprise", identreprise);
            if(StringUtils.isNotEmpty(matricule)) query.setParameter("matricule", "%"+matricule+"%");
            if(StringUtils.isNotEmpty(coderub)) query.setParameter("coderub", "%"+coderub+"%");
            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                ElementVariableDetailMois evDB = (ElementVariableDetailMois)o[0];
                ElementVariableDetailMoisDto evDto = ElementVariableDetailMoisDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomsalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomsalarie(evDto.getNomsalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setLibrubrique(o[3].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }
}
