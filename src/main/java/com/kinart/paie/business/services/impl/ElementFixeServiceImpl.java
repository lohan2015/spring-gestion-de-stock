package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.ElementFixeSalaireDto;
import com.kinart.paie.business.model.ElementFixeSalaire;
import com.kinart.paie.business.repository.ElementFixeSalaireRepository;
import com.kinart.paie.business.services.ElementFixeSalaireService;
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
public class ElementFixeServiceImpl implements ElementFixeSalaireService {

    private ElementFixeSalaireRepository elementFixeSalaireRepository;
    private GeneriqueConnexionService service;

    @Autowired
    public ElementFixeServiceImpl(ElementFixeSalaireRepository elementFixeSalaireRepository, GeneriqueConnexionService service) {
        this.elementFixeSalaireRepository = elementFixeSalaireRepository;
        this.service = service;
    }

    @Override
    public ElementFixeSalaireDto save(ElementFixeSalaireDto dto) {
        return ElementFixeSalaireDto.fromEntity(
                elementFixeSalaireRepository.save(
                        ElementFixeSalaireDto.toEntity(dto)
                )
        );
    }

    @Override
    public ElementFixeSalaireDto findById(Integer id) {
        if (id == null) {
            log.error("Virement ID is null");
            return null;
        }

        return elementFixeSalaireRepository.findById(id).map(ElementFixeSalaireDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun virement avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<ElementFixeSalaireDto> findAll() {
        List<ElementFixeSalaireDto> liste = new ArrayList<ElementFixeSalaireDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.lrub as librubrique " +
                "FROM ElementFixeSalaire e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ElementSalaire t ON (t.identreprise=e.identreprise AND t.crub=e.codp) "+
                "WHERE 1=1";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", ElementFixeSalaire.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("librubrique", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                ElementFixeSalaire evDB = (ElementFixeSalaire) o[0];
                ElementFixeSalaireDto evDto = ElementFixeSalaireDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomSalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomSalarie(evDto.getNomSalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setLibrubrique(o[3].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }

    @Override
    public List<ElementFixeSalaireDto> findByMatricule(String matricule) {
        List<ElementFixeSalaireDto> liste = new ArrayList<ElementFixeSalaireDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.lrub as librubrique " +
                "FROM ElementFixeSalaire e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ElementSalaire t ON (t.identreprise=e.identreprise AND t.crub=e.codp) "+
                "WHERE e.nmat='"+matricule+"'";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", ElementFixeSalaire.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("librubrique", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                ElementFixeSalaire evDB = (ElementFixeSalaire)o[0];
                ElementFixeSalaireDto evDto = ElementFixeSalaireDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomSalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomSalarie(evDto.getNomSalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setLibrubrique(o[3].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Dossier ID is null");
            return;
        }
        elementFixeSalaireRepository.deleteById(id);
    }
}
