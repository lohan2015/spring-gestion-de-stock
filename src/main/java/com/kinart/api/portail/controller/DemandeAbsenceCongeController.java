package com.kinart.api.portail.controller;

import com.kinart.api.gestiondepaie.dto.CalendrierPaieDto;
import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.gestiondestock.dto.ArticleDto;
import com.kinart.api.mail.EmailDetails;
import com.kinart.api.mail.service.EmailService;
import com.kinart.api.portail.dto.DemandeAbsenceCongeDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.portail.business.model.NotifAbsConge;
import com.kinart.portail.business.repository.DemandeAbsCongeRepository;
import com.kinart.portail.business.repository.NotifAbsCongeRepository;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.portail.business.validator.ObjectsValidator;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PAIE;
import static com.kinart.stock.business.utils.Constants.APP_ROOT_PORTAIL;

@Api("demande-absence-conge")
@RestController
@RequiredArgsConstructor
public class DemandeAbsenceCongeController {

    private final ObjectsValidator<DemandeAbsenceCongeDto> validator;
    private final DemandeAbsCongeRepository repository;
    private final NotifAbsCongeRepository absCongeRepository;
    private final EmailService emailService;
    private final ParamDataRepository paramDataRepository;

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/user", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    @Transactional
    ResponseEntity<DemandeAbsenceCongeDto> saveUser(@RequestBody DemandeAbsenceCongeDto dto){
        validator.validate(dto);
        try {
            repository.save(DemandeAbsenceCongeDto.toEntity(dto));

            // Sauvegarde modification
            String modele = "Bonjour,"+
                            "Merci de bien vouloir traiter la demande de congé de $SENDER du $DATEDEBUT au $DATEFIN."+
                            "Cdlt,";
            ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "MOD_ABSCGE", Integer.valueOf(1))
                    .map(ParamDataDto::fromEntity)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Aucune donnée avec l'ID = "+"MOD_ABSCGE"+" n' ete trouve dans la table 99",
                                    ErrorCodes.ARTICLE_NOT_FOUND)
                    );
            if(fnom == null)
                throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"MOD_ABSCGE"+" n' ete trouve dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
           else modele = fnom.getVall();

            modele = modele.replaceAll("\\$SENDER", dto.getUserDemAbsCg().getPrenom()+" "+dto.getUserDemAbsCg().getNom());
            modele = modele.replaceAll("\\$DATEDEBUT", new SimpleDateFormat("MM-dd-yyyy").format(dto.getDteDebut()));
            modele = modele.replaceAll("\\$DATEFIN", new SimpleDateFormat("MM-dd-yyyy").format(dto.getDteFin()));

            NotifAbsConge notifAbsConge = new NotifAbsConge();
            notifAbsConge.setSender(dto.getUserDemAbsCg().getEmail());
            notifAbsConge.setRecipient(dto.getUserDemAbsCg().getValid1());
            notifAbsConge.setMessage(modele);
            notifAbsConge.setCreationDate(Instant.now());
            notifAbsConge.setLastModifiedDate(Instant.now());
            absCongeRepository.save(notifAbsConge);

            // Envoi notification
            EmailDetails paramMail = new EmailDetails();
            paramMail.setMsgBody(notifAbsConge.getMessage());
           paramMail.setRecipient(notifAbsConge.getRecipient());
            paramMail.setSubject("Notification absence / congé");
            try {
                    emailService.sendSimpleMail(paramMail);
                } catch (Exception e){
                    e.printStackTrace();
                }

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid1", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<CalendrierPaieDto> saveValid1(@RequestBody DemandeAbsenceCongeDto dto){
        validator.validate(dto);
        try {
            repository.save(DemandeAbsenceCongeDto.toEntity(dto));
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid2", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<CalendrierPaieDto> saveValid2(@RequestBody DemandeAbsenceCongeDto dto){
        validator.validate(dto);
        try {
            repository.save(DemandeAbsenceCongeDto.toEntity(dto));
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid3", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<CalendrierPaieDto> saveValid3(@RequestBody DemandeAbsenceCongeDto dto){
        validator.validate(dto);
        try {
            repository.save(DemandeAbsenceCongeDto.toEntity(dto));
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid4", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<CalendrierPaieDto> saveValid4(@RequestBody DemandeAbsenceCongeDto dto){
        validator.validate(dto);
        try {
            repository.save(DemandeAbsenceCongeDto.toEntity(dto));
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeDto>> findByUser(@PathVariable("email") String email){
        List<DemandeAbsenceCongeDto> demandeAbsenceConges = repository.searchByUserEmail(email).stream()
                                                                                            .map(DemandeAbsenceCongeDto::fromEntity)
                                                                                            .collect(Collectors.toList());
        // Demandes soumis a notre validation
        demandeAbsenceConges.addAll(repository.searchByEmail(email).stream()
                                                                    .map(DemandeAbsenceCongeDto::fromEntity)
                                                                    .collect(Collectors.toList()));
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/attente/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeDto>> findByUserAttente(@PathVariable("email") String email){
        List<DemandeAbsenceCongeDto> demandeAbsenceConges = repository.searchByEmailAndStatus(email, EnumStatusType.EATTENTE_VALIDATION.getCode()).stream()
                                                                        .map(DemandeAbsenceCongeDto::fromEntity)
                                                                        .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @DeleteMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/{demand-id}")
    @ApiOperation(value = "Supprimer une demande pas encore validée", notes = "Cette methode permet de supprimer une demande pas encore validée")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("demand-id") Integer demandid){
        repository.deleteById(demandid);
    }
}
