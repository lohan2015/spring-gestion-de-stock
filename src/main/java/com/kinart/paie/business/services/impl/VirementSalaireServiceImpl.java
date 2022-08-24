package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.VirementSalarieDto;
import com.kinart.paie.business.model.VirementSalarie;
import com.kinart.paie.business.repository.VirementSalaireRepository;
import com.kinart.paie.business.services.VirementSalaireService;
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
public class VirementSalaireServiceImpl implements VirementSalaireService {

    private VirementSalaireRepository virementSalaireRepository;
    private GeneriqueConnexionService service;

    @Autowired
    public VirementSalaireServiceImpl(VirementSalaireRepository virementSalaireRepository, GeneriqueConnexionService service) {
        this.virementSalaireRepository = virementSalaireRepository;
        this.service = service;
    }

    @Override
    public VirementSalarieDto save(VirementSalarieDto dto) {
        return VirementSalarieDto.fromEntity(
                virementSalaireRepository.save(
                        VirementSalarieDto.toEntity(dto)
                )
        );
    }

    @Override
    public VirementSalarieDto findById(Integer id) {
        if (id == null) {
            log.error("Virement ID is null");
            return null;
        }

        return virementSalaireRepository.findById(id).map(VirementSalarieDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun virement avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public VirementSalarieDto findByMatricule(String matricule) {
        return VirementSalarieDto.fromEntity(virementSalaireRepository.findBySalarie(matricule));
    }

    @Override
    public List<VirementSalarieDto> findAll() {
        List<VirementSalarieDto> liste = new ArrayList<VirementSalarieDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.vall as libbanque " +
                "FROM VirementSalarie e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ParamData t ON (t.identreprise=e.identreprise AND t.cacc=e.bqag AND t.ctab=10 AND t.nume=1) "+
                "WHERE 1=1";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", VirementSalarie.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("libbanque", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                VirementSalarie evDB = (VirementSalarie)o[0];
                VirementSalarieDto evDto = VirementSalarieDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomSalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomSalarie(evDto.getNomSalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setNomBanqueAgent(o[3].toString());

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
        virementSalaireRepository.deleteById(id);
    }

    @Override
    public List<VirementSalarieDto> findDetailByMatricule(String matricule) {
        List<VirementSalarieDto> liste = new ArrayList<VirementSalarieDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.vall as libbanque " +
                "FROM VirementSalarie e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ParamData t ON (t.identreprise=e.identreprise AND t.cacc=e.bqag AND t.ctab=10 AND t.nume=1) "+
                "WHERE e.nmat='"+matricule+"'";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", VirementSalarie.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("libbanque", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                VirementSalarie evDB = (VirementSalarie)o[0];
                VirementSalarieDto evDto = VirementSalarieDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomSalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomSalarie(evDto.getNomSalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setNomBanqueAgent(o[3].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }

}
