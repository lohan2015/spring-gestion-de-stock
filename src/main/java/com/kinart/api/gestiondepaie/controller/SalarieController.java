package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.SalarieApi;
import com.kinart.api.gestiondepaie.dto.ElementFixeSalaireDto;
import com.kinart.api.gestiondepaie.dto.SalarieDto;
import com.kinart.paie.business.model.ConjointSalarie;
import com.kinart.paie.business.model.ElementFixeSalaire;
import com.kinart.paie.business.model.EnfantSalarie;
import com.kinart.paie.business.services.SalarieService;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SalarieController implements SalarieApi {

    private SalarieService salarieService;
    private GeneriqueConnexionService service;

    @Autowired
    public SalarieController(SalarieService salarieService, GeneriqueConnexionService service) {

        this.salarieService = salarieService;
        this.service = service;
    }

    @Override
    public ResponseEntity<SalarieDto> save(SalarieDto dto) {
        try {
            salarieService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SalarieDto> findById(Integer id) {
        SalarieDto salarieDto = salarieService.findById(id);
        if(salarieDto!=null) return ResponseEntity.ok(salarieDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<SalarieDto>> findByMatricule(String nmat) {
        List<SalarieDto> salariesDto = salarieService.findByMatricule(nmat);
        if(salariesDto!=null) {
            return ResponseEntity.ok(salariesDto);
        } else {
            throw new EntityNotFoundException("Pas de salariés trouvés");
        }
    }

    @Override
    public ResponseEntity<List<SalarieDto>> findAll() {
        List<SalarieDto> salariesDto = salarieService.findAll();
        if(salariesDto!=null) {
            return ResponseEntity.ok(salariesDto);
        } else {
            throw new EntityNotFoundException("Pas de salariés trouvés");
        }
    }

    @Override
    public ResponseEntity<List<SalarieDto>> findByName(String nom) {
        List<SalarieDto> salariesDto = salarieService.findByName(nom);
        if(salariesDto!=null) {
            return ResponseEntity.ok(salariesDto);
        } else {
            throw new EntityNotFoundException("Pas de salariés trouvés");
        }
    }

    @Override
    public ResponseEntity<List<SalarieDto>> findByMatriculeInactif(String nmat) {
        List<SalarieDto> salariesDto = salarieService.findByMatriculeInactif(nmat);
        if(salariesDto!=null) {
            return ResponseEntity.ok(salariesDto);
        } else {
            throw new EntityNotFoundException("Pas de salariés trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        salarieService.delete(id);
    }

    @Override
    public ResponseEntity<List<EnfantSalarie>> loadEnfants(String nmat) {
        List<EnfantSalarie> liste = new ArrayList<EnfantSalarie>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal " +
                "FROM EnfantSalarie e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "WHERE e.nmat='"+nmat+"'";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", EnfantSalarie.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                EnfantSalarie evDB = (EnfantSalarie)o[0];
                liste.add(evDB);
            }

        } catch (Exception e){
            throw e;
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de enfants trouvés");
        }
    }

    @Override
    public ResponseEntity<List<ConjointSalarie>> loadConjoints(String nmat) {
        List<ConjointSalarie> liste = new ArrayList<ConjointSalarie>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal " +
                "FROM ConjointSalarie e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "WHERE e.nmat='"+nmat+"'";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", ConjointSalarie.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                ConjointSalarie evDB = (ConjointSalarie)o[0];
                liste.add(evDB);
            }

        } catch (Exception e){
            throw e;
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de enfants trouvés");
        }
    }
}
