package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.kinart.api.gestiondepaie.controller.api.ElementVariableApi;
import com.kinart.api.gestiondepaie.dto.RechercheDto;
import com.kinart.paie.business.services.ElementVariableService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<ElementVariableDetailMoisDto>> findEVByFilter(RechercheDto dto) {
        List<ElementVariableDetailMoisDto> elementVariableDetailMoisDto = elementVariableService.findEVByFilter(dto.nmatMin, dto.rubMin);
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
