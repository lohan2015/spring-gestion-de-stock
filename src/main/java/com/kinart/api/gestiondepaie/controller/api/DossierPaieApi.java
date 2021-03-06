package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.DossierPaieDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("dossierpaie")
public interface DossierPaieApi {

    @PostMapping(value = APP_ROOT_PAIE + "/dossierpaie/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un dossier de paie", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = DossierPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DossierPaieDto> save(@RequestBody DossierPaieDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/dossierpaie/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un calul par ID", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            DossierPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<DossierPaieDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/dossierpaie/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des bulletins", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DossierPaieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<DossierPaieDto>> findAll();

    @DeleteMapping(value = APP_ROOT_PAIE + "/dossierpaie/delete/{id}")
    @ApiOperation(value = "Supprimer un dossier", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/dossierpaie/moispaie", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi le mois depaie en cours", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le mois de paie en cours")
    })
    String getMoisDePaieCourant(@RequestParam("idEntreprise") String idEntreprise);

    @GetMapping(value = APP_ROOT_PAIE + "/dossierpaie/numbul", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi le numéro de bulletin en cours", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le numérode bulletin de paie en cours")
    })
    String getNumeroBulletinPaie(@RequestParam("idEntreprise") String idEntreprise);
}
