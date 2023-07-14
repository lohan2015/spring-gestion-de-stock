package com.kinart.api.portail.controller;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.portail.dto.DemandeAttestationDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.portail.business.model.DemandeAttestation;
import com.kinart.portail.business.repository.DemandeAttestationRepository;
import com.kinart.portail.business.service.NotificationAttestationService;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.portail.business.validator.ObjectsValidator;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import com.kinart.stock.business.model.Utilisateur;
import com.kinart.stock.business.repository.UtilisateurRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PORTAIL;

@Api("demande-attestation")
@RestController
@RequiredArgsConstructor
public class DemandeAttestationController {

    private final ObjectsValidator<DemandeAttestationDto> validator;
    private final DemandeAttestationRepository repository;
    private final NotificationAttestationService notificationService;
    private final ParamDataRepository paramDataRepository;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/attestation/user", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande attestation", notes = "Cette methode permet d'enregistrer des demandes attestation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAttestationDto> saveUser(@RequestBody DemandeAttestationDto dto){
        validator.validate(dto);

        try {
            // Get info user
            Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemAttest().getEmail());
            if(user.isPresent()){
                dto.getUserDemAttest().setNom(user.get().getNom());
                dto.getUserDemAttest().setPrenom(user.get().getPrenom());
            } else throw new EntityNotFoundException("Utilisateur inexistant");

            // Fixer les validateurs
            String emailValidator = "cyrille.mbassi@yahoo.com";
            ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "VAL_SCE", Integer.valueOf(1))
                    .map(ParamDataDto::fromEntity)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Aucune donnée avec l'ID = "+"VAL_SCE"+" n' ete trouve dans la table 99",
                                    ErrorCodes.ARTICLE_NOT_FOUND)
                    );

            if(fnom == null)
                throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"VAL_SCE"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
            else emailValidator = fnom.getVall();
            dto.setScePersonnel(emailValidator);
            dto.setStatus(EnumStatusType.EATTENTE_VALIDATION);
            repository.save(DemandeAttestationDto.toEntity(dto));

            // Gestion des notifications
            notificationService.sendAttestationNotification(dto, dto.getScePersonnel());

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/attestation/valid", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande attestation", notes = "Cette methode permet d'enregistrer des demandes attestation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAttestationDto> saveValid(@RequestBody DemandeAttestationDto dto){
        validator.validate(dto);
        try {
            Optional<DemandeAttestation> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandeAttestation entite = dbDemande.get();
                entite.setStatus(dto.getStatus());
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemAttest().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entite.getScePersonnel());
                if(user.isPresent()){
                    dto.getUserDemAttest().setNom(user.get().getNom());
                    dto.getUserDemAttest().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendAttestationNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                } else if(dto.getStatus().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendAttestationNotificationRejet(dto);
            }
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/attestation/status/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAttestationDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAttestationDto>> findByUserAndDateStatus(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status1")  String status1
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandeAttestationDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriodeStatus(start, end, email, status1).stream()
                .map(DemandeAttestationDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @DeleteMapping(value = APP_ROOT_PORTAIL + "/demande/attestation/{demand-id}")
    @ApiOperation(value = "Supprimer une demande pas encore validée", notes = "Cette methode permet de supprimer une demande pas encore validée")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("demand-id") Integer demandid) throws Exception {
        Optional<DemandeAttestation> entite = repository.findById(demandid);
        if(entite.isPresent()) repository.deleteById(demandid);

        // Notification validateur de l'annulation
        DemandeAttestationDto dto = DemandeAttestationDto.fromEntity(entite.get());
        notificationService.sendAnnulationAttestationNotification(dto, dto.getScePersonnel());
    }
}
