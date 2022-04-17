package com.kinart.api.gestiondestock.controller;

import com.kinart.api.gestiondestock.controller.api.FournisseurApi;
import com.kinart.api.gestiondestock.dto.FournisseurDto;
import com.kinart.stock.business.exception.InvalidEntityException;
import com.kinart.stock.business.services.FournisseurService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FournisseurController implements FournisseurApi {

  private FournisseurService fournisseurService;

  @Autowired
  public FournisseurController(FournisseurService fournisseurService) {
    this.fournisseurService = fournisseurService;
  }

  @Override
  public ResponseEntity<FournisseurDto> save(FournisseurDto dto) {
    try {
      fournisseurService.save(dto);
    } catch (InvalidEntityException e){
      return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity(dto, HttpStatus.CREATED);
  }

  @Override
  public FournisseurDto findById(Integer id) {
    return fournisseurService.findById(id);
  }

  @Override
  public List<FournisseurDto> findAll() {
    return fournisseurService.findAll();
  }

  @Override
  public void delete(Integer id) {
    fournisseurService.delete(id);
  }
}
