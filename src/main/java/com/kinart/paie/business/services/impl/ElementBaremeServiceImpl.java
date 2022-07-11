package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.ElementSalaireBaremeDto;
import com.kinart.api.gestiondepaie.dto.ElementSalaireBaseDto;
import com.kinart.paie.business.model.ElementSalaireBareme;
import com.kinart.paie.business.model.ElementSalaireBase;
import com.kinart.paie.business.repository.ElementSalaireBaremeRepository;
import com.kinart.paie.business.repository.ElementSalaireBaseRepository;
import com.kinart.paie.business.services.ElementBaremeService;
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
public class ElementBaremeServiceImpl implements ElementBaremeService {

    private ElementSalaireBaremeRepository elementSalaireBaremeRepository;
    private GeneriqueConnexionService service;

    @Autowired
    public ElementBaremeServiceImpl(ElementSalaireBaremeRepository elementSalaireBaremeRepository, GeneriqueConnexionService service) {
        this.elementSalaireBaremeRepository = elementSalaireBaremeRepository;
        this.service = service;
    }

    @Override
    public ElementSalaireBaremeDto save(ElementSalaireBaremeDto dto) {
        return ElementSalaireBaremeDto.fromEntity(
                elementSalaireBaremeRepository.save(
                        ElementSalaireBaremeDto.toEntity(dto)
                )
        );
    }

    @Override
    public ElementSalaireBaremeDto findById(Integer id) {
        if (id == null) {
            log.error("Elément de barème ID is null");
            return null;
        }

        return elementSalaireBaremeRepository.findById(id).map(ElementSalaireBaremeDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun élément de barème avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Elément de barème ID is null");
            return;
        }
        elementSalaireBaremeRepository.deleteById(id);
    }

    @Override
    public List<ElementSalaireBaremeDto> findByCodeRub(String crub) {
        List<ElementSalaireBaremeDto> liste = new ArrayList<ElementSalaireBaremeDto>();
        String sqlQuery = "SELECT e.* " +
                "FROM ElementSalaireBareme e " +
                "WHERE e.crub='"+crub+"'";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", ElementSalaireBareme.class);

            List<ElementSalaireBareme> lst = query.getResultList();
            service.closeSession(session);

            for (ElementSalaireBareme o : lst)
            {
                ElementSalaireBareme evDB = (ElementSalaireBareme)o;
                ElementSalaireBaremeDto evDto = ElementSalaireBaremeDto.fromEntity(evDB);

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }
}
