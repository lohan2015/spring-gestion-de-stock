package com.cmbassi.gestiondepaie.controller.api;

import com.cmbassi.gestiondepaie.dto.CalculPaieDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cmbassi.gestiondestock.utils.Constants.APP_ROOT_PAIE;

public interface CalculPaieApi {

    @PostMapping(value = APP_ROOT_PAIE + "/calcul/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un calcul", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = CalculPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<CalculPaieDto> save(@RequestBody CalculPaieDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/calcul/salarie/{matricule}/{periode}/{numeBul}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un calcul", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = CalculPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    boolean calculPaieSalarie(@PathVariable("matricule") String matricule, @PathVariable("periode") String periode, @PathVariable("numeBul") Integer numeBul);

    @GetMapping(value = APP_ROOT_PAIE + "/calcul/search/{matricule}/{periode}/{numeBul}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un calul par matricule", notes = "Cette methode permet de chercher un calcul par matricule", response = CalculPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'article a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec l'ID fourni")
    })
    ResponseEntity<List<CalculPaieDto>> findByMatriculeAndPeriod(@PathVariable("matricule") String matricule, String periode, Integer numeBul);

    @GetMapping(value = APP_ROOT_PAIE + "/calcul/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un calul par ID", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            CalculPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<CalculPaieDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/calcul/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des bulletins", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<CalculPaieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des bulletins / Une liste vide")
    })
    ResponseEntity<List<CalculPaieDto>> findAll();

    @DeleteMapping(value = APP_ROOT_PAIE + "/calcul/delete/{id}")
    @ApiOperation(value = "Supprimer un élément", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("id") Integer id);
}
