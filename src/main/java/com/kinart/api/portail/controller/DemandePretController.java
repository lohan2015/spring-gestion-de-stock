package com.kinart.api.portail.controller;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.portail.dto.DemandePretDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.portail.business.model.DemandePret;
import com.kinart.portail.business.repository.DemandePretRepository;
import com.kinart.portail.business.service.NotificationPretService;
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

@Api("demande-pret")
@RestController
@RequiredArgsConstructor
public class DemandePretController {

    private final ObjectsValidator<DemandePretDto> validator;
    private final UtilisateurRepository utilisateurRepository;
    private final DemandePretRepository repository;
    private final NotificationPretService notificationService;
    private final ParamDataRepository paramDataRepository;

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/pret/user", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande de prêt", notes = "Cette methode permet d'enregistrer des demandes de prêt")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandePretDto> saveUser(@RequestBody DemandePretDto dto){
        validator.validate(dto);
        try {
            // Fixer les validateurs
            // Service personnel
            ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "VAL_SCE", Integer.valueOf(1))
                    .map(ParamDataDto::fromEntity)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Aucune donnée avec l'ID = "+"VAL_SCE"+" n' ete trouve dans la table 99",
                                    ErrorCodes.ARTICLE_NOT_FOUND)
                    );

            if(fnom == null)
                throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"VAL_SCE"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
            else dto.setScePersonnel(fnom.getVall());
            // DRHL
            fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "VAL_DH", Integer.valueOf(1))
                    .map(ParamDataDto::fromEntity)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Aucune donnée avec l'ID = "+"VAL_DH"+" n' ete trouve dans la table 99",
                                    ErrorCodes.ARTICLE_NOT_FOUND)
                    );

            if(fnom == null)
                throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"VAL_DH"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
            else dto.setDrhl(fnom.getVall());
            // DGA
            fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "VAL_DGA", Integer.valueOf(1))
                    .map(ParamDataDto::fromEntity)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Aucune donnée avec l'ID = "+"VAL_DGA"+" n' ete trouve dans la table 99",
                                    ErrorCodes.ARTICLE_NOT_FOUND)
                    );

            if(fnom == null)
                throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"VAL_DGA"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
            else dto.setDga(fnom.getVall());
            // DG
            fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "VAL_DG", Integer.valueOf(1))
                    .map(ParamDataDto::fromEntity)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Aucune donnée avec l'ID = "+"VAL_DG"+" n' ete trouve dans la table 99",
                                    ErrorCodes.ARTICLE_NOT_FOUND)
                    );

            if(fnom == null)
                throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"VAL_DG"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
            else dto.setDg(fnom.getVall());

            Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemPret().getEmail());
            if(user.isPresent()){
                dto.getUserDemPret().setNom(user.get().getNom());
                dto.getUserDemPret().setPrenom(user.get().getPrenom());
            } else throw new EntityNotFoundException("Utilisateur inexistant");


            dto.setStatus1(EnumStatusType.EATTENTE_VALIDATION);
            dto.setStatus2(EnumStatusType.NONE);
            dto.setStatus3(EnumStatusType.NONE);
            dto.setStatus4(EnumStatusType.NONE);
            repository.save(DemandePretDto.toEntity(dto));

            // Gestion des notifications
            notificationService.sendPretNotification(dto, dto.getScePersonnel());

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/pret/scepersonnel", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandePretDto> saveValidScePersonnel(@RequestBody DemandePretDto dto){
        validator.validate(dto);
        try {
            Optional<DemandePret> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandePret entite = dbDemande.get();
                entite.setStatus1(dto.getStatus1());
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE)) entite.setStatus2(EnumStatusType.EATTENTE_VALIDATION);
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemPret().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entite.getDrhl());
                if(user.isPresent()){
                    dto.getUserDemPret().setNom(user.get().getNom());
                    dto.getUserDemPret().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendPretNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                    notificationService.sendPretNotification(dto, entite.getDrhl());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendPretNotificationRejet(dto);
            }
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/pret/drhl", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandePretDto> saveValidDrhl(@RequestBody DemandePretDto dto){
        validator.validate(dto);
        try {
            Optional<DemandePret> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandePret entite = dbDemande.get();
                entite.setStatus2(dto.getStatus2());
                if(dto.getStatus2().equals(EnumStatusType.VALIDEE)) entite.setStatus3(EnumStatusType.EATTENTE_VALIDATION);
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemPret().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entite.getDga());
                if(user.isPresent()){
                    dto.getUserDemPret().setNom(user.get().getNom());
                    dto.getUserDemPret().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendPretNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                    notificationService.sendPretNotification(dto, entite.getDga());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendPretNotificationRejet(dto);
            }
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/pret/dga", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandePretDto> saveValidDga(@RequestBody DemandePretDto dto){
        validator.validate(dto);
        try {
            Optional<DemandePret> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandePret entite = dbDemande.get();
                entite.setStatus3(dto.getStatus3());
                if(dto.getStatus3().equals(EnumStatusType.VALIDEE)) entite.setStatus4(EnumStatusType.EATTENTE_VALIDATION);
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemPret().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entite.getDg());
                if(user.isPresent()){
                    dto.getUserDemPret().setNom(user.get().getNom());
                    dto.getUserDemPret().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendPretNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                    notificationService.sendPretNotification(dto, entite.getDg());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendPretNotificationRejet(dto);
            }
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/pret/valid4", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandePretDto> saveValid4(@RequestBody DemandePretDto dto){
        validator.validate(dto);
        try {
            Optional<DemandePret> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandePret entite = dbDemande.get();
                entite.setStatus4(dto.getStatus4());
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemPret().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(dto.getDg());
                if(user.isPresent()){
                    dto.getUserDemPret().setNom(user.get().getNom());
                    dto.getUserDemPret().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendPretNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendPretNotificationRejet(dto);
            }
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretDto>> findByUser(@PathVariable("email") String email){
        List<DemandePretDto> demandeAbsenceConges = repository.searchByUserEmail(email).stream()
                                                                                            .map(DemandePretDto::fromEntity)
                                                                                            .collect(Collectors.toList());
        // Demandes soumis a notre validation. Exclure les demandes existantes déja dans la liste
        demandeAbsenceConges.addAll(repository.searchByEmail(email).stream()
                                                                    .map(DemandePretDto::fromEntity)
                                                                    .collect(Collectors.toList()));
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/attente/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretDto>> findByUserAttente(@PathVariable("email") String email){
        List<DemandePretDto> demandeAbsenceConges = repository.searchByEmailAndStatus(email, EnumStatusType.EATTENTE_VALIDATION.getCode()).stream()
                                                                        .map(DemandePretDto::fromEntity)
                                                                        .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/list-by-date/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretDto>> findByUserAndDate(
          @PathVariable("email") String email,
          @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
          @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandePretDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriode(start, end, email).stream()
                .map(DemandePretDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/status1/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretDto>> findByUserAndDateStatus1(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status1")  String status1
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandePretDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriodeStatus1(start, end, email, status1).stream()
                .map(DemandePretDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/status2/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretDto>> findByUserAndDateStatus2(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status2")  String status2
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandePretDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriodeStatus2(start, end, email, status2).stream()
                .map(DemandePretDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/status3/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretDto>> findByUserAndDateStatus3(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status3")  String status3
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandePretDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriodeStatus3(start, end, email, status3).stream()
                .map(DemandePretDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/status4/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretDto>> findByUserAndDateStatus4(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status3")  String status4
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandePretDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriodeStatus4(start, end, email, status4).stream()
                .map(DemandePretDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @DeleteMapping(value = APP_ROOT_PORTAIL + "/demande/pret/{demand-id}")
    @ApiOperation(value = "Supprimer une demande pas encore validée", notes = "Cette methode permet de supprimer une demande pas encore validée")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("demand-id") Integer demandid) throws Exception {
        Optional<DemandePret> entite = repository.findById(demandid);
        if(entite.isPresent()) repository.deleteById(demandid);

        // Notification validateur de l'annulation
        DemandePretDto dto = DemandePretDto.fromEntity(entite.get());
        notificationService.sendAnnulationPretNotification(dto, dto.getScePersonnel());
    }

}
