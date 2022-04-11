package com.cmbassi.gestiondestock.controller.api;

import static com.cmbassi.gestiondestock.utils.Constants.ENTREPRISE_ENDPOINT;

import com.cmbassi.gestiondestock.dto.EntrepriseDto;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import io.swagger.annotations.Api;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("entreprises")
public interface EntrepriseApi {

  @PostMapping(value=ENTREPRISE_ENDPOINT + "/create", consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  ResponseEntity<EntrepriseDto> save(@RequestBody EntrepriseDto dto);

  @GetMapping(ENTREPRISE_ENDPOINT + "/{idEntreprise}")
  ResponseEntity<EntrepriseDto> findById(@PathVariable("idEntreprise") Integer id);

  @GetMapping(value=ENTREPRISE_ENDPOINT + "/all", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  ResponseEntity<List<EntrepriseDto>> findAll();

  @DeleteMapping(ENTREPRISE_ENDPOINT + "/delete/{idEntreprise}")
  void delete(@PathVariable("idEntreprise") Integer id);

}
