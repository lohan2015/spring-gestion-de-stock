package com.cmbassi.gestiondepaie.controller.api;

import com.cmbassi.gestiondepaie.dto.ElementSalaireDto;
import com.cmbassi.gestiondepaie.dto.SalarieDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cmbassi.gestiondestock.utils.Constants.APP_ROOT_PAIE;

public interface ElementSalaireApi {

    @PostMapping(value = APP_ROOT_PAIE + "/elementsalaire/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un élément", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = ElementSalaireDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<ElementSalaireDto> save(@RequestBody ElementSalaireDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/elementsalaire/search/{idelement}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un salarié par ID", notes = "Cette methode permet de chercher un salarié par son ID", response = ElementSalaireDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'article a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec l'ID fourni")
    })
    ResponseEntity<ElementSalaireDto> findById(@PathVariable("idSalarie") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/elementsalaire/filter1/{code}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un élément par code", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            ElementSalaireDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<List<ElementSalaireDto>> findByCode(@PathVariable("code") String code);

    @GetMapping(value = APP_ROOT_PAIE + "/elementsalaire/filter2/{libelle}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un élément par libellé", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            ElementSalaireDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le libellé fourni")
    })
    ResponseEntity<List<ElementSalaireDto>> findByLibelle(@PathVariable("libelle") String libelle);

    @GetMapping(value = APP_ROOT_PAIE + "/elementsalaire/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des salariés", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<ElementSalaireDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des éléments / Une liste vide")
    })
    ResponseEntity<List<ElementSalaireDto>> findAll();

    @DeleteMapping(value = APP_ROOT_PAIE + "/elementsalaire/delete/{idelement}")
    @ApiOperation(value = "Supprimer un élément", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("idelement") Integer id);
}
