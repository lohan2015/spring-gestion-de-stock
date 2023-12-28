package com.kinart.api.portail.controller;

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
    ResponseEntity<EnqSalarieEnteteDto> saveEntete(@RequestBody EnqSalarieEnteteDto dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
            EnqSalarieEntete entity = EnqSalarieEnteteDto.toEntity(dto);
            enqSalarieEnteteRepository.save(entity);
            dto.setId(entity.getId());
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
                for(EnqBienEtre entity : dto.getListeBienEtre()) {
                    if(entity.getId()==null || entity.getId()==0)
                      enqBienEtreRepository.save(entity);
                    else {
                        Optional<EnqBienEtre> oldEntityOpt = enqBienEtreRepository.findById(entity.getId());
                        if(oldEntityOpt.isPresent()){
                            EnqBienEtre oldEntity = oldEntityOpt.get();
                            BeanUtils.copyProperties(entity, oldEntity, "id");
                            enqBienEtreRepository.save(oldEntity);
                        }
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
    ResponseEntity<EnqSalarieEnteteDto> saveBilanObjectif(@RequestBody EnqSalarieEnteteDto dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
            for(EnqBilanObj entity : dto.getListeBilanObjectif()) {
                if(entity.getId()==null || entity.getId()==0)
                    enqBilanObjRepository.save(entity);
                else {
                    Optional<EnqBilanObj> oldEntityOpt = enqBilanObjRepository.findById(entity.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqBilanObj oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(entity, oldEntity, "id");
                        enqBilanObjRepository.save(oldEntity);
                    }
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
    ResponseEntity<EnqSalarieEnteteDto> saveBilanActivite(@RequestBody EnqSalarieEnteteDto dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
            for(EnqBilanAct entity : dto.getListeBilanActivites()) {
                if(entity.getId()==null || entity.getId()==0)
                    enqBilanActRepository.save(entity);
                else {
                    Optional<EnqBilanAct> oldEntityOpt = enqBilanActRepository.findById(entity.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqBilanAct oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(entity, oldEntity, "id");
                        enqBilanActRepository.save(oldEntity);
                    }
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
    ResponseEntity<EnqSalarieEnteteDto> saveBilanCompPoste(@RequestBody EnqSalarieEnteteDto dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
            for(EnqBilanCompPoste entity : dto.getListeBilanCompPoste()) {
                if(entity.getId()==null || entity.getId()==0)
                    enqBilanCompPosteRepository.save(entity);
                else {
                    Optional<EnqBilanCompPoste> oldEntityOpt = enqBilanCompPosteRepository.findById(entity.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqBilanCompPoste oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(entity, oldEntity, "id");
                        enqBilanCompPosteRepository.save(oldEntity);
                    }
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
    ResponseEntity<EnqSalarieEnteteDto> saveBilanCompAttendu(@RequestBody EnqSalarieEnteteDto dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
            for(EnqBilanCompAtt entity : dto.getListeBilanCompAttendu()) {
                if(entity.getId()==null || entity.getId()==0)
                    enqBilanCompAttRepository.save(entity);
                else {
                    Optional<EnqBilanCompAtt> oldEntityOpt = enqBilanCompAttRepository.findById(entity.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqBilanCompAtt oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(entity, oldEntity, "id");
                        enqBilanCompAttRepository.save(oldEntity);
                    }
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
    ResponseEntity<EnqSalarieEnteteDto> saveFormationSuivie(@RequestBody EnqSalarieEnteteDto dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
            for(EnqFormSuivie entity : dto.getListeFormationSuivie()) {
                if(entity.getId()==null || entity.getId()==0)
                    enqFormSuivieRepository.save(entity);
                else {
                    Optional<EnqFormSuivie> oldEntityOpt = enqFormSuivieRepository.findById(entity.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqFormSuivie oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(entity, oldEntity, "id");
                        enqFormSuivieRepository.save(oldEntity);
                    }
                }
            }

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
    ResponseEntity<EnqSalarieEnteteDto> saveFormationSouhaitee(@RequestBody EnqSalarieEnteteDto dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
            for(EnqFormSouhait entity : dto.getListeFormationSouhaitee()) {
                if(entity.getId()==null || entity.getId()==0)
                    enqFormSouhaitRepository.save(entity);
                else {
                    Optional<EnqFormSouhait> oldEntityOpt = enqFormSouhaitRepository.findById(entity.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqFormSouhait oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(entity, oldEntity, "id");
                        enqFormSouhaitRepository.save(oldEntity);
                    }
                }
            }

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
    ResponseEntity<EnqSalarieEnteteDto> saveCondition(@RequestBody EnqSalarieEnteteDto dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
            for(EnqCondition entity : dto.getListeCondition()) {
                if(entity.getId()==null || entity.getId()==0)
                    enqConditionRepository.save(entity);
                else {
                    Optional<EnqCondition> oldEntityOpt = enqConditionRepository.findById(entity.getId());
                    if(oldEntityOpt.isPresent()){
                        EnqCondition oldEntity = oldEntityOpt.get();
                        BeanUtils.copyProperties(entity, oldEntity, "id");
                        enqConditionRepository.save(oldEntity);
                    }
                }
            }

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
        EnqSalarieEnteteDto result = new EnqSalarieEnteteDto();
        Optional<EnqSalarieEntete> entity = enqSalarieEnteteRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(entity.isPresent())
            BeanUtils.copyProperties(entity.get(), result);
        else {
            result.setAnnee(Integer.parseInt(year));
            result.setNmat(matricule);
        }

        // Bien-être
        List<EnqBienEtre> listeBienEtre = enqBienEtreRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeBienEtre != null) result.setListeBienEtre(listeBienEtre);
        else {
            result.getListeBienEtre().add(new EnqBienEtre(matricule, Integer.parseInt(year), 1, "Comment vous sentez-vous dans votre travail ?", StringUtils.EMPTY));
            result.getListeBienEtre().add(new EnqBienEtre(matricule, Integer.parseInt(year), 2, "Le poste que vous occupez vous plaît-il ?", StringUtils.EMPTY));
            result.getListeBienEtre().add(new EnqBienEtre(matricule, Integer.parseInt(year), 3, "Les relations avec les autres membres de votre équipe sont-elles bonnes ?", StringUtils.EMPTY));
            result.getListeBienEtre().add(new EnqBienEtre(matricule, Integer.parseInt(year), 4, "Commentaires", StringUtils.EMPTY));
        }

        // Bilan objectif
        List<EnqBilanObj> listeBilanObj = enqBilanObjRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeBilanObj != null) result.setListeBilanObjectif(listeBilanObj);
        else {
            result.getListeBilanObjectif().add(new EnqBilanObj(matricule, Integer.parseInt(year)));
            result.getListeBilanObjectif().add(new EnqBilanObj(matricule, Integer.parseInt(year)));
            result.getListeBilanObjectif().add(new EnqBilanObj(matricule, Integer.parseInt(year)));
        }

        // Bilan activité
        List<EnqBilanAct> listeBilanAct= enqBilanActRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeBilanAct != null) result.setListeBilanActivites(listeBilanAct);
        else {
            result.getListeBilanActivites().add(new EnqBilanAct(matricule, Integer.parseInt(year)));
            result.getListeBilanActivites().add(new EnqBilanAct(matricule, Integer.parseInt(year)));
        }

        // Bilan compétence poste
        List<EnqBilanCompPoste> listeBilanCompPoste= enqBilanCompPosteRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeBilanCompPoste != null) result.setListeBilanCompPoste(listeBilanCompPoste);
        else {
            result.getListeBilanCompPoste().add(new EnqBilanCompPoste(matricule, Integer.parseInt(year), 1, "Que réussissez-vous le mieux dans votre poste actuel ?", StringUtils.EMPTY));
            result.getListeBilanCompPoste().add(new EnqBilanCompPoste(matricule, Integer.parseInt(year), 2, "Qu'aimez-vous le plus dans vos fonctions actuelles ?", StringUtils.EMPTY));
            result.getListeBilanCompPoste().add(new EnqBilanCompPoste(matricule, Integer.parseInt(year), 3, "Quels sont vos points d'amélioration ou compétences à acquérir ?", StringUtils.EMPTY));
            result.getListeBilanCompPoste().add(new EnqBilanCompPoste(matricule, Integer.parseInt(year), 4, "Avez-vous des compétences non utilisées susceptibles d'être mises à profit dans votre vie professionnelle ?", StringUtils.EMPTY));
        }

        // Bilan compétence attendue
        List<EnqBilanCompAtt> listeBilanCompAtt= enqBilanCompAttRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeBilanCompAtt != null) result.setListeBilanCompAttendu(listeBilanCompAtt);
        else {
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Maîtrise des connaissances théoriques"));
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Maîtrise des éléments techniques"));
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Capacité à animer des réunions"));
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Capacité à gérer des projets"));
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Créativité"));
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Esprit d'équipe"));
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Gestion des priorités"));
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Organisation"));
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Autonomie"));
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Implication au travail"));
            result.getListeBilanCompAttendu().add(new EnqBilanCompAtt(matricule, Integer.parseInt(year), "Motivation"));
        }

        // Formation suivie
        List<EnqFormSuivie> listeFormationSuivie = enqFormSuivieRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeFormationSuivie != null) result.setListeFormationSuivie(listeFormationSuivie);
        else {
            result.getListeFormationSuivie().add(new EnqFormSuivie(matricule, Integer.parseInt(year)));
            result.getListeFormationSuivie().add(new EnqFormSuivie(matricule, Integer.parseInt(year)));
            result.getListeFormationSuivie().add(new EnqFormSuivie(matricule, Integer.parseInt(year)));
        }

        // Formation souhaitée
        List<EnqFormSouhait> listeFormationSouhait = enqFormSouhaitRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeFormationSouhait != null) result.setListeFormationSouhaitee(listeFormationSouhait);
        else {
            result.getListeFormationSouhaitee().add(new EnqFormSouhait(matricule, Integer.parseInt(year)));
            result.getListeFormationSouhaitee().add(new EnqFormSouhait(matricule, Integer.parseInt(year)));
            result.getListeFormationSouhaitee().add(new EnqFormSouhait(matricule, Integer.parseInt(year)));
        }

        // Condition
        List<EnqCondition> listeCondition= enqConditionRepository.findByNmatAndAnnee(matricule, Integer.parseInt(year));
        if(listeCondition != null) result.setListeCondition(listeCondition);
        else {
            result.getListeCondition().add(new EnqCondition(matricule, Integer.parseInt(year), "Quelles sont vos remarques, points d'amélioration ou suggestions d’amélioration liés à vos conditions d’activité ? (Environnement de travail, communication, etc.)"));
            result.getListeCondition().add(new EnqCondition(matricule, Integer.parseInt(year), "Comment évaluez-vous votre charge de travail ? (Adéquation de la charge de travail avec le nombre de jours travaillés, variabilité de l’activité, heures supplémentaires, atteinte des objectifs, etc.)"));
            result.getListeCondition().add(new EnqCondition(matricule, Integer.parseInt(year), "Quel est votre ressenti quant à l’articulation de votre vie privée/ vie professionnelle ? (Conciliation vie professionnelle et vie personnelle, déconnexion, déplacements, etc.)"));
        }

        if(result!=null) {
            return ResponseEntity.ok(result);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

}
