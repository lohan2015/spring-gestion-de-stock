package com.kinart.api.organisation.controller.api;

import com.kinart.api.organisation.dto.PosteDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("poste")
public interface PosteApi {

    @PostMapping(value = APP_ROOT_PAIE + "/poste/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un poste", notes = "Cette methode permet d'enregistrer ou modifier un poste", response = PosteDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<PosteDto> save(@RequestBody PosteDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/poste/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un poste par ID", notes = "Cette methode permet de chercher un poste par son CODE", response =
            PosteDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<PosteDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/poste/allposte", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des postes", notes = "Cette methode permet de chercher et renvoyer la liste des postes qui existent "
            + "dans la BDD", responseContainer = "List<PosteDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<PosteDto>> findAllPoste(@RequestParam("codedossier") String codeDossier);

    @GetMapping(value = APP_ROOT_PAIE + "/poste/allmetier", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des postes", notes = "Cette methode permet de chercher et renvoyer la liste des postes qui existent "
            + "dans la BDD", responseContainer = "List<PosteDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<PosteDto>> findAllMetier(@RequestParam("codedossier") String codeDossier);

    @DeleteMapping(value = APP_ROOT_PAIE + "/poste/delete")
    @ApiOperation(value = "Supprimer un dossier", notes = "Cette methode permet de supprimer un poste par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@RequestParam("id") Integer id);
}
