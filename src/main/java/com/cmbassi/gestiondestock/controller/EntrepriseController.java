package com.cmbassi.gestiondestock.controller;

import com.cmbassi.gestiondestock.controller.api.EntrepriseApi;
import com.cmbassi.gestiondestock.dto.EntrepriseDto;
import com.cmbassi.gestiondestock.services.EntrepriseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntrepriseController implements EntrepriseApi {

  private EntrepriseService entrepriseService;

  @Autowired
  public EntrepriseController(EntrepriseService entrepriseService) {
    this.entrepriseService = entrepriseService;
  }

  @Override
  public EntrepriseDto save(EntrepriseDto dto) throws Exception {
    return entrepriseService.save(dto);
  }

  @Override
  public EntrepriseDto findById(Integer id) throws Exception {
    return entrepriseService.findById(id);
  }

  @Override
  public List<EntrepriseDto> findAll() throws Exception {
    return entrepriseService.findAll();
  }

  @Override
  public void delete(Integer id) throws Exception {
    entrepriseService.delete(id);
  }
}
