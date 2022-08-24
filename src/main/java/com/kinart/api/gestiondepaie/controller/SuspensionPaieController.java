package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.SuspensionPaieApi;
import com.kinart.api.gestiondepaie.dto.SuspensionPaieDto;
import com.kinart.paie.business.services.SuspensionPaieService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SuspensionPaieController implements SuspensionPaieApi {

    private SuspensionPaieService suspensionPaieService;

    @Autowired
    public SuspensionPaieController(SuspensionPaieService suspensionPaieService) {
        this.suspensionPaieService = suspensionPaieService;
    }

    @Override
    public ResponseEntity<SuspensionPaieDto> save(SuspensionPaieDto dto) {
        try {
            suspensionPaieService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SuspensionPaieDto> findById(Integer id) {
        SuspensionPaieDto elementSalaireDto = suspensionPaieService.findById(id);
        if(elementSalaireDto!=null) return ResponseEntity.ok(elementSalaireDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<SuspensionPaieDto>> findAll() {
        List<SuspensionPaieDto> elementSalaireDto = suspensionPaieService.findAll();
        if(elementSalaireDto!=null) {
            return ResponseEntity.ok(elementSalaireDto);
        } else {
            throw new EntityNotFoundException("Pas d'éléments trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        suspensionPaieService.delete(id);
    }

    @Override
    public ResponseEntity<List<SuspensionPaieDto>> findByMatricule(String matricule) {
        List<SuspensionPaieDto> elementSalaireDto = suspensionPaieService.findByMatricule(matricule);
        if(elementSalaireDto!=null) {
            return ResponseEntity.ok(elementSalaireDto);
        } else {
            throw new EntityNotFoundException("Pas d'éléments trouvés");
        }
    }
}
