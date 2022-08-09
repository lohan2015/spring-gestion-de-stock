package com.kinart.api.organisation.controller;

import com.kinart.api.organisation.controller.api.PosteApi;
import com.kinart.api.organisation.dto.PosteDto;
import com.kinart.organisation.business.services.PosteService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PosteController implements PosteApi {

    private PosteService service;

    @Autowired
    public PosteController(PosteService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<PosteDto> save(PosteDto dto) {
        try {
            service.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PosteDto> findById(Integer id) {
        PosteDto dto = service.findById(id);
        if(dto!=null) return ResponseEntity.ok(dto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<PosteDto>> findAllPoste(String codeDossier) {
        List<PosteDto> posteDtos = service.findAllPoste(codeDossier);
        if(posteDtos!=null) {
            return ResponseEntity.ok(posteDtos);
        } else {
            throw new EntityNotFoundException("Pas de postes trouvés");
        }
    }

    @Override
    public ResponseEntity<List<PosteDto>> findAllMetier(String codeDossier) {
        List<PosteDto> posteDtos = service.findAllMetier(codeDossier);
        if(posteDtos!=null) {
            return ResponseEntity.ok(posteDtos);
        } else {
            throw new EntityNotFoundException("Pas de métiers trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        service.delete(id);
    }
}
