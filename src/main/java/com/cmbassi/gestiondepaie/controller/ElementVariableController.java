package com.cmbassi.gestiondepaie.controller;

import com.cmbassi.gestiondepaie.controller.api.ElementVariableApi;
import com.cmbassi.gestiondepaie.dto.DossierPaieDto;
import com.cmbassi.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.cmbassi.gestiondepaie.services.ElementVariableService;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ElementVariableController implements ElementVariableApi {

    private ElementVariableService elementVariableService;

    @Autowired
    public ElementVariableController(ElementVariableService elementVariableService) {
        this.elementVariableService = elementVariableService;
    }

    @Override
    public ResponseEntity<ElementVariableDetailMoisDto> save(ElementVariableDetailMoisDto dto) {
        try {
            elementVariableService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ElementVariableDetailMoisDto> findById(Integer id) {
        ElementVariableDetailMoisDto elementVariableDetailMoisDto = elementVariableService.findDetailById(id);
        if(elementVariableDetailMoisDto!=null) return ResponseEntity.ok(elementVariableDetailMoisDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<ElementVariableDetailMoisDto>> findAll() {
        List<ElementVariableDetailMoisDto> elementVariableDetailMoisDto = elementVariableService.findDetailAll();
        if(elementVariableDetailMoisDto!=null) {
            return ResponseEntity.ok(elementVariableDetailMoisDto);
        } else {
            throw new EntityNotFoundException("Pas de EVs trouvés");
        }
    }

    @Override
    public ResponseEntity<List<ElementVariableDetailMoisDto>> findEVByFilter(Optional<String> matricule, Optional<String> coderub) {
        List<ElementVariableDetailMoisDto> elementVariableDetailMoisDto = elementVariableService.findEVByFilter(matricule, coderub);
        if(elementVariableDetailMoisDto!=null) {
            return ResponseEntity.ok(elementVariableDetailMoisDto);
        } else {
            throw new EntityNotFoundException("Pas de dossiers trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        elementVariableService.delete(id);
    }
}
