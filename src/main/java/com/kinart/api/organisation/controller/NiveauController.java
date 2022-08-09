package com.kinart.api.organisation.controller;

import com.kinart.api.organisation.controller.api.NiveauApi;
import com.kinart.api.organisation.dto.NiveauDto;
import com.kinart.api.organisation.dto.PosteDto;
import com.kinart.organisation.business.services.NiveauService;
import com.kinart.organisation.business.services.PosteService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NiveauController implements NiveauApi {

    private NiveauService service;

    @Autowired
    public NiveauController(NiveauService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<NiveauDto> save(NiveauDto dto) {
        try {
            service.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<NiveauDto> findById(Integer id) {
        NiveauDto dto = service.findById(id);
        if(dto!=null) return ResponseEntity.ok(dto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<NiveauDto>> findAll(String codeDossier) {
        List<NiveauDto> niveauDtos = service.findAll();
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
