package com.cmbassi.gestiondepaie.controller.api;

import com.cmbassi.gestiondepaie.dto.ElementVariableCongeDto;
import com.cmbassi.gestiondepaie.dto.ElementVariableDetailMoisDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.cmbassi.gestiondestock.utils.Constants.APP_ROOT_PAIE;

public interface ElementVariableCongeApi {

    @PostMapping(value = APP_ROOT_PAIE + "/eltvarconge/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un EV congé", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = ElementVariableCongeDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<ElementVariableCongeDto> save(@RequestBody ElementVariableCongeDto dto, @PathVariable("dateFormat") String dateFormat, @PathVariable("typeBD") String typeBD);

    @GetMapping(value = APP_ROOT_PAIE + "/eltvarconge/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un EV congé par ID", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            ElementVariableCongeDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<ElementVariableCongeDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/eltvarconge/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des bulletins", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<ElementVariableCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<ElementVariableCongeDto>> findAll();

    @GetMapping(value = APP_ROOT_PAIE + "/eltvarconge/filter2", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des bulletins", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<ElementVariableCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<ElementVariableCongeDto>> findEVCongeByFilter(@PathVariable("matricule") Optional<String> matricule, @PathVariable("codemotif") Optional<String> codemotif);

    @DeleteMapping(value = APP_ROOT_PAIE + "/eltvarconge/delete/{id}")
    @ApiOperation(value = "Supprimer un dossier", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("id") Integer id);
}
