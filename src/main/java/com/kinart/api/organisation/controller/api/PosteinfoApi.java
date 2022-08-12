package com.kinart.api.organisation.controller.api;

import com.kinart.api.organisation.dto.NiveauemploitypeDto;
import com.kinart.api.organisation.dto.PosteinfoDto;
import com.kinart.api.organisation.dto.RechercheCompetenceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;

@Api("posteinfo")
public interface PosteinfoApi {

    @PostMapping(value = APP_ROOT_PAIE + "/posteinfo/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Enregistrer un posteinfo", notes = "Cette methode permet d'enregistrer ou modifier un posteinfo", response = PosteinfoDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<PosteinfoDto> save(@RequestBody PosteinfoDto dto);

    @GetMapping(value = APP_ROOT_PAIE + "/posteinfo/filter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Rechercher un posteinfo par ID", notes = "Cette methode permet de chercher un posteinfo par son CODE", response =
            PosteinfoDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete trouve dans la BDD"),
            @ApiResponse(code = 404, message = "Aucun élément n'existe dans la BDD avec le CODE fourni")
    })
    ResponseEntity<PosteinfoDto> findById(@PathVariable("id") Integer id);

    @GetMapping(value = APP_ROOT_PAIE + "/posteinfo/allposteinfo", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des niveaux", notes = "Cette methode permet de chercher et renvoyer la liste des posteinfos qui existent "
            + "dans la BDD", responseContainer = "List<PosteinfoDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des dossiers de paie / Une liste vide")
    })
    ResponseEntity<List<PosteinfoDto>> findAll(@RequestParam("codedossier") String codeDossier);

    @GetMapping(value = APP_ROOT_PAIE + "/posteinfo/posteinfobytype", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des niveaux", notes = "Cette methode permet de chercher et renvoyer la liste des posteinfos qui existent "
            + "dans la BDD", responseContainer = "List<PosteinfoDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des compétences / Une liste vide")
    })
    ResponseEntity<List<PosteinfoDto>> findSkilByType(@RequestBody RechercheCompetenceDto dto, HttpServletRequest request);

    @DeleteMapping(value = APP_ROOT_PAIE + "/posteinfo/delete")
    @ApiOperation(value = "Supprimer un posteinfo", notes = "Cette methode permet de supprimer un posteinfo par ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@RequestParam("id") Integer id);
}
