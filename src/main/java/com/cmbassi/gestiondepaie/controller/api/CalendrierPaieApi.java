package com.cmbassi.gestiondepaie.controller.api;

import com.cmbassi.gestiondepaie.dto.CalendrierPaieDto;
import com.cmbassi.gestiondepaie.dto.DossierPaieDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cmbassi.gestiondestock.utils.Constants.APP_ROOT_PAIE;

public interface CalendrierPaieApi {

    @PostMapping(value = APP_ROOT_PAIE + "/calendrierpaie/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un calendrier de paie", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = CalendrierPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<CalendrierPaieDto> save(@RequestBody CalendrierPaieDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/calendrierpaie/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un calul par ID", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            CalendrierPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<CalendrierPaieDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/calendrierpaie/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des calendrier", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DossierPaieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<CalendrierPaieDto>> findAll();

    @DeleteMapping(value = APP_ROOT_PAIE + "/calendrierpaie/delete/{id}")
    @ApiOperation(value = "Supprimer un dossier", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("id") Integer id);
}
