package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.ParamEFCMRDto;
import com.kinart.api.gestiondepaie.dto.RechercheDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;

import static com.kinart.stock.business.utils.Constants.APP_ROOT;

@Api("etatsfiscauxcmr")
public interface EFCMRApi {

    @PostMapping(value=APP_ROOT + "/efcmr/declarationversement", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Générer déclaration des versements", notes = "Cette methode permet de générer déclaration des versements")
    ResponseEntity<Object> generateDeclVersement(@RequestBody ParamEFCMRDto dto, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException;

    @PostMapping(value=APP_ROOT + "/efcmr/redevaudiovisuelle", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Etat de redevance audio visuelle", notes = "Cette methode permet de générer l'état de redevance audio visuelle")
    ResponseEntity<Object> generateRedevAudioVisuelle(@RequestBody ParamEFCMRDto dto, HttpServletRequest request, HttpServletResponse response);

    @PostMapping(value=APP_ROOT + "/efcmr/contribcfc", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Etat contribution au CFC", notes = "Cette methode permet de générer l'état contribution au CFC")
    ResponseEntity<Object> generateContribCFC(@RequestBody ParamEFCMRDto dto, HttpServletRequest request, HttpServletResponse response);

    @PostMapping(value=APP_ROOT + "/efcmr/dipesmensuelles", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Etat dipes mensuelles", notes = "Cette methode permet de générer l'état dipes mensuelles")
    ResponseEntity<Object> generateDipesMensuelles(@RequestBody ParamEFCMRDto dto, HttpServletRequest request, HttpServletResponse response);
}
