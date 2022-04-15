package com.cmbassi.gestiondepaie.services.impl;

import com.cmbassi.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.cmbassi.gestiondepaie.model.ElementVariableDetailMois;
import com.cmbassi.gestiondepaie.model.ElementVariableEnteteMois;
import com.cmbassi.gestiondepaie.repository.ElementVariableDetailMoisRepository;
import com.cmbassi.gestiondepaie.repository.ElementVariableEnteteMoisRepository;
import com.cmbassi.gestiondepaie.services.ElementVariableService;
import com.cmbassi.gestiondepaie.services.utils.ClsDate;
import com.cmbassi.gestiondepaie.services.utils.GeneriqueConnexionService;
import com.cmbassi.gestiondepaie.validator.ElementVariableValidator;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.ErrorCodes;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.postgresql.util.GT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public List<ElementVariableDetailMoisDto> findEVByFilter(Optional<String> matricule, Optional<String> coderub) {
        List<ElementVariableDetailMoisDto> liste = new ArrayList<ElementVariableDetailMoisDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.lrub as librub " +
                        "FROM ElementVariableDetailMois e " +
                        "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                        "LEFT JOIN ElementSalaire t ON (t.identreprise=s.identreprise AND t.crub=e.rubq) "+
                        "WHERE 1=1";

        if(matricule.isPresent()) sqlQuery += " AND upper(e.nmat) LIKE :matricule";
        if(coderub.isPresent()) sqlQuery += " AND upper(e.rubq) LIKE :coderub";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", ElementVariableDetailMois.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("librub", StandardBasicTypes.STRING);

            //query.setParameter("identreprise", identreprise);
            if(matricule.isPresent()) query.setParameter("matricule", "%"+matricule+"%");
            if(coderub.isPresent()) query.setParameter("coderub", "%"+coderub+"%");
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
