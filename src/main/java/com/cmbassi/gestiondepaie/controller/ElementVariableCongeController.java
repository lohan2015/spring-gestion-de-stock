package com.cmbassi.gestiondepaie.controller;

import com.cmbassi.gestiondepaie.dto.ElementVariableCongeDto;
import com.cmbassi.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.cmbassi.gestiondepaie.services.ElementVariableCongeService;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ElementVariableCongeController implements com.cmbassi.gestiondepaie.controller.api.ElementVariableCongeApi {

    private ElementVariableCongeService elementVariableCongeService;

    @Autowired
    public ElementVariableCongeController(ElementVariableCongeService elementVariableCongeService) {
        this.elementVariableCongeService = elementVariableCongeService;
    }

    @Override
    public ResponseEntity<ElementVariableCongeDto> save(ElementVariableCongeDto dto, String dateFormat, String typeBD) {
        try {
            elementVariableCongeService.save(dto, dateFormat, typeBD);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ElementVariableCongeDto> findById(Integer id) {
        ElementVariableCongeDto elementVariableCongeDto = elementVariableCongeService.findById(id);
        if(elementVariableCongeDto!=null) return ResponseEntity.ok(elementVariableCongeDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<ElementVariableCongeDto>> findAll() {
        List<ElementVariableCongeDto> elementVariableCongeDto = elementVariableCongeService.findAll();
        if(elementVariableCongeDto!=null) {
            return ResponseEntity.ok(elementVariableCongeDto);
        } else {
            throw new EntityNotFoundException("Pas de EVs congé trouvés");
        }
    }

    @Override
    public ResponseEntity<List<ElementVariableCongeDto>> findEVCongeByFilter(Optional<String> matricule, Optional<String> codemotif) {
        List<ElementVariableCongeDto> elementVariableCongeDto = elementVariableCongeService.findEVCongeByFilter(matricule, codemotif);
        if(elementVariableCongeDto!=null) {
            return ResponseEntity.ok(elementVariableCongeDto);
        } else {
            throw new EntityNotFoundException("Pas de EV congé trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        elementVariableCongeService.delete(id);
    }
}
