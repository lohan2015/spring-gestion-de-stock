package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.HistoConnexionDto;
import com.kinart.api.gestiondepaie.dto.RechercheTraceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("histoconnexion")
public interface HistoConnexionApi {

    @PostMapping(value = APP_ROOT_PAIE + "/histoconnexion/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un compte de virement", notes = "Cette methode permet d'enregistrer ou modifier un compte de virement", response = HistoConnexionDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<HistoConnexionDto> save(@RequestBody HistoConnexionDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/histoconnexion/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un compte de virement par ID", notes = "Cette methode permet de chercher un compte de virement par son CODE", response =
            HistoConnexionDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<HistoConnexionDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/histoconnexion/filter2/{user}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un compte de virement par ID", notes = "Cette methode permet de chercher un compte de virement par son CODE")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<List<HistoConnexionDto>> findByUser(@PathVariable("user") String user);

    @GetMapping(value = APP_ROOT_PAIE + "/histoconnexion/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des calendrier", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<LogMessageDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<HistoConnexionDto>> findAll();

    @DeleteMapping(value = APP_ROOT_PAIE + "/histoconnexion/delete/{id}")
    @ApiOperation(value = "Supprimer un dossier", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("id") Integer id);

    @PostMapping(value = APP_ROOT_PAIE + "/histoconnexion/search1", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un compte de virement", notes = "Cette methode permet d'enregistrer ou modifier un compte de virement")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<List<HistoConnexionDto>> findByUserAndOperation(@RequestBody RechercheTraceDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/histoconnexion/search2", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un compte de virement", notes = "Cette methode permet d'enregistrer ou modifier un compte de virement")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<List<HistoConnexionDto>> findByUserAndNoOperation(@RequestBody RechercheTraceDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/histoconnexion/search3", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un compte de virement", notes = "Cette methode permet d'enregistrer ou modifier un compte de virement")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<List<HistoConnexionDto>> findByOperation(@RequestBody RechercheTraceDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/histoconnexion/search4", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un compte de virement", notes = "Cette methode permet d'enregistrer ou modifier un compte de virement")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<List<HistoConnexionDto>> findByNoOperation(@RequestBody RechercheTraceDto dto);
}
