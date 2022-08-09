package com.kinart.api.organisation.controller.api;

import com.kinart.api.organisation.dto.NiveauDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("niveau")
public interface NiveauApi {

    @PostMapping(value = APP_ROOT_PAIE + "/niveau/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un niveau", notes = "Cette methode permet d'enregistrer ou modifier un niveau", response = NiveauDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<NiveauDto> save(@RequestBody NiveauDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/niveau/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un niveau par ID", notes = "Cette methode permet de chercher un niveau par son CODE", response =
            NiveauDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<NiveauDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/niveau/allniveau", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des niveaux", notes = "Cette methode permet de chercher et renvoyer la liste des niveaus qui existent "
            + "dans la BDD", responseContainer = "List<NiveauDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<NiveauDto>> findAll(@RequestParam("codedossier") String codeDossier);

    @DeleteMapping(value = APP_ROOT_PAIE + "/niveau/delete")
    @ApiOperation(value = "Supprimer un niveau", notes = "Cette methode permet de supprimer un niveau par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@RequestParam("id") Integer id);
}
