package com.kinart.api.gestiondestock.controller;

import com.kinart.api.gestiondestock.controller.api.CommandeFournisseurApi;
import com.kinart.api.gestiondestock.dto.CommandeFournisseurDto;
import com.kinart.api.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.kinart.stock.business.exception.InvalidEntityException;
import com.kinart.stock.business.model.EtatCommande;
import com.kinart.stock.business.services.CommandeFournisseurService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandeFournisseurController implements CommandeFournisseurApi {

  private CommandeFournisseurService commandeFournisseurService;

  @Autowired
  public CommandeFournisseurController(CommandeFournisseurService commandeFournisseurService) {
    this.commandeFournisseurService = commandeFournisseurService;
  }

  @Override
  public ResponseEntity<CommandeFournisseurDto> save(CommandeFournisseurDto dto) {
    try {
      commandeFournisseurService.save(dto);
    } catch (InvalidEntityException e){
      return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity(dto, HttpStatus.CREATED);
  }

  @Override
  public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
    return commandeFournisseurService.updateEtatCommande(idCommande, etatCommande);
  }

  @Override
  public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
    return commandeFournisseurService.updateQuantiteCommande(idCommande, idLigneCommande, quantite);
  }

  @Override
  public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
    return commandeFournisseurService.updateFournisseur(idCommande, idFournisseur);
  }

  @Override
  public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
    return commandeFournisseurService.updateArticle(idCommande, idLigneCommande, idArticle);
  }

  @Override
  public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
    return commandeFournisseurService.deleteArticle(idCommande, idLigneCommande);
  }

  @Override
  public CommandeFournisseurDto findById(Integer id) {
    return commandeFournisseurService.findById(id);
  }

  @Override
  public CommandeFournisseurDto findByCode(String code) {
    return commandeFournisseurService.findByCode(code);
  }

  @Override
  public List<CommandeFournisseurDto> findAll() {
    return commandeFournisseurService.findAll();
  }

  @Override
  public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
    return commandeFournisseurService.findAllLignesCommandesFournisseurByCommandeFournisseurId(idCommande);
  }

  @Override
  public void delete(Integer id) {
    commandeFournisseurService.delete(id);
  }
}
