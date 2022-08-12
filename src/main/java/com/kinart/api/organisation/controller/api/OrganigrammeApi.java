package com.kinart.api.organisation.controller.api;

import com.kinart.api.organisation.dto.OrganigrammeDto;
import com.kinart.api.organisation.dto.ParametreOrganigrammeDto;
import com.kinart.api.organisation.dto.RechercheListeOrganigrammeDto;
import com.kinart.api.organisation.dto.ResultatDessinOrganigrammeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("organigramme")
public interface OrganigrammeApi {

    @PostMapping(value = APP_ROOT_PAIE + "/organigramme/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un organigramme", notes = "Cette methode permet d'enregistrer ou modifier un organigramme", response = OrganigrammeDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<OrganigrammeDto> save(@RequestBody OrganigrammeDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/organigramme/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un organigramme par ID", notes = "Cette methode permet de chercher un organigramme par son CODE", response =
            OrganigrammeDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<OrganigrammeDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/organigramme/allorganigramme", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des organigrammex", notes = "Cette methode permet de chercher et renvoyer la liste des organigrammes qui existent "
            + "dans la BDD", responseContainer = "List<OrganigrammeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<OrganigrammeDto>> findAll(@RequestBody RechercheListeOrganigrammeDto search);

    @DeleteMapping(value = APP_ROOT_PAIE + "/organigramme/delete")
    @ApiOperation(value = "Supprimer un organigramme", notes = "Cette methode permet de supprimer un organigramme par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@RequestParam("codeorganigramme") String codeorganigramme);

    @PostMapping(value = APP_ROOT_PAIE + "/organigramme/dessin", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des organigrammex", notes = "Cette methode permet de chercher et renvoyer la liste des organigrammes qui existent "
            + "dans la BDD", responseContainer = "List<OrganigrammeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<ResultatDessinOrganigrammeDto> dessinerOrganigramme(@RequestBody ParametreOrganigrammeDto dto, HttpServletRequest request, HttpServletResponse response);
}
