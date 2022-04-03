package com.cmbassi.gestiondestock.controller;


import com.cmbassi.gestiondestock.controller.api.UtilisateurApi;
import com.cmbassi.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.cmbassi.gestiondestock.dto.UtilisateurDto;
import com.cmbassi.gestiondestock.services.UtilisateurService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilisateurController implements UtilisateurApi {

  private UtilisateurService utilisateurService;

  @Autowired
  public UtilisateurController(UtilisateurService utilisateurService) throws Exception {
    this.utilisateurService = utilisateurService;
  }

  @Override
  public UtilisateurDto save(UtilisateurDto dto) throws Exception {
    return utilisateurService.save(dto);
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
