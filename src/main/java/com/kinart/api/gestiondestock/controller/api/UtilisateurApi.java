package com.kinart.api.gestiondestock.controller.api;


import static com.kinart.stock.business.utils.Constants.UTILISATEUR_ENDPOINT;

import com.kinart.api.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.kinart.api.gestiondestock.dto.UtilisateurDto;
import io.swagger.annotations.Api;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("utilisateurs")
public interface UtilisateurApi {

  @PostMapping(UTILISATEUR_ENDPOINT + "/create")
  ResponseEntity<UtilisateurDto> save(@RequestBody UtilisateurDto dto) throws Exception;

  @PostMapping(UTILISATEUR_ENDPOINT + "/update/password")
  UtilisateurDto changerMotDePasse(@RequestBody ChangerMotDePasseUtilisateurDto dto) throws Exception;

  @GetMapping(UTILISATEUR_ENDPOINT + "/{idUtilisateur}")
  UtilisateurDto findById(@PathVariable("idUtilisateur") Integer id) throws Exception;

  @GetMapping(UTILISATEUR_ENDPOINT + "/find/{email}")
  UtilisateurDto findByEmail(@PathVariable("email") String email) throws Exception;

  @GetMapping(UTILISATEUR_ENDPOINT + "/all")
  List<UtilisateurDto> findAll() throws Exception;

  @DeleteMapping(UTILISATEUR_ENDPOINT + "/delete/{idUtilisateur}")
  void delete(@PathVariable("idUtilisateur") Integer id) throws Exception;

}
