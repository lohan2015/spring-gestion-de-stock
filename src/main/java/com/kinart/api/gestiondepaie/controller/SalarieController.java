package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.SalarieApi;
import com.kinart.api.gestiondepaie.dto.SalarieDto;
import com.kinart.paie.business.services.SalarieService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SalarieController implements SalarieApi {

    private SalarieService salarieService;

    @Autowired
    public SalarieController(SalarieService salarieService) {
        this.salarieService = salarieService;
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
}
