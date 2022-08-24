package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.ElementSalaireDto;
import com.kinart.api.gestiondepaie.dto.PretInterneDto;
import com.kinart.paie.business.model.PretInterne;
import com.kinart.paie.business.repository.PretInterneRepository;
import com.kinart.paie.business.services.PretInterneService;
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
public class PretInterneServiceImpl implements PretInterneService {

    private PretInterneRepository pretInterneRepository;
    private GeneriqueConnexionService service;

    @Autowired
    public PretInterneServiceImpl(PretInterneRepository pretInterneRepository, GeneriqueConnexionService service) {
        this.pretInterneRepository = pretInterneRepository;
        this.service = service;
    }

    @Override
    public PretInterneDto save(PretInterneDto dto) {
        return PretInterneDto.fromEntity(
                pretInterneRepository.save(
                        PretInterneDto.toEntity(dto)
                )
        );
    }

    @Override
    public PretInterneDto findById(Integer id) {
        if (id == null) {
            log.error("Virement ID is null");
            return null;
        }

        return pretInterneRepository.findById(id).map(PretInterneDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun prêt avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Dossier ID is null");
            return;
        }
        pretInterneRepository.deleteById(id);
    }

    @Override
    public List<PretInterneDto> findByMatricule(String matricule) {
        List<PretInterneDto> liste = new ArrayList<PretInterneDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.lrub as librubrique " +
                "FROM PretInterne e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ElementSalaire t ON (t.identreprise=e.identreprise AND t.crub=e.crub) "+
                "WHERE e.nmat='"+matricule+"'";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", PretInterne.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("librubrique", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                PretInterne evDB = (PretInterne)o[0];
                PretInterneDto evDto = PretInterneDto.fromEntity(evDB);
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
    public List<ElementSalaireDto> findRubriqueForPretInterne() {
        List<ElementSalaireDto> liste = new ArrayList<ElementSalaireDto>();
        String sqlQuery = "SELECT crub, lrub " +
                "FROM ElementSalaire " +
                "WHERE algo=13";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                ElementSalaireDto evDto = new ElementSalaireDto();
                if(o[0]!=null) evDto.setCrub(o[0].toString());
                if(o[1]!=null) evDto.setLrub(o[1].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }
}
