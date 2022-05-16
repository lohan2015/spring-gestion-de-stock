package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.ElementVariableCongeApi;
import com.kinart.api.gestiondepaie.dto.ElementVariableCongeDto;
import com.kinart.api.gestiondepaie.dto.RechercheDto;
import com.kinart.paie.business.services.ElementVariableCongeService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ElementVariableCongeController implements ElementVariableCongeApi {

    private ElementVariableCongeService elementVariableCongeService;

    @Autowired
    public ElementVariableCongeController(ElementVariableCongeService elementVariableCongeService) {
        this.elementVariableCongeService = elementVariableCongeService;
    }

    @Override
    public ResponseEntity<ElementVariableCongeDto> save(ElementVariableCongeDto dto) {
        try {
            elementVariableCongeService.save(dto, dto.getDateFormat(), dto.getTypeBD());
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
    public ResponseEntity<List<ElementVariableCongeDto>> findEVCongeByFilter(RechercheDto dto) {
        List<ElementVariableCongeDto> elementVariableCongeDto = elementVariableCongeService.findEVCongeByFilter(dto.nmatMin, dto.codeMotifCg);
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
