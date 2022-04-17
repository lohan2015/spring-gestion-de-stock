package com.kinart.api.gestiondestock.controller;


import com.kinart.api.gestiondestock.controller.api.UtilisateurApi;
import com.kinart.api.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.kinart.api.gestiondestock.dto.UtilisateurDto;
import com.kinart.stock.business.exception.InvalidEntityException;
import com.kinart.stock.business.services.UtilisateurService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilisateurController implements UtilisateurApi {

  private UtilisateurService utilisateurService;

  @Autowired
  public UtilisateurController(UtilisateurService utilisateurService) throws Exception {
    this.utilisateurService = utilisateurService;
  }

  @Override
  public ResponseEntity<UtilisateurDto> save(UtilisateurDto dto) throws Exception {
    try {
      utilisateurService.save(dto);
    } catch (InvalidEntityException e){
      return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity(dto, HttpStatus.CREATED);
  }

  @Override
  public UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto) throws Exception {
    return utilisateurService.changerMotDePasse(dto);
  }

  @Override
  public UtilisateurDto findById(Integer id) throws Exception {
    return utilisateurService.findById(id);
  }

  @Override
  public UtilisateurDto findByEmail(String email) throws Exception {
    return utilisateurService.findByEmail(email);
  }

  @Override
  public List<UtilisateurDto> findAll() throws Exception {
    return utilisateurService.findAll();
  }

  @Override
  public void delete(Integer id) throws Exception {
    utilisateurService.delete(id);
  }
}
