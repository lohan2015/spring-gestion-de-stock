package com.kinart.api.portail.controller;

import com.kinart.api.portail.dto.DemandePretDto;
import com.kinart.api.portail.dto.EnqSalarieEnteteDto;
import com.kinart.portail.business.model.*;
import com.kinart.portail.business.repository.*;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final EnqBilanCompAttRepository enqBilanCompAttRepository;
    private final EnqFormSuivieRepository enqFormSuivieRepository;
    private final EnqFormSouhaitRepository enqFormSouhaitRepository;
    private final EnqConditionRepository enqConditionRepository;

    @PostMapping(value = APP_ROOT_PORTAIL + "/enquete/entete", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte entête enquête", notes = "Cette methode permet d'enregistrer des enquêtes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<EnqSalarieEntete> saveEntete(@RequestBody EnqSalarieEntete dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
            //EnqSalarieEntete entity = EnqSalarieEnteteDto.toEntity(dto);
            enqSalarieEnteteRepository.save(dto);
            //dto.setId(entity.getId());
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
    ResponseEntity<EnqBienEtre> saveBienEtre(@RequestBody EnqBienEtre dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
                    if(dto.getId()==null || dto.getId()==0)
                      enqBienEtreRepository.save(dto);
                    else {
                        Optional<EnqBienEtre> oldEntityOpt = enqBienEtreRepository.findById(dto.getId());
                        if(oldEntityOpt.isPresent()){
                            EnqBienEtre oldEntity = oldEntityOpt.get();
                            BeanUtils.copyProperties(dto, oldEntity, "id");
                            enqBienEtreRepository.save(oldEntity);
                        }
                    }

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/enquete/bilanobjectif", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte bien être", notes = "Cette methode permet d'enregistrer des enquêtes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<EnqBilanObj> saveBilanObjectif(@RequestBody EnqBilanObj dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
                if(dto.getId()==null || dto.getId()==0)
                    enqBilanObjRepository.save(dto);
                else {
                    Optional<EnqBilanObj> oldEntityOpt = enqBilanObjRepository.findById(dto.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqBilanObj oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(dto, oldEntity, "id");
                        enqBilanObjRepository.save(oldEntity);
                    }
                }


        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/enquete/bilanactivite", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte bien être", notes = "Cette methode permet d'enregistrer des enquêtes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<EnqBilanAct> saveBilanActivite(@RequestBody EnqBilanAct dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {

                if(dto.getId()==null || dto.getId()==0)
                    enqBilanActRepository.save(dto);
                else {
                    Optional<EnqBilanAct> oldEntityOpt = enqBilanActRepository.findById(dto.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqBilanAct oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(dto, oldEntity, "id");
                        enqBilanActRepository.save(oldEntity);
                    }
                }


        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/enquete/bilancompposte", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte bien être", notes = "Cette methode permet d'enregistrer des enquêtes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<EnqBilanCompPoste> saveBilanCompPoste(@RequestBody EnqBilanCompPoste dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {

                if(dto.getId()==null || dto.getId()==0)
                    enqBilanCompPosteRepository.save(dto);
                else {
                    Optional<EnqBilanCompPoste> oldEntityOpt = enqBilanCompPosteRepository.findById(dto.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqBilanCompPoste oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(dto, oldEntity, "id");
                        enqBilanCompPosteRepository.save(oldEntity);
                    }
                }


        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/enquete/bilancompatt", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte bien être", notes = "Cette methode permet d'enregistrer des enquêtes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<EnqBilanCompAtt> saveBilanCompAttendu(@RequestBody EnqBilanCompAtt dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {

                if(dto.getId()==null || dto.getId()==0)
                    enqBilanCompAttRepository.save(dto);
                else {
                    Optional<EnqBilanCompAtt> oldEntityOpt = enqBilanCompAttRepository.findById(dto.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqBilanCompAtt oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(dto, oldEntity, "id");
                        enqBilanCompAttRepository.save(oldEntity);
                    }
                }


        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/enquete/formsuivie", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte bien être", notes = "Cette methode permet d'enregistrer des enquêtes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<EnqFormSuivie> saveFormationSuivie(@RequestBody EnqFormSuivie dto){
        //validator.validate(dto);
        System.out.println("SAVE FORMATION SUIVIE........................");
        try {

                //if(dto.getId()==null || dto.getId()==0)
                    enqFormSuivieRepository.save(dto);
                /*else {
                    Optional<EnqFormSuivie> oldEntityOpt = enqFormSuivieRepository.findById(dto.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqFormSuivie oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(dto, oldEntity, "id");
                        enqFormSuivieRepository.save(oldEntity);
                    }
                }*/


        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/enquete/formsouhaitee", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte bien être", notes = "Cette methode permet d'enregistrer des enquêtes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<EnqFormSouhait> saveFormationSouhaitee(@RequestBody EnqFormSouhait dto){
        //validator.validate(dto);
        System.out.println("SAVE FORMATION SOUHAITEE........................");
        try {

                //if(dto.getId()==null || dto.getId()==0)
                    enqFormSouhaitRepository.save(dto);
                /*else {
                    Optional<EnqFormSouhait> oldEntityOpt = enqFormSouhaitRepository.findById(dto.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqFormSouhait oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(dto, oldEntity, "id");
                        enqFormSouhaitRepository.save(oldEntity);
                    }
                }*/


        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/enquete/condition", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte bien être", notes = "Cette methode permet d'enregistrer des enquêtes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<EnqCondition> saveCondition(@RequestBody EnqCondition dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {

                if(dto.getId()==null || dto.getId()==0)
                    enqConditionRepository.save(dto);
                else {
                    Optional<EnqCondition> oldEntityOpt = enqConditionRepository.findById(dto.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqCondition oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(dto, oldEntity, "id");
                        enqConditionRepository.save(oldEntity);
                    }
                }


        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/{matricule}", produces = {MediaType.ALL_VALUE, MediaType.ALL_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<EnqSalarieEnteteDto> findBySalarieAndYear(
          @PathVariable("matricule") String matricule,
          @RequestParam("year") String year
    ){
        EnqSalarieEnteteDto result = new EnqSalarieEnteteDto();
        Optional<EnqSalarieEntete> entity = enqSalarieEnteteRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(entity.isPresent())
            BeanUtils.copyProperties(entity.get(), result);
        else {
            result.setAnnee(Integer.parseInt(year));
            result.setNmat(matricule);
        }

        if(result!=null) {
            return ResponseEntity.ok(result);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @DeleteMapping(value = APP_ROOT_PORTAIL + "/enquete/{matricule}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Supprimer une demande pas encore validée", notes = "Cette methode permet de supprimer une demande pas encore validée")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    ResponseEntity<Void> delete(@PathVariable("enqid") Integer enqid) throws Exception {
        Optional<EnqSalarieEntete> entite = enqSalarieEnteteRepository.findById(enqid);

        if(entite.isPresent()) {
            enqConditionRepository.deleteByNmatAndAnnee(entite.get().getNmat(), entite.get().getAnnee());
            enqFormSouhaitRepository.deleteByNmatAndAnnee(entite.get().getNmat(), entite.get().getAnnee());
            enqFormSuivieRepository.deleteByNmatAndAnnee(entite.get().getNmat(), entite.get().getAnnee());
            enqBilanCompAttRepository.deleteByNmatAndAnnee(entite.get().getNmat(), entite.get().getAnnee());
            enqBilanCompPosteRepository.deleteByNmatAndAnnee(entite.get().getNmat(), entite.get().getAnnee());
            enqBilanActRepository.deleteByNmatAndAnnee(entite.get().getNmat(), entite.get().getAnnee());
            enqBilanObjRepository.deleteByNmatAndAnnee(entite.get().getNmat(), entite.get().getAnnee());
            enqBienEtreRepository.deleteByNmatAndAnnee(entite.get().getNmat(), entite.get().getAnnee());
            enqSalarieEnteteRepository.deleteByNmatAndAnnee(entite.get().getNmat(), entite.get().getAnnee());
        }

        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/bienetre/{matricule}", produces = {MediaType.ALL_VALUE, MediaType.ALL_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<EnqBienEtre>> findBienEtreBySalarieAndYear(
            @PathVariable("matricule") String matricule,
            @RequestParam("year") String year
    ){
        // Bien-être
        List<EnqBienEtre> listeBienEtre = enqBienEtreRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeBienEtre == null || listeBienEtre.size()==0) {
            listeBienEtre = new ArrayList<>();
            listeBienEtre.add(new EnqBienEtre(matricule, Integer.parseInt(year), 1, "Comment vous sentez-vous dans votre travail ?.........................................................", StringUtils.EMPTY));
            listeBienEtre.add(new EnqBienEtre(matricule, Integer.parseInt(year), 2, "Le poste que vous occupez vous plaît-il ?.....................................................................", StringUtils.EMPTY));
            listeBienEtre.add(new EnqBienEtre(matricule, Integer.parseInt(year), 3, "Les relations avec les autres membres de votre équipe sont-elles bonnes ?..", StringUtils.EMPTY));
            listeBienEtre.add(new EnqBienEtre(matricule, Integer.parseInt(year), 4, "Commentaires", StringUtils.EMPTY));

        }


        if(listeBienEtre!=null) {
            return ResponseEntity.ok(listeBienEtre);
        } else {
            throw new EntityNotFoundException("Liste vide");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/bilanobjectif/{matricule}", produces = {MediaType.ALL_VALUE, MediaType.ALL_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<EnqBilanObj>> findBilanObjectifBySalarieAndYear(
            @PathVariable("matricule") String matricule,
            @RequestParam("year") String year
    ){
        // Bilan objectif
        List<EnqBilanObj> listeBilanObj = enqBilanObjRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeBilanObj == null || listeBilanObj.size()==0) {
            listeBilanObj.add(new EnqBilanObj(matricule, Integer.parseInt(year)));
            listeBilanObj.add(new EnqBilanObj(matricule, Integer.parseInt(year)));
            listeBilanObj.add(new EnqBilanObj(matricule, Integer.parseInt(year)));
        }


        if(listeBilanObj!=null) {
            return ResponseEntity.ok(listeBilanObj);
        } else {
            throw new EntityNotFoundException("Liste vide");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/bilanactivite/{matricule}", produces = {MediaType.ALL_VALUE, MediaType.ALL_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<EnqBilanAct>> findBilanActiviteBySalarieAndYear(
            @PathVariable("matricule") String matricule,
            @RequestParam("year") String year
    ){
        // Bilan activité
        List<EnqBilanAct> listeBilanAct= enqBilanActRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeBilanAct == null || listeBilanAct.size()==0) {
            listeBilanAct.add(new EnqBilanAct(matricule, Integer.parseInt(year)));
            listeBilanAct.add(new EnqBilanAct(matricule, Integer.parseInt(year)));
        }


        if(listeBilanAct!=null) {
            return ResponseEntity.ok(listeBilanAct);
        } else {
            throw new EntityNotFoundException("Liste vide");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/bilancompetenceposte/{matricule}", produces = {MediaType.ALL_VALUE, MediaType.ALL_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<EnqBilanCompPoste>> findBilanCompetencePosteBySalarieAndYear(
            @PathVariable("matricule") String matricule,
            @RequestParam("year") String year
    ){
        // Bilan compétence poste
        List<EnqBilanCompPoste> listeBilanCompPoste= enqBilanCompPosteRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeBilanCompPoste == null || listeBilanCompPoste.size()==0) {
            listeBilanCompPoste.add(new EnqBilanCompPoste(matricule, Integer.parseInt(year), 1, "Que réussissez-vous le mieux dans votre poste actuel ?", StringUtils.EMPTY));
            listeBilanCompPoste.add(new EnqBilanCompPoste(matricule, Integer.parseInt(year), 2, "Qu'aimez-vous le plus dans vos fonctions actuelles ?", StringUtils.EMPTY));
            listeBilanCompPoste.add(new EnqBilanCompPoste(matricule, Integer.parseInt(year), 3, "Quels sont vos points d'amélioration ou compétences à acquérir ?", StringUtils.EMPTY));
            listeBilanCompPoste.add(new EnqBilanCompPoste(matricule, Integer.parseInt(year), 4, "Avez-vous des compétences non utilisées susceptibles d'être mises à profit dans votre vie professionnelle ?", StringUtils.EMPTY));
        }


        if(listeBilanCompPoste!=null) {
            return ResponseEntity.ok(listeBilanCompPoste);
        } else {
            throw new EntityNotFoundException("Liste vide");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/bilancompetenceattendue/{matricule}", produces = {MediaType.ALL_VALUE, MediaType.ALL_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<EnqBilanCompAtt>> findBilanCompetenceAttendueBySalarieAndYear(
            @PathVariable("matricule") String matricule,
            @RequestParam("year") String year
    ){
        // Bilan compétence attendue
        List<EnqBilanCompAtt> listeBilanCompAtt= enqBilanCompAttRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeBilanCompAtt == null || listeBilanCompAtt.size()==0) {
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Maîtrise des connaissances théoriques"));
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Maîtrise des éléments techniques"));
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Capacité à animer des réunions"));
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Capacité à gérer des projets"));
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Créativité"));
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Esprit d'équipe"));
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Gestion des priorités"));
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Organisation"));
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Autonomie"));
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Implication au travail"));
            listeBilanCompAtt.add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Motivation"));
        }


        if(listeBilanCompAtt!=null) {
            return ResponseEntity.ok(listeBilanCompAtt);
        } else {
            throw new EntityNotFoundException("Liste vide");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/condition/{matricule}", produces = {MediaType.ALL_VALUE, MediaType.ALL_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<EnqCondition>> findConditionBySalarieAndYear(
            @PathVariable("matricule") String matricule,
            @RequestParam("year") String year
    ){
        // Condition
        List<EnqCondition> listeCondition= enqConditionRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeCondition == null || listeCondition.size()==0) {
            listeCondition.add(new EnqCondition(matricule, Integer.parseInt(year), "Quelles sont vos remarques, points d'amélioration ou suggestions d’amélioration liés à vos conditions d’activité ? (Environnement de travail, communication, etc.)"));
            listeCondition.add(new EnqCondition(matricule, Integer.parseInt(year), "Comment évaluez-vous votre charge de travail ? (Adéquation de la charge de travail avec le nombre de jours travaillés, variabilité de l’activité, heures supplémentaires, atteinte des objectifs, etc.)"));
            listeCondition.add(new EnqCondition(matricule, Integer.parseInt(year), "Quel est votre ressenti quant à l’articulation de votre vie privée/ vie professionnelle ? (Conciliation vie professionnelle et vie personnelle, déconnexion, déplacements, etc.)"));
        }


        if(listeCondition!=null) {
            return ResponseEntity.ok(listeCondition);
        } else {
            throw new EntityNotFoundException("Liste vide");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/formationsouhaitee/{matricule}", produces = {MediaType.ALL_VALUE, MediaType.ALL_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<EnqFormSouhait>> findFormationSouhaiteeBySalarieAndYear(
            @PathVariable("matricule") String matricule,
            @RequestParam("year") String year
    ){
        // Formation souhaitée
        List<EnqFormSouhait> listeFormationSouhait = enqFormSouhaitRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeFormationSouhait == null || listeFormationSouhait.size()==0) {
            listeFormationSouhait.add(new EnqFormSouhait(matricule, Integer.parseInt(year)));
            listeFormationSouhait.add(new EnqFormSouhait(matricule, Integer.parseInt(year)));
            listeFormationSouhait.add(new EnqFormSouhait(matricule, Integer.parseInt(year)));
        }

        if(listeFormationSouhait!=null) {
            return ResponseEntity.ok(listeFormationSouhait);
        } else {
            throw new EntityNotFoundException("Liste vide");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/formationsuivie/{matricule}", produces = {MediaType.ALL_VALUE, MediaType.ALL_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<EnqFormSuivie>> findFormationSuivieBySalarieAndYear(
            @PathVariable("matricule") String matricule,
            @RequestParam("year") String year
    ){

        // Formation suivie
        List<EnqFormSuivie> listeFormationSuivie = enqFormSuivieRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeFormationSuivie == null || listeFormationSuivie.size()==0) {
            listeFormationSuivie.add(new EnqFormSuivie(matricule, Integer.parseInt(year)));
            listeFormationSuivie.add(new EnqFormSuivie(matricule, Integer.parseInt(year)));
            listeFormationSuivie.add(new EnqFormSuivie(matricule, Integer.parseInt(year)));
        }

        if(listeFormationSuivie!=null) {
            return ResponseEntity.ok(listeFormationSuivie);
        } else {
            throw new EntityNotFoundException("Liste vide");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/enquete/liste/{matricule}", produces = {MediaType.ALL_VALUE, MediaType.ALL_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<EnqSalarieEntete>> findEnqueteBySalarieAndYear(
            @PathVariable("matricule") String matricule,
            @RequestParam("year") String year,
            @RequestParam("drhl") String drhl
    ){

        // Formation suivie
        List<EnqSalarieEntete> listeEnquete = null;
        if(StringUtils.isNotEmpty(drhl) && drhl.equalsIgnoreCase("O")){
            listeEnquete = enqSalarieEnteteRepository.findAll();
            if(listeEnquete == null) listeEnquete = new ArrayList<>();
        }
        else {
            listeEnquete = new ArrayList<>();
            Optional<EnqSalarieEntete> entity = enqSalarieEnteteRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
            if(entity.isPresent())
                listeEnquete.add(entity.get());
            else {
                EnqSalarieEntete enq = new EnqSalarieEntete();
                enq.setAnnee(Integer.parseInt(year));
                enq.setNmat(matricule);
                listeEnquete.add(enq);
            }
        }

        if(listeEnquete!=null) {
            return ResponseEntity.ok(listeEnquete);
        } else {
            throw new EntityNotFoundException("Liste vide");
        }
    }

}
