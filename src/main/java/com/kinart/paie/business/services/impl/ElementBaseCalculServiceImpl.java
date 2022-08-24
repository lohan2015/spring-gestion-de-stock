package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.ElementSalaireBaseDto;
import com.kinart.paie.business.model.ElementSalaireBase;
import com.kinart.paie.business.repository.ElementSalaireBaseRepository;
import com.kinart.paie.business.services.ElementBaseCalculService;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ElementBaseCalculServiceImpl implements ElementBaseCalculService {

    private ElementSalaireBaseRepository elementSalaireBaseRepository;
    private GeneriqueConnexionService service;

    @Autowired
    public ElementBaseCalculServiceImpl(ElementSalaireBaseRepository elementSalaireBaseRepository, GeneriqueConnexionService service) {
        this.elementSalaireBaseRepository = elementSalaireBaseRepository;
        this.service = service;
    }

    @Override
    public ElementSalaireBaseDto save(ElementSalaireBaseDto dto) {
        return ElementSalaireBaseDto.fromEntity(
                elementSalaireBaseRepository.save(
                        ElementSalaireBaseDto.toEntity(dto)
                )
        );
    }

    @Override
    public ElementSalaireBaseDto findById(Integer id) {
        if (id == null) {
            log.error("Elément de base ID is null");
            return null;
        }

        return elementSalaireBaseRepository.findById(id).map(ElementSalaireBaseDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun élément de base avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Elément de base ID is null");
            return;
        }
        elementSalaireBaseRepository.deleteById(id);
    }

    @Override
    public List<ElementSalaireBaseDto> findByCodeRub(String crub) {
        List<ElementSalaireBaseDto> liste = new ArrayList<ElementSalaireBaseDto>();
        String sqlQuery = "SELECT e.*, t.lrub as librubrique " +
                "FROM ElementSalaireBase e " +
                "LEFT JOIN ElementSalaire t ON (t.identreprise=e.identreprise AND t.crub=e.rubk) "+
                "WHERE e.crub='"+crub+"'";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", ElementSalaireBase.class)
                    .addScalar("librubrique", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                ElementSalaireBase evDB = (ElementSalaireBase)o[0];
                ElementSalaireBaseDto evDto = ElementSalaireBaseDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setLibRubrique(o[1].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }
}
