package com.kinart.api.organisation.controller;

import com.kinart.api.organisation.controller.api.NiveauETApi;
import com.kinart.api.organisation.dto.NiveauemploitypeDto;
import com.kinart.organisation.business.services.NiveauEmploitypeService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NiveauETController implements NiveauETApi {

    private NiveauEmploitypeService service;

    @Autowired
    public NiveauETController(NiveauEmploitypeService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<NiveauemploitypeDto> save(NiveauemploitypeDto dto) {
        try {
            service.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<NiveauemploitypeDto> findById(Integer id) {
        NiveauemploitypeDto dto = service.findById(id);
        if(dto!=null) return ResponseEntity.ok(dto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<NiveauemploitypeDto>> findAll(String codeDossier) {
        List<NiveauemploitypeDto> niveauDtos = service.findAll();
        if(niveauDtos!=null) {
            return ResponseEntity.ok(niveauDtos);
        } else {
            throw new EntityNotFoundException("Pas de niveaux trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        service.delete(id);
    }
}
