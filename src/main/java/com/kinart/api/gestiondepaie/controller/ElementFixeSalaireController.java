package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.ElementFixeSalaireApi;
import com.kinart.api.gestiondepaie.dto.ElementFixeSalaireDto;
import com.kinart.api.gestiondepaie.dto.ElementSalaireDto;
import com.kinart.paie.business.model.ElementFixeSalaire;
import com.kinart.paie.business.services.ElementFixeSalaireService;
import com.kinart.paie.business.services.ElementSalaireService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ElementFixeSalaireController implements ElementFixeSalaireApi {

    private ElementFixeSalaireService elementFixeSalaireService;

    @Autowired
    public ElementFixeSalaireController(ElementFixeSalaireService elementFixeSalaireService) {
        this.elementFixeSalaireService = elementFixeSalaireService;
    }

    @Override
    public ResponseEntity<ElementFixeSalaireDto> save(ElementFixeSalaireDto dto) {
        try {
            elementFixeSalaireService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ElementFixeSalaireDto> findById(Integer id) {
        ElementFixeSalaireDto elementSalaireDto = elementFixeSalaireService.findById(id);
        if(elementSalaireDto!=null) return ResponseEntity.ok(elementSalaireDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<ElementFixeSalaireDto>> findAll() {
        List<ElementFixeSalaireDto> elementSalaireDto = elementFixeSalaireService.findAll();
        if(elementSalaireDto!=null) {
            return ResponseEntity.ok(elementSalaireDto);
        } else {
            throw new EntityNotFoundException("Pas d'éléments trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        elementFixeSalaireService.delete(id);
    }

    @Override
    public ResponseEntity<List<ElementFixeSalaireDto>> findByMatricule(String matricule) {
        List<ElementFixeSalaireDto> elementSalaireDto = elementFixeSalaireService.findByMatricule(matricule);
        if(elementSalaireDto!=null) {
            return ResponseEntity.ok(elementSalaireDto);
        } else {
            throw new EntityNotFoundException("Pas d'éléments trouvés");
        }
    }
}
