package com.kinart.api.gestiondestock.controller.api;


import static com.kinart.stock.business.utils.Constants.UTILISATEUR_ENDPOINT;

import com.kinart.api.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.kinart.api.gestiondestock.dto.UtilisateurDto;
import io.swagger.annotations.Api;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("utilisateurs")
public interface UtilisateurApi {

  @PostMapping(value =UTILISATEUR_ENDPOINT + "/create", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
  ResponseEntity<UtilisateurDto> save(@RequestBody UtilisateurDto dto) throws Exception;

  @PostMapping(value =UTILISATEUR_ENDPOINT + "/update/password", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
  UtilisateurDto changerMotDePasse(@RequestBody ChangerMotDePasseUtilisateurDto dto) throws Exception;

  @GetMapping(value =UTILISATEUR_ENDPOINT + "/{idUtilisateur}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
  UtilisateurDto findById(@PathVariable("idUtilisateur") Integer id) throws Exception;

  @GetMapping(value =UTILISATEUR_ENDPOINT + "/find/{email}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
  UtilisateurDto findByEmail(@PathVariable("email") String email) throws Exception;

  @GetMapping(value =UTILISATEUR_ENDPOINT + "/all", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
  List<UtilisateurDto> findAll() throws Exception;

  @DeleteMapping(value =UTILISATEUR_ENDPOINT + "/delete/{idUtilisateur}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
  void delete(@PathVariable("idUtilisateur") Integer id) throws Exception;

}
