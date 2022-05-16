package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.ElementVariableDetailMoisDto;
import com.kinart.api.gestiondepaie.dto.RechercheDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("elementvariable")
public interface ElementVariableApi {

    @PostMapping(value = APP_ROOT_PAIE + "/eltvar/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un EV", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = ElementVariableDetailMoisDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<ElementVariableDetailMoisDto> save(@RequestBody ElementVariableDetailMoisDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/eltvar/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un EV par ID", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            ElementVariableDetailMoisDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<ElementVariableDetailMoisDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/eltvar/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des bulletins", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<ElementVariableDetailMoisDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<ElementVariableDetailMoisDto>> findAll();

    @PostMapping(value = APP_ROOT_PAIE + "/eltvar/filter2", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des bulletins", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<ElementVariableDetailMoisDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<ElementVariableDetailMoisDto>> findEVByFilter(@RequestBody RechercheDto dto);

    @DeleteMapping(value = APP_ROOT_PAIE + "/eltvar/delete/{id}")
    @ApiOperation(value = "Supprimer un dossier", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("id") Integer id);
}
