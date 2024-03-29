package com.kinart.api.organisation.controller.api;

import com.kinart.api.organisation.dto.*;
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

    @PostMapping(value = APP_ROOT_PAIE + "/organigramme/allorganigramme", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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

    @PostMapping(value = APP_ROOT_PAIE + "/organigramme/extract", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des organigrammex", notes = "Cette methode permet de chercher et renvoyer la liste des organigrammes qui existent "
            + "dans la BDD")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<Object> extractionOrganigramme(@RequestBody ParametreOrganigrammeDto dto, HttpServletRequest request, HttpServletResponse response);


    @GetMapping(value = APP_ROOT_PAIE + "/organigramme/existcellule", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Vérifier l'existence d'une cellule", notes = "Cette methode permet de chercher et renvoyer la liste des organigrammes qui existent "
            + "dans la BDD")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    boolean existCellule(@RequestParam("codeorganigramme") String codeorganigramme);

    @GetMapping(value = APP_ROOT_PAIE + "/organigramme/possibilite", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Lecture des possibilités de création d'une cellule", notes = "Cette methode permet de chercher et renvoyer la liste des organigrammes qui existent "
            + "dans la BDD")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    String getPossibilites(@RequestParam("codepere")  String codepere);

    @PostMapping(value = APP_ROOT_PAIE + "/organigramme/ctrlaffectposte", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Contrôel affectation un poste à la cellule de l'organigramme", notes = "Cette methode permet de chercher et renvoyer la liste des organigrammes qui existent "
            + "dans la BDD", responseContainer = "List<String>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    List<String> controleAffectationPoste(@RequestBody OperationOrganigrammeDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/organigramme/affectposte", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Affecter un poste à la cellule de l'organigramme", notes = "Cette methode permet de chercher et renvoyer la liste des organigrammes qui existent "
            + "dans la BDD", responseContainer = "List<String>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    List<String> affectationPosteOrganigramme(@RequestBody OperationOrganigrammeDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/organigramme/ctrlaffectsalarie", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Contrôler affectation salarié au poste", notes = "Cette methode permet de chercher et renvoyer la liste des organigrammes qui existent "
            + "dans la BDD", responseContainer = "List<String>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    List<String> controleAffectationSalarie(@RequestBody OperationOrganigrammeDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/organigramme/affectsalarie", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Affecter salarié au poste", notes = "Cette methode permet de chercher et renvoyer la liste des organigrammes qui existent "
            + "dans la BDD", responseContainer = "List<String>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    List<String> affectationSalariePoste(@RequestBody OperationOrganigrammeDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/organigramme/rattachcellule", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rattacher une cellule à une autre", notes = "Cette methode permet de chercher et renvoyer la liste des organigrammes qui existent "
            + "dans la BDD", responseContainer = "List<String>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    List<String> rattacherCellules(@RequestBody OperationOrganigrammeDto dto);

}
