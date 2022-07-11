package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.SuspensionPaieDto;
import com.kinart.paie.business.model.SuspensionPaie;
import com.kinart.paie.business.model.VirementSalarie;
import com.kinart.paie.business.repository.SuspensionPaieRepository;
import com.kinart.paie.business.services.SuspensionPaieService;
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
public class SuspensionPaieServiceImpl implements SuspensionPaieService {

    private SuspensionPaieRepository suspensionPaieRepository;
    private GeneriqueConnexionService service;

    @Autowired
    public SuspensionPaieServiceImpl(SuspensionPaieRepository suspensionPaieRepository, GeneriqueConnexionService service) {
        this.suspensionPaieRepository = suspensionPaieRepository;
        this.service = service;
    }

    @Override
    public SuspensionPaieDto save(SuspensionPaieDto dto) {
        return SuspensionPaieDto.fromEntity(
                suspensionPaieRepository.save(
                        SuspensionPaieDto.toEntity(dto)
                )
        );
    }

    @Override
    public SuspensionPaieDto findById(Integer id) {
        if (id == null) {
            log.error("Virement ID is null");
            return null;
        }

        return suspensionPaieRepository.findById(id).map(SuspensionPaieDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun virement avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<SuspensionPaieDto> findAll() {
        List<SuspensionPaieDto> liste = new ArrayList<SuspensionPaieDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.vall as libsuspension " +
                "FROM SuspensionPaieDto e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ParamData t ON (t.identreprise=e.identreprise AND t.cacc=e.mtar AND t.ctab=21 AND t.nume=1) "+
                "WHERE 1=1";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", VirementSalarie.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("libsuspension", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                SuspensionPaie evDB = (SuspensionPaie) o[0];
                SuspensionPaieDto evDto = SuspensionPaieDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomSalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomSalarie(evDto.getNomSalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setLibmotif(o[3].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }

    @Override
    public List<SuspensionPaieDto> findByMatricule(String matricule) {
        List<SuspensionPaieDto> liste = new ArrayList<SuspensionPaieDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.vall as libsuspension " +
                "FROM SuspensionPaie e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ParamData t ON (t.identreprise=e.identreprise AND t.cacc=e.mtar AND t.ctab=21 AND t.nume=1) "+
                "WHERE e.nmat='"+matricule+"'";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", VirementSalarie.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("libsuspension", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                SuspensionPaie evDB = (SuspensionPaie) o[0];
                SuspensionPaieDto evDto = SuspensionPaieDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomSalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomSalarie(evDto.getNomSalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setLibmotif(o[3].toString());

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
        suspensionPaieRepository.deleteById(id);
    }
}
