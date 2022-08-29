package com.kinart.api.evaluation.controller.api;

import com.kinart.api.evaluation.dto.ObjectifDto;
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

@Api("objectif")
public interface ObjectifApi {

    @PostMapping(value = APP_ROOT_PAIE + "/objectif/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer les objectifs", notes = "Cette methode permet d'enregistrer ou modifier les objectifs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    List<String> saveObjectifs(@RequestBody List<ObjectifDto> dtos);

    @GetMapping(value = APP_ROOT_PAIE + "/objectif/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des objectifs", notes = "Cette methode permet de chercher et renvoyer la liste des niveaus qui existent "
            + "dans la BDD", responseContainer = "List<ObjectifDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des objectifs / Une liste vide")
    })
    ResponseEntity<List<ObjectifDto>> findAll(@PathVariable("identreprise") Integer identreprise);
}
