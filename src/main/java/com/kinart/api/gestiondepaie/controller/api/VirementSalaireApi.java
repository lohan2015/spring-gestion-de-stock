package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.VirementSalarieDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("virementsalaire")
public interface VirementSalaireApi {

    @PostMapping(value = APP_ROOT_PAIE + "/virementsalaire/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un compte de virement", notes = "Cette methode permet d'enregistrer ou modifier un compte de virement", response = VirementSalarieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<VirementSalarieDto> save(@RequestBody VirementSalarieDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/virementsalaire/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un compte de virement par ID", notes = "Cette methode permet de chercher un compte de virement par son CODE", response =
            VirementSalarieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<VirementSalarieDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/virementsalaire/filter2/{matricule}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un compte de virement par ID", notes = "Cette methode permet de chercher un compte de virement par son CODE", response =
            VirementSalarieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<VirementSalarieDto> findByMatricule(@PathVariable("matricule") String matricule);

    @GetMapping(value = APP_ROOT_PAIE + "/virementsalaire/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des calendrier", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<VirementSalarieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<VirementSalarieDto>> findAll();

    @DeleteMapping(value = APP_ROOT_PAIE + "/virementsalaire/delete/{id}")
    @ApiOperation(value = "Supprimer un dossier", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("id") Integer id);
}
