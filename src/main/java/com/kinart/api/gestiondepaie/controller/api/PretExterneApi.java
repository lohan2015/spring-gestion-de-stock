package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.LigneEcheancierDto;
import com.kinart.api.gestiondepaie.dto.PretExterneEnteteDto;
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

@Api("pretexterne")
public interface PretExterneApi {

    @PostMapping(value = APP_ROOT_PAIE + "/pretexterne/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un pret", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = PretExterneEnteteDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<PretExterneEnteteDto> save(@RequestBody PretExterneEnteteDto dto, @RequestBody List<LigneEcheancierDto> echeancier);

    @GetMapping(value = APP_ROOT_PAIE + "/pretexterne/filter", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des prêts", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<PretExterneEnteteDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<PretExterneEnteteDto>> findPretEntetePretByFilter(@PathVariable("matricule") Optional<String> matricule, @PathVariable("nprt") Optional<String> nprt, @PathVariable("crub") Optional<String> crub);

    @GetMapping(value = APP_ROOT_PAIE + "/pretexterne/generateecheancier", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste de l'échéancier généré", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<LigneEcheancierDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des échéances de prêt / Une liste vide")
    })
    ResponseEntity<List<LigneEcheancierDto>> generateEcheancier(@RequestBody PretExterneEnteteDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/pretexterne/loadecheancier", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste de l'échéancier de la BD", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<LigneEcheancierDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des échéances de prêt / Une liste vide")
    })
    ResponseEntity<List<LigneEcheancierDto>> loadEchancier(@PathVariable("numPret") String numPret);
}
