package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.CalculPaieApi;
import com.kinart.api.gestiondepaie.dto.CalculPaieDto;
import com.kinart.paie.business.services.CalculPaieService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

 @RestController
public class CalculPaieController implements CalculPaieApi {

    private CalculPaieService calculPaieService;

    @Autowired
    public CalculPaieController(CalculPaieService calculPaieService) {
         this.calculPaieService = calculPaieService;
    }

     @Override
    public ResponseEntity<CalculPaieDto> save(CalculPaieDto dto) {
         try {
             calculPaieService.save(dto);
         } catch (InvalidEntityException e){
             return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
         }

         return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public boolean calculPaieSalarie(String matricule, String periode, Integer numeBul) {
        return calculPaieService.calculPaieSalarie(matricule, periode, numeBul);
    }

    @Override
    public ResponseEntity<List<CalculPaieDto>> findByMatriculeAndPeriod(String matricule, String periode, Integer numeBul) {
        List<CalculPaieDto> calculPaieDto = calculPaieService.findByMatriculeAndPeriod(matricule, periode, numeBul);
        if(calculPaieDto!=null) {
            return ResponseEntity.ok(calculPaieDto);
        } else {
            throw new EntityNotFoundException("Pas de bulletin trouvés");
        }
    }

    @Override
    public ResponseEntity<CalculPaieDto> findById(Integer id) {
        CalculPaieDto calculPaieDto = calculPaieService.findById(id);
        if(calculPaieDto!=null) return ResponseEntity.ok(calculPaieDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<CalculPaieDto>> findAll() {
        List<CalculPaieDto> calculPaieDto = calculPaieService.findAll();
        if(calculPaieDto!=null) {
            return ResponseEntity.ok(calculPaieDto);
        } else {
            throw new EntityNotFoundException("Pas de buleltins trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        calculPaieService.delete(id);
    }
}
