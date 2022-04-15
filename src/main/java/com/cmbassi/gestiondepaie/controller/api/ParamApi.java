package com.cmbassi.gestiondepaie.controller.api;

import com.cmbassi.gestiondepaie.dto.ParamColumnDto;
import com.cmbassi.gestiondepaie.dto.ParamDataDto;
import com.cmbassi.gestiondepaie.dto.ParamTableDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cmbassi.gestiondestock.utils.Constants.APP_ROOT_PAIE;

public interface ParamApi {

    @PostMapping(value = APP_ROOT_PAIE + "/parametrage/createtable", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer une table de paramétrage", notes = "Cette methode permet d'enregistrer ou modifier une table de paramétrage", response = ParamTableDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La table cree / modifie"),
            @ApiResponse(code = 400, message = "La table n'est pas valide")
    })
    ResponseEntity<ParamTableDto> save(@RequestBody ParamTableDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/parametrage/colonne", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer une colonne de paramétrage", notes = "Cette methode permet d'enregistrer ou modifier une colonne", response = ParamColumnDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La colonne cree / modifie"),
            @ApiResponse(code = 400, message = "La colonne n'est pas valide")
    })
    ResponseEntity<ParamColumnDto> save(@RequestBody ParamColumnDto dto);

    @PostMapping(value = APP_ROOT_PAIE + "/parametrage/donnee", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer une donnée de paramétrage", notes = "Cette methode permet d'enregistrer ou modifier une donnée", response = ParamDataDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La donnée cree / modifie"),
            @ApiResponse(code = 400, message = "La donnée n'est pas valide")
    })
    ResponseEntity<ParamDataDto> save(@RequestBody ParamDataDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/parametrage/colonne/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher une colonne par ID", notes = "Cette methode permet de chercher une colonne par son ID", response = ParamColumnDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La colonne a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucune colonne n'existe dans la BDD avec l'ID fourni")
    })
    ResponseEntity<ParamColumnDto> findColumById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/parametrage/table/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher une table par ID", notes = "Cette methode permet de chercher une table par son ID", response = ParamTableDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La table a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucune table n'existe dans la BDD avec l'ID fourni")
    })
    ResponseEntity<ParamTableDto> findTableById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/parametrage/donnee/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher une donnée par ID", notes = "Cette methode permet de chercher une donnée par son ID", response = ParamDataDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La donnée a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucune donnée n'existe dans la BDD avec l'ID fourni")
    })
    ResponseEntity<ParamDataDto> findDataById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/parametrage/donnee/filter1/{ctab}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un salarié par matricule", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            ParamDataDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le salarié a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<List<ParamDataDto>> findDataByCodeTable(@PathVariable("ctab") Integer ctab);

    @GetMapping(value = APP_ROOT_PAIE + "/parametrage/donnee/filter2/{ctab}/{cacc}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un salarié par matricule", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            ParamDataDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le salarié a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<List<ParamDataDto>> findDataByCle(@PathVariable("ctab") Integer ctab, @PathVariable("cacc") String cacc);

    @GetMapping(value = APP_ROOT_PAIE + "/parametrage/donnee/filter3/{ctab}/{cacc}/{nume}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un salarié par matricule", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            ParamDataDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le salarié a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<ParamDataDto> findDataByNumeroLigne(@PathVariable("ctab") Integer ctab, @PathVariable("cacc") String cacc, @PathVariable("nume") Integer nume);

    @GetMapping(value = APP_ROOT_PAIE + "/parametrage/colonne/filter/{ctab}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher une colonne par numéro de table", notes = "Cette methode permet de chercher un salarié par son CODE", response =
            ParamColumnDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le salarié a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun salarié n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<List<ParamColumnDto>> findColumnByCodeTable(@PathVariable("ctab") Integer ctab);

    @GetMapping(value = APP_ROOT_PAIE + "/colonne/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des salariés", notes = "Cette methode permet de chercher et renvoyer la liste des salariés qui existent "
            + "dans la BDD", responseContainer = "List<ParamColumnDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des colonnes / Une liste vide")
    })
    ResponseEntity<List<ParamColumnDto>> findColumnAll();

    @GetMapping(value = APP_ROOT_PAIE + "/donnee/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des données", notes = "Cette methode permet de chercher et renvoyer la liste des salariés qui existent "
            + "dans la BDD", responseContainer = "List<ParamDataDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des colonnes / Une liste vide")
    })
    ResponseEntity<List<ParamDataDto>> findDataAll();

    @GetMapping(value = APP_ROOT_PAIE + "/table/all", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des colonnes", notes = "Cette methode permet de chercher et renvoyer la liste des salariés qui existent "
            + "dans la BDD", responseContainer = "List<ParamTableDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des tables / Une liste vide")
    })
    ResponseEntity<List<ParamTableDto>> findTableAll();

    @DeleteMapping(value = APP_ROOT_PAIE + "/salaries/deletecolonne/{id}")
    @ApiOperation(value = "Supprimer un salarié", notes = "Cette methode permet de supprimer un salarié par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La colonne a ete supprime")
    })
    void deleteColumn(@PathVariable("id") Integer id);

    @DeleteMapping(value = APP_ROOT_PAIE + "/salaries/deletetable/{id}")
    @ApiOperation(value = "Supprimer une table", notes = "Cette methode permet de supprimer un salarié par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La table a ete supprime")
    })
    void deleteTable(@PathVariable("id") Integer id);

    @DeleteMapping(value = APP_ROOT_PAIE + "/salaries/deletedonnees/{id}")
    @ApiOperation(value = "Supprimer une donnée", notes = "Cette methode permet de supprimer un salarié par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La donnée a ete supprime")
    })
    void deleteData(@PathVariable("id") Integer id);
}
