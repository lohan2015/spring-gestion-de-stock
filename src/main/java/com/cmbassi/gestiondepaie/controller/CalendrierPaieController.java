package com.cmbassi.gestiondepaie.controller;

import com.cmbassi.gestiondepaie.controller.api.CalendrierPaieApi;
import com.cmbassi.gestiondepaie.dto.CalendrierPaieDto;
import com.cmbassi.gestiondepaie.dto.DossierPaieDto;
import com.cmbassi.gestiondepaie.services.CalendrierPaieService;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CalendrierPaieController implements CalendrierPaieApi {

    private CalendrierPaieService calendrierPaieService;

    @Autowired
    public CalendrierPaieController(CalendrierPaieService calendrierPaieService) {
        this.calendrierPaieService = calendrierPaieService;
    }

    @Override
    public ResponseEntity<CalendrierPaieDto> save(CalendrierPaieDto dto) {
        try {
            calendrierPaieService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CalendrierPaieDto> findById(Integer id) {
        CalendrierPaieDto calendrierPaieDto = calendrierPaieService.findById(id);
        if(calendrierPaieDto!=null) return ResponseEntity.ok(calendrierPaieDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<CalendrierPaieDto>> findAll() {
        List<CalendrierPaieDto> calendrierPaieDto = calendrierPaieService.findAll();
        if(calendrierPaieDto!=null) {
            return ResponseEntity.ok(calendrierPaieDto);
        } else {
            throw new EntityNotFoundException("Pas de calendriers trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        calendrierPaieService.delete(id);
    }
}
