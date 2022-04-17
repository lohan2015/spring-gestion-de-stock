package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.ElementSalaireApi;
import com.kinart.api.gestiondepaie.dto.ElementSalaireDto;
import com.kinart.paie.business.services.ElementSalaireService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ElementSalaireController implements ElementSalaireApi {

    private ElementSalaireService elementSalaireService;

    @Autowired
    public ElementSalaireController(ElementSalaireService elementSalaireService) {
        this.elementSalaireService = elementSalaireService;
    }

    @Override
    public ResponseEntity<ElementSalaireDto> save(ElementSalaireDto dto) {
        try {
            elementSalaireService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ElementSalaireDto> findById(Integer id) {
        ElementSalaireDto elementSalaireDto = elementSalaireService.findById(id);
        if(elementSalaireDto!=null) return ResponseEntity.ok(elementSalaireDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<ElementSalaireDto>> findByCode(String code) {
        List<ElementSalaireDto> elementSalaireDto = elementSalaireService.findByCode(code);
        if(elementSalaireDto!=null) {
            return ResponseEntity.ok(elementSalaireDto);
        } else {
            throw new EntityNotFoundException("Pas d'éléments trouvés");
        }
    }

    @Override
    public ResponseEntity<List<ElementSalaireDto>> findByLibelle(String libelle) {
        List<ElementSalaireDto> elementSalaireDto = elementSalaireService.findByLibelle(libelle);
        if(elementSalaireDto!=null) {
            return ResponseEntity.ok(elementSalaireDto);
        } else {
            throw new EntityNotFoundException("Pas d'éléments trouvés");
        }
    }

    @Override
    public ResponseEntity<List<ElementSalaireDto>> findAll() {
        List<ElementSalaireDto> elementSalaireDto = elementSalaireService.findAll();
        if(elementSalaireDto!=null) {
            return ResponseEntity.ok(elementSalaireDto);
        } else {
            throw new EntityNotFoundException("Pas d'éléments trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        elementSalaireService.delete(id);
    }
}
