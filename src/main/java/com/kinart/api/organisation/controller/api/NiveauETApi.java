package com.kinart.api.organisation.controller.api;

import com.kinart.api.organisation.dto.NiveauemploitypeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("niveauet")
public interface NiveauETApi {

    @PostMapping(value = APP_ROOT_PAIE + "/niveauet/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un niveauet", notes = "Cette methode permet d'enregistrer ou modifier un niveauet", response = NiveauemploitypeDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<NiveauemploitypeDto> save(@RequestBody NiveauemploitypeDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/niveauet/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un niveauet par ID", notes = "Cette methode permet de chercher un niveauet par son CODE", response =
            NiveauemploitypeDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<NiveauemploitypeDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/niveauet/allniveauet", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des niveaux", notes = "Cette methode permet de chercher et renvoyer la liste des niveauets qui existent "
            + "dans la BDD", responseContainer = "List<NiveauemploitypeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<NiveauemploitypeDto>> findAll(@RequestParam("codedossier") String codeDossier);

    @DeleteMapping(value = APP_ROOT_PAIE + "/niveauet/delete")
    @ApiOperation(value = "Supprimer un niveauet", notes = "Cette methode permet de supprimer un niveauet par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@RequestParam("id") Integer id);
}
