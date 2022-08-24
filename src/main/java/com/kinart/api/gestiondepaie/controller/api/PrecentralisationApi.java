package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.RechercheDto;
import com.kinart.api.gestiondepaie.dto.ResultatPrecentralisationDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("precentralisation")
public interface PrecentralisationApi {

    @PostMapping(value = APP_ROOT_PAIE + "/precentralisation", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Lancer la précentralisation", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = ResultatPrecentralisationDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResultatPrecentralisationDto lancerPrecentralisation(@RequestBody RechercheDto dto, HttpServletRequest request);
}
