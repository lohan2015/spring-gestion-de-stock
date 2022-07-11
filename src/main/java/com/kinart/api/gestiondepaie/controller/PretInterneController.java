package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.PretInterneApi;
import com.kinart.api.gestiondepaie.dto.ElementSalaireDto;
import com.kinart.api.gestiondepaie.dto.PretExterneEnteteDto;
import com.kinart.api.gestiondepaie.dto.PretInterneDto;
import com.kinart.api.gestiondepaie.dto.VirementSalarieDto;
import com.kinart.paie.business.services.PretExterneService;
import com.kinart.paie.business.services.PretInterneService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PretInterneController implements PretInterneApi {

    private PretInterneService pretInterneService;

    @Autowired
    public PretInterneController(PretInterneService pretInterneService) {
        this.pretInterneService = pretInterneService;
    }

    @Override
    public ResponseEntity<PretInterneDto> save(PretInterneDto dto) {
        try {
            pretInterneService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PretInterneDto> findById(Integer id) {
        PretInterneDto entetePretByFilter = pretInterneService.findById(id);
        if(entetePretByFilter!=null) {
            return ResponseEntity.ok(entetePretByFilter);
        } else {
            throw new EntityNotFoundException("Pas de prêts trouvés");
        }
    }

    @Override
    public ResponseEntity<List<PretInterneDto>> findByMatricule(String matricule) {
        List<PretInterneDto> pretInterneDtos = pretInterneService.findByMatricule(matricule);
        if(pretInterneDtos!=null) return ResponseEntity.ok(pretInterneDtos);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public void delete(Integer id) {
        pretInterneService.delete(id);
    }

    @Override
    public ResponseEntity<List<ElementSalaireDto>> findRubriqueForPretInterne() {
        List<ElementSalaireDto> pretInterneDtos = pretInterneService.findRubriqueForPretInterne();
        if(pretInterneDtos!=null) return ResponseEntity.ok(pretInterneDtos);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
}
