package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.ElementSalaireDto;
import com.kinart.api.gestiondepaie.dto.PretInterneDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("pretinterne")
public interface PretInterneApi {

    @PostMapping(value = APP_ROOT_PAIE + "/pretinterne/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un prêt interne ", notes = "Cette methode permet d'enregistrer ou modifier un compte de virement", response = PretInterneDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<PretInterneDto> save(@RequestBody PretInterneDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/pretinterne/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un prêt interne par ID", notes = "Cette methode permet de chercher un prêt interne par son CODE", response =
            PretInterneDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<PretInterneDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/pretinterne/filter2/{matricule}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher les prêts internes par matricule", notes = "Cette methode permet de chercher les prêts internes par matricule", responseContainer = "List<PretInterneDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<List<PretInterneDto>> findByMatricule(@PathVariable("matricule") String matricule);

    @DeleteMapping(value = APP_ROOT_PAIE + "/pretinterne/delete")
    @ApiOperation(value = "Supprimer un dossier", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@RequestParam("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/pretinterne/typepretinterne", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher les types de prêts internes par matricule", notes = "Cette methode permet de chercher les prêts internes par matricule", responseContainer = "List<ElementSalaireDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<List<ElementSalaireDto>> findRubriqueForPretInterne();
}
