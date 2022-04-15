package com.cmbassi.gestiondepaie.controller;

import com.cmbassi.gestiondepaie.controller.api.PretExterneApi;
import com.cmbassi.gestiondepaie.dto.LigneEcheancierDto;
import com.cmbassi.gestiondepaie.dto.PretExterneEnteteDto;
import com.cmbassi.gestiondepaie.dto.SalarieDto;
import com.cmbassi.gestiondepaie.services.PretExterneService;
import com.cmbassi.gestiondepaie.services.impl.PretExterneServiceImpl;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PretExterneController implements PretExterneApi {

    private PretExterneService pretExterneService;

    @Autowired
    public PretExterneController(PretExterneService pretExterneService) {
        this.pretExterneService = pretExterneService;
    }

    @Override
    public ResponseEntity<PretExterneEnteteDto> save(PretExterneEnteteDto dto, List<LigneEcheancierDto> echeancier) {
        try {
            pretExterneService.save(dto, echeancier);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<PretExterneEnteteDto>> findPretEntetePretByFilter(Optional<String> matricule, Optional<String> nprt, Optional<String> crub) {
        List<PretExterneEnteteDto> entetePretByFilter = pretExterneService.findPretEntetePretByFilter(matricule, nprt, crub);
        if(entetePretByFilter!=null) {
            return ResponseEntity.ok(entetePretByFilter);
        } else {
            throw new EntityNotFoundException("Pas de prêts trouvés");
        }
    }

    @Override
    public ResponseEntity<List<LigneEcheancierDto>> generateEcheancier(PretExterneEnteteDto dto) {
        List<LigneEcheancierDto> ligneEcheancierDto = pretExterneService.generateEcheancier(dto);
        if(ligneEcheancierDto!=null) {
            return ResponseEntity.ok(ligneEcheancierDto);
        } else {
            throw new EntityNotFoundException("Pas d'échéances générés");
        }
    }

    @Override
    public ResponseEntity<List<LigneEcheancierDto>> loadEchancier(String numPret) {
        List<LigneEcheancierDto> ligneEcheancierDto = pretExterneService.loadEchancier(numPret);
        if(ligneEcheancierDto!=null) {
            return ResponseEntity.ok(ligneEcheancierDto);
        } else {
            throw new EntityNotFoundException("Pas de prêts trouvés");
        }
    }
}
