package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.SalarieDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("salaire")
public interface SalarieApi {

    @PostMapping(value = APP_ROOT_PAIE + "/salaries/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un salarié", notes = "Cette methode permet d'enregistrer ou modifier un salarié", response = SalarieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le salarié cree / modifie"),
            @ApiResponse(code = 400, message = "Le salarié n'est pas valide")
    })
    ResponseEntity<SalarieDto> save(@RequestBody SalarieDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/salaries/{idSalarie}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un salarié par ID", notes = "Cette methode permet de chercher un salarié par son ID", response = SalarieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'article a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec l'ID fourni")
    })
    ResponseEntity<SalarieDto> findById(@PathVariable("idSalarie") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/salaries/filter1/{nmat}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un salarié par matricule", notes = "Cette methode permet de chercher un salarié par son CODE")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le salarié a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<List<SalarieDto>> findByMatricule(@PathVariable("nmat") String nmat);

    @GetMapping(value = APP_ROOT_PAIE + "/salaries/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des salariés", notes = "Cette methode permet de chercher et renvoyer la liste des salariés qui existent "
            + "dans la BDD", responseContainer = "List<SalarieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des salariés / Une liste vide")
    })
    ResponseEntity<List<SalarieDto>> findAll();

    @GetMapping(value = APP_ROOT_PAIE + "/salaries/filter2/{nom}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un salarié par matricule", notes = "Cette methode permet de chercher un salarié par son CODE", responseContainer = "List<SalarieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le salarié a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec le nom ou prénom fourni")
    })
    ResponseEntity<List<SalarieDto>> findByName(@PathVariable("nom") String nom);

    @GetMapping(value = APP_ROOT_PAIE + "/salaries/filter3/inactif/{nmat}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un salarié par matricule", notes = "Cette methode permet de chercher un salarié par son CODE", responseContainer = "List<SalarieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le salarié a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<List<SalarieDto>> findByMatriculeInactif(@PathVariable("nmat") String nmat);

    @DeleteMapping(value = APP_ROOT_PAIE + "/salaries/delete/{idSalarie}")
    @ApiOperation(value = "Supprimer un salarié", notes = "Cette methode permet de supprimer un salarié par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le salarié a ete supprime")
    })
    void delete(@PathVariable("idSalarie") Integer id);
}
