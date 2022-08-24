package com.kinart.api.gestiondepaie.controller.api;

import com.kinart.api.gestiondepaie.dto.CalculPaieDto;
import com.kinart.api.gestiondepaie.dto.CumulPaieDto;
import com.kinart.api.gestiondepaie.dto.RechercheDto;
import com.kinart.api.gestiondepaie.dto.SalarieDto;
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

import static com.kinart.stock.business.utils.Constants.APP_ROOT;
import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("calculpaie")
public interface CalculPaieApi {

    @PostMapping(value = APP_ROOT_PAIE + "/calcul/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un calcul", notes = "Cette methode permet d'enregistrer ou modifier un élément", response = CalculPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<CalculPaieDto> save(@RequestBody CalculPaieDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/calcul/salarie/calcul", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un calcul", notes = "Cette methode permet d'enregistrer ou modifier un élément")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    boolean calculPaieSalarie(@RequestBody RechercheDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/calcul/search/{matricule}/{periode}/{numeBul}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un calul par matricule", notes = "Cette methode permet de chercher un calcul par matricule", response = CalculPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'article a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec l'ID fourni")
    })
    ResponseEntity<List<CalculPaieDto>> findByMatriculeAndPeriod(@PathVariable("matricule") String matricule, @PathVariable("periode") String periode, @PathVariable("numeBul") Integer numeBul);

    @GetMapping(value = APP_ROOT_PAIE + "/calcul/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un calul par ID", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            CalculPaieDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<CalculPaieDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/calcul/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des bulletins", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<CalculPaieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des bulletins / Une liste vide")
    })
    ResponseEntity<List<CalculPaieDto>> findAll();

    @DeleteMapping(value = APP_ROOT_PAIE + "/calcul/delete/{id}")
    @ApiOperation(value = "Supprimer un élément", notes = "Cette methode permet de supprimer un élément par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("id") Integer id);

    @PostMapping(value = APP_ROOT_PAIE + "/calcul/bulletin", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Consulter un calcul", notes = "Cette methode permet d'enregistrer ou modifier un élément")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<List<CalculPaieDto>> findResultCalculByFilter(@RequestBody RechercheDto dto);

    @PostMapping(value=APP_ROOT + "/calcul/imprimer", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Générer édition bulletin", notes = "Cette methode permet de générer édition bulletin")
    ResponseEntity<Object> getReport(@RequestBody RechercheDto dto, HttpServletRequest request, HttpServletResponse response);

    @PostMapping(value = APP_ROOT_PAIE + "/calcul/salaries", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Charger liste salarié", notes = "Cette methode permet de renvoyer la liste des salariés à calculer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<List<SalarieDto>> findListeSalarieByFilter(@RequestBody RechercheDto dto);

    @PostMapping(value=APP_ROOT + "/calcul/cloture", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Clôture de la paie", notes = "Cette methode permet de clôturer la paie")
    ResponseEntity<Object> cloturerPaie(@RequestBody RechercheDto dto, HttpServletRequest request, HttpServletResponse response);

    @PostMapping(value = APP_ROOT_PAIE + "/calcul/historique", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Consulter un cumul", notes = "Cette methode permet de consulter un bulletin clôturé", responseContainer = "List<CumulPaieDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<List<CumulPaieDto>> findCumulByFilter(@RequestBody RechercheDto dto);

    @PostMapping(value=APP_ROOT + "/mvtcptable/imprimer", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Générer mouvement comptable", notes = "Cette methode permet de générer mouvement comptable")
    ResponseEntity<Object> getReportMvtCpte(@RequestBody RechercheDto dto, HttpServletRequest request, HttpServletResponse response);

}
