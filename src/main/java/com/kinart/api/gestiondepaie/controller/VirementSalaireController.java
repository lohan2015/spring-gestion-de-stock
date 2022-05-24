package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.VirementSalaireApi;
import com.kinart.api.gestiondepaie.dto.VirementSalarieDto;
import com.kinart.paie.business.services.VirementSalaireService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VirementSalaireController implements VirementSalaireApi {

    private VirementSalaireService virementSalaireService;

    @Autowired
    public VirementSalaireController(VirementSalaireService virementSalaireService) {
        this.virementSalaireService = virementSalaireService;
    }

    @Override
    public ResponseEntity<VirementSalarieDto> save(VirementSalarieDto dto) {
        try {
            virementSalaireService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<VirementSalarieDto> findById(Integer id) {
        VirementSalarieDto virementSalarieDto = virementSalaireService.findById(id);
        if(virementSalarieDto!=null) return ResponseEntity.ok(virementSalarieDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<VirementSalarieDto> findByMatricule(String matricule) {
        VirementSalarieDto virementSalarieDto = virementSalaireService.findByMatricule(matricule);
        if(virementSalarieDto!=null) return ResponseEntity.ok(virementSalarieDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<VirementSalarieDto>> findAll() {
        List<VirementSalarieDto> virementSalarieDtos = virementSalaireService.findAll();
        if(virementSalarieDtos!=null) {
            return ResponseEntity.ok(virementSalarieDtos);
        } else {
            throw new EntityNotFoundException("Pas de comptes trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        virementSalaireService.delete(id);
    }
}
