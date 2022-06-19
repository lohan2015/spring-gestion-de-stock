package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.DossierPaieDto;
import com.kinart.api.gestiondepaie.dto.ElementFixeSalaireDto;
import com.kinart.api.gestiondepaie.dto.SuspensionPaieDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("suspensionpaie")
public interface SuspensionPaieApi {

    @PostMapping(value = APP_ROOT_PAIE + "/suspensionpaie/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un dossier de paie", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = SuspensionPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<SuspensionPaieDto> save(@RequestBody SuspensionPaieDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/suspensionpaie/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un calul par ID", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            SuspensionPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<SuspensionPaieDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/suspensionpaie/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des éléments fixes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<SuspensionPaieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<SuspensionPaieDto>> findAll();

    @DeleteMapping(value = APP_ROOT_PAIE + "/suspensionpaie/delete")
    @ApiOperation(value = "Supprimer un dossier", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@RequestParam("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/suspensionpaie/filter2", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi le numéro de bulletin en cours", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<SuspensionPaieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le numérode bulletin de paie en cours")
    })
    ResponseEntity<List<SuspensionPaieDto>> findByMatricule(@RequestParam("matricule") String matricule);
}
