package com.kinart.api.gestiondestock.controller;

import com.kinart.api.gestiondestock.controller.api.EntrepriseApi;
import com.kinart.api.gestiondestock.dto.EntrepriseDto;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import com.kinart.stock.business.services.EntrepriseService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntrepriseController implements EntrepriseApi {

  private EntrepriseService entrepriseService;

  @Autowired
  public EntrepriseController(EntrepriseService entrepriseService) {
    this.entrepriseService = entrepriseService;
  }

  @Override
  public ResponseEntity<EntrepriseDto> save(EntrepriseDto dto) {
    try {
      entrepriseService.save(dto);
    } catch (InvalidEntityException e){
        return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity(dto, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<EntrepriseDto> findById(Integer id) {
    EntrepriseDto entreprise = entrepriseService.findById(id);
    if(entreprise!=null) return ResponseEntity.ok(entreprise);
    return new ResponseEntity(null, HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<List<EntrepriseDto>> findAll() {
    List<EntrepriseDto> entreprises = entrepriseService.findAll();
    if(entreprises!=null) {
      return ResponseEntity.ok(entreprises);
    } else {
      throw new EntityNotFoundException("Pas d'entreprise trouvée");
    }
  }

  @Override
  public void delete(Integer id) {
    entrepriseService.delete(id);
  }
}
