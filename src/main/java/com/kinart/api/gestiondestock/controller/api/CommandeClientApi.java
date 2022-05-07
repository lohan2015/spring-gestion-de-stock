package com.kinart.api.gestiondestock.controller.api;

import static com.kinart.stock.business.utils.Constants.APP_ROOT;

import com.kinart.api.gestiondestock.dto.ArticleDto;
import com.kinart.api.gestiondestock.dto.CommandeClientDto;
import com.kinart.api.gestiondestock.dto.LigneCommandeClientDto;
import com.kinart.stock.business.model.EtatCommande;
import io.swagger.annotations.Api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api("commandesclients")
public interface CommandeClientApi {


  @PostMapping(APP_ROOT + "/commandesclients/create")
  ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto dto);

  @PatchMapping(APP_ROOT + "/commandesclients/update/etat/{idCommande}/{etatCommande}")
  ResponseEntity<CommandeClientDto> updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCommande etatCommande);

  @PatchMapping(APP_ROOT + "/commandesclients/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
  ResponseEntity<CommandeClientDto> updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
      @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite);

  @PatchMapping(APP_ROOT + "/commandesclients/update/client/{idCommande}/{idClient}")
  ResponseEntity<CommandeClientDto> updateClient(@PathVariable("idCommande") Integer idCommande, @PathVariable("idClient") Integer idClient);

  @PatchMapping(APP_ROOT + "/commandesclients/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
  ResponseEntity<CommandeClientDto> updateArticle(@PathVariable("idCommande") Integer idCommande,
      @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle);

  @DeleteMapping(APP_ROOT + "/commandesclients/delete/article/{idCommande}/{idLigneCommande}")
  ResponseEntity<CommandeClientDto> deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande);

  @GetMapping(APP_ROOT + "/commandesclients/{idCommandeClient}")
  ResponseEntity<CommandeClientDto> findById(@PathVariable Integer idCommandeClient);

  @GetMapping(APP_ROOT + "/commandesclients/filter/{codeCommandeClient}")
  ResponseEntity<CommandeClientDto> findByCode(@PathVariable("codeCommandeClient") String code);

  @GetMapping(APP_ROOT + "/commandesclients/all")
  ResponseEntity<List<CommandeClientDto>> findAll();

  @GetMapping(APP_ROOT + "/commandesclients/lignesCommande/{idCommande}")
  ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Integer idCommande);

  @DeleteMapping(APP_ROOT + "/commandesclients/delete/{idCommandeClient}")
  ResponseEntity<Void> delete(@PathVariable("idCommandeClient") Integer id);

  @PostMapping(value=APP_ROOT + "/commandesclients/report", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Générer édition commande", notes = "Cette methode permet de générer édition commande")
  void getReport(@RequestBody CommandeClientDto dto, HttpServletRequest request, HttpServletResponse response);

}
