package com.kinart.api.portail.controller;

import com.kinart.api.gestiondepaie.dto.CalendrierPaieDto;
import com.kinart.api.portail.dto.DemandeAbsenceCongeDto;
import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.repository.DemandeAbsCongeRepository;
import com.kinart.portail.business.service.NotificationAbsenceCongeService;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.portail.business.validator.ObjectsValidator;
import com.kinart.stock.business.exception.EntityNotFoundException;
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

@Api("demande-absence-conge")
@RestController
@RequiredArgsConstructor
public class DemandeAbsenceCongeController {

    private final ObjectsValidator<DemandeAbsenceCongeDto> validator;
    private final UtilisateurRepository utilisateurRepository;
    private final DemandeAbsCongeRepository repository;
    private final NotificationAbsenceCongeService notificationService;

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/user", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAbsenceCongeDto> saveUser(@RequestBody DemandeAbsenceCongeDto dto){
        validator.validate(dto);
        try {
            // Fixer les validateurs
            Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemAbsCg().getEmail());
            if(user.isPresent()){
                dto.getUserDemAbsCg().setNom(user.get().getNom());
                dto.getUserDemAbsCg().setPrenom(user.get().getPrenom());
                dto.setValid1(user.get().getValid1());
                dto.setValid2(user.get().getValid2());
                dto.setValid3(user.get().getValid3());
                dto.setValid4(user.get().getValid4());
            } else throw new EntityNotFoundException("Utilisateur inexistant");

            dto.setStatus1(EnumStatusType.EATTENTE_VALIDATION);
            dto.setStatus2(EnumStatusType.NONE);
            dto.setStatus3(EnumStatusType.NONE);
            dto.setStatus4(EnumStatusType.NONE);
            repository.save(DemandeAbsenceCongeDto.toEntity(dto));

            // Gestion des notifications
            notificationService.sendAbsenceCongeNotification(dto, dto.getValid1());

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid1", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAbsenceCongeDto> saveValid1(@RequestBody DemandeAbsenceCongeDto dto){
        validator.validate(dto);
        try {
            Optional<DemandeAbsenceConge> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandeAbsenceConge entite = dbDemande.get();
                entite.setStatus1(dto.getStatus1());
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE)) entite.setStatus2(EnumStatusType.EATTENTE_VALIDATION);
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemAbsCg().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entite.getValid2());
                if(user.isPresent()){
                    dto.getUserDemAbsCg().setNom(user.get().getNom());
                    dto.getUserDemAbsCg().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendAbsCongeNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                    notificationService.sendAbsenceCongeNotification(dto, entite.getValid2());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendAbsCongeNotificationRejet(dto);
            }
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid2", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<CalendrierPaieDto> saveValid2(@RequestBody DemandeAbsenceCongeDto dto){
        validator.validate(dto);
        try {
            Optional<DemandeAbsenceConge> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandeAbsenceConge entite = dbDemande.get();
                entite.setStatus2(dto.getStatus2());
                if(dto.getStatus2().equals(EnumStatusType.VALIDEE)) entite.setStatus3(EnumStatusType.EATTENTE_VALIDATION);
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemAbsCg().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entite.getValid3());
                if(user.isPresent()){
                    dto.getUserDemAbsCg().setNom(user.get().getNom());
                    dto.getUserDemAbsCg().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendAbsCongeNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                    notificationService.sendAbsenceCongeNotification(dto, entite.getValid3());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendAbsCongeNotificationRejet(dto);
            }
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid3", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<CalendrierPaieDto> saveValid3(@RequestBody DemandeAbsenceCongeDto dto){
        validator.validate(dto);
        try {
            Optional<DemandeAbsenceConge> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandeAbsenceConge entite = dbDemande.get();
                entite.setStatus3(dto.getStatus3());
                if(dto.getStatus3().equals(EnumStatusType.VALIDEE)) entite.setStatus4(EnumStatusType.EATTENTE_VALIDATION);
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemAbsCg().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entite.getValid4());
                if(user.isPresent()){
                    dto.getUserDemAbsCg().setNom(user.get().getNom());
                    dto.getUserDemAbsCg().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendAbsCongeNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                    notificationService.sendAbsenceCongeNotification(dto, entite.getValid2());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendAbsCongeNotificationRejet(dto);
            }
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid4", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<CalendrierPaieDto> saveValid4(@RequestBody DemandeAbsenceCongeDto dto){
        validator.validate(dto);
        try {
            Optional<DemandeAbsenceConge> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandeAbsenceConge entite = dbDemande.get();
                entite.setStatus4(dto.getStatus4());
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemAbsCg().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(dto.getValid4());
                if(user.isPresent()){
                    dto.getUserDemAbsCg().setNom(user.get().getNom());
                    dto.getUserDemAbsCg().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendAbsCongeNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendAbsCongeNotificationRejet(dto);
            }
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
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
        // Demandes soumis a notre validation. Exclure les demandes existantes déja dans la liste
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

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/list-by-date/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeDto>> findByUserAndDate(
          @PathVariable("email") String email,
          @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
          @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandeAbsenceCongeDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriode(start, end, email).stream()
                .map(DemandeAbsenceCongeDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/status1/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeDto>> findByUserAndDateStatus1(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status1")  String status1
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandeAbsenceCongeDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriodeStatus1(start, end, email, status1).stream()
                .map(DemandeAbsenceCongeDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/status2/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeDto>> findByUserAndDateStatus2(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status2")  String status2
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandeAbsenceCongeDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriodeStatus2(start, end, email, status2).stream()
                .map(DemandeAbsenceCongeDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/status3/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeDto>> findByUserAndDateStatus3(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status3")  String status3
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandeAbsenceCongeDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriodeStatus3(start, end, email, status3).stream()
                .map(DemandeAbsenceCongeDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/status4/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeDto>> findByUserAndDateStatus4(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status3")  String status4
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandeAbsenceCongeDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriodeStatus4(start, end, email, status4).stream()
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
    void delete(@PathVariable("demand-id") Integer demandid) throws Exception {
        Optional<DemandeAbsenceConge> entite = repository.findById(demandid);
        if(entite.isPresent()) repository.deleteById(demandid);

        // Notification validateur de l'annulation
        DemandeAbsenceCongeDto dto = DemandeAbsenceCongeDto.fromEntity(entite.get());
        notificationService.sendAnnulationAbsCgeNotification(dto, dto.getValid1());
    }

}
