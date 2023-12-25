package com.kinart.api.portail.controller;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.portail.dto.DemandePretDto;
import com.kinart.api.portail.dto.DemandePretResponse;
import com.kinart.api.portail.dto.EnqSalarieEnteteDto;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.portail.business.model.DemandePret;
import com.kinart.portail.business.repository.*;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import com.kinart.stock.business.model.Utilisateur;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PORTAIL;

@Api("enquete")
@RestController
@RequiredArgsConstructor
public class EnqueteController {

    private final EnqSalarieEnteteRepository enqSalarieEnteteRepository;
    private final EnqBienEtreRepository enqBienEtreRepository;
    private final EnqBilanObjRepository enqBilanObjRepository;
    private final EnqBilanActRepository enqBilanActRepository;
    private final EnqBilanCompPosteRepository enqBilanCompPosteRepository;

    @PostMapping(value = APP_ROOT_PORTAIL + "/enquete/entete", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte entête enquête", notes = "Cette methode permet d'enregistrer des enquêtes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<EnqSalarieEnteteDto> saveEntete(@RequestBody EnqSalarieEnteteDto dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {


        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/enquete/bienetre", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte bien être", notes = "Cette methode permet d'enregistrer des enquêtes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<EnqSalarieEnteteDto> saveBienEtre(@RequestBody EnqSalarieEnteteDto dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {


        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/{matricule}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<EnqSalarieEnteteDto> findBySalarieAndYear(
          @PathVariable("matricule") String matricule,
          @RequestParam("year") String year
    ){
        EnqSalarieEnteteDto result = null;

        if(result!=null) {
            return ResponseEntity.ok(result);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

}
