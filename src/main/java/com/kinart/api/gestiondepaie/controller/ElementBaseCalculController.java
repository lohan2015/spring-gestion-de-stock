package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.ElementBaseCalculApi;
import com.kinart.api.gestiondepaie.dto.ElementSalaireBaseDto;
import com.kinart.paie.business.services.ElementBaseCalculService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ElementBaseCalculController implements ElementBaseCalculApi {

    private ElementBaseCalculService baseCalculService;

    @Autowired
    public ElementBaseCalculController(ElementBaseCalculService baseCalculService) {
        this.baseCalculService = baseCalculService;
    }

    @Override
    public ResponseEntity<ElementSalaireBaseDto> save(ElementSalaireBaseDto dto) {
        try {
            baseCalculService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ElementSalaireBaseDto> findById(Integer id) {
        ElementSalaireBaseDto elementSalaireBaseDto = baseCalculService.findById(id);
        if(elementSalaireBaseDto!=null) {
            return ResponseEntity.ok(elementSalaireBaseDto);
        } else {
            throw new EntityNotFoundException("Pas de bases de calcul trouvés");
        }
    }

    @Override
    public ResponseEntity<List<ElementSalaireBaseDto>> findByCodeRub(String codeRubrique) {
        List<ElementSalaireBaseDto> elementSalaireBaseDtos = baseCalculService.findByCodeRub(codeRubrique);
        if(elementSalaireBaseDtos!=null) return ResponseEntity.ok(elementSalaireBaseDtos);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public void delete(Integer id) {
        baseCalculService.delete(id);
    }
}
