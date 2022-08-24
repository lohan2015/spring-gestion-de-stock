package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.dto.ElementSalaireBaremeDto;
import com.kinart.api.gestiondepaie.controller.api.ElementBaremeApi;
import com.kinart.paie.business.services.ElementBaremeService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ElementBaremeController implements ElementBaremeApi {

    private ElementBaremeService baremeService;

    @Autowired
    public ElementBaremeController(ElementBaremeService baremeService) {
        this.baremeService = baremeService;
    }

    @Override
    public ResponseEntity<ElementSalaireBaremeDto> save(ElementSalaireBaremeDto dto) {
        try {
            baremeService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ElementSalaireBaremeDto> findById(Integer id) {
        ElementSalaireBaremeDto elementSalaireBaseDto = baremeService.findById(id);
        if(elementSalaireBaseDto!=null) {
            return ResponseEntity.ok(elementSalaireBaseDto);
        } else {
            throw new EntityNotFoundException("Pas de barème trouvés");
        }
    }

    @Override
    public ResponseEntity<List<ElementSalaireBaremeDto>> findByCodeRub(String codeRubrique) {
        List<ElementSalaireBaremeDto> elementSalaireBaseDtos = baremeService.findByCodeRub(codeRubrique);
        if(elementSalaireBaseDtos!=null) return ResponseEntity.ok(elementSalaireBaseDtos);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public void delete(Integer id) {
        baremeService.delete(id);
    }
}
