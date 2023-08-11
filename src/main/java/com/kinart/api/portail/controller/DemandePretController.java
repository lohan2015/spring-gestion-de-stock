package com.kinart.api.portail.controller;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.portail.dto.DemandeAttestationDto;
import com.kinart.api.portail.dto.DemandeAttestationResponse;
import com.kinart.api.portail.dto.DemandePretDto;
import com.kinart.api.portail.dto.DemandePretResponse;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
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
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PORTAIL;

@Api("demande-pret")
@RestController
@RequiredArgsConstructor
public class DemandePretController {

    private final ObjectsValidator<DemandePretResponse> validator;
    private final UtilisateurRepository utilisateurRepository;
    private final DemandePretRepository repository;
    private final NotificationPretService notificationService;
    private final ParamDataRepository paramDataRepository;
    private final GeneriqueConnexionService generiqueConnexionService;

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/pret/user", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte demande de prêt", notes = "Cette methode permet d'enregistrer des demandes de prêt")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandePretResponse> saveUser(@RequestBody DemandePretResponse dto){
        //validator.validate(dto);
        System.out.println("LECTURE PARAMETER........................");
        try {
            // Fixer les validateurs
            DemandePretDto dto2 = new DemandePretDto();
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
            else dto2.setScePersonnel(fnom.getVall());
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
            else dto2.setDrhl(fnom.getVall());
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
            else dto2.setDga(fnom.getVall());
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
            else dto2.setDg(fnom.getVall());

    //System.out.println("Fixation valeur........................");
            dto2.setMontantPret(dto.getMontantPret());
            dto2.setDteDebutPret(dto.getDteDebutPret());
            dto2.setTypePret(dto.getTypePret());
            dto2.setDureePret(dto.getDureePret());
            dto2.setCommentUser(dto.getCommentUser());
            dto.setDateFin(new ClsDate(dto.getDteDebutPret()).getDateS("dd/MM/yyyy"));
            System.out.println("Fin Fixation valeur........................"+dto.getDteDebutPret());

            Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            if(user.isPresent()){
                dto2.setUserDemPret(user.get());
            } else throw new EntityNotFoundException("Utilisateur inexistant");


            dto2.setStatus1(EnumStatusType.ATTENTE_VALIDATION);
            dto2.setStatus2(EnumStatusType.NONE);
            dto2.setStatus3(EnumStatusType.NONE);
            dto2.setStatus4(EnumStatusType.NONE);
            DemandePret entity = repository.save(DemandePretDto.toEntity(dto2));
            if(entity != null){
                dto.setId(entity.getId());
                dto2.setId(entity.getId());
            }


            // Gestion des notifications
            notificationService.sendPretNotification(dto2, dto2.getScePersonnel());

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/pret/scepersonnel", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandePretResponse> saveValidScePersonnel(@RequestBody DemandePretResponse dto){
        //validator.validate(dto);
        try {
            Optional<DemandePret> dbDemande = repository.findById(dto.getId());
            DemandePret entity = dbDemande.isPresent()?dbDemande.get():new DemandePret();
            entity.setCommentScepers(dto.getCommentScepers());
            entity.setStatus1(EnumStatusType.valueOf(dto.getStatus1()));
            if(EnumStatusType.VALIDEE.getCode().equalsIgnoreCase(dto.getStatus1()))
                entity.setStatus2(EnumStatusType.ATTENTE_VALIDATION);
            repository.save(entity);

            //Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            Optional<Utilisateur> validatorPrec =  utilisateurRepository.findUtilisateurByEmail(entity.getScePersonnel());
            Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entity.getDrhl());
            DemandePretDto dto2 = new DemandePretDto();
            BeanUtils.copyProperties(entity, dto2);

            /*if(user.isPresent()){
                dto2.setUserDemPret(user.get());
            } else throw new EntityNotFoundException("Utilisateur inexistant");*/

                 // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE.getCode())){
                    if(validator.isPresent())
                        notificationService.sendPretNotificationSender(dto2, validatorPrec.get().getPrenom()+ " "+validatorPrec.get().getNom());
                    notificationService.sendPretNotification(dto2, entity.getDrhl());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE.getCode()))// Sinon notification du sender du rejet
                    notificationService.sendPretNotificationRejet(dto2);

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/pret/drhl", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandePretResponse> saveValidDrhl(@RequestBody DemandePretResponse dto){
        //validator.validate(dto);
        try {
            Optional<DemandePret> dbDemande = repository.findById(dto.getId());
            DemandePret entity = dbDemande.isPresent()?dbDemande.get():new DemandePret();
            entity.setCommentDrhl(dto.getCommentDrhl());
            entity.setStatus2(EnumStatusType.valueOf(dto.getStatus2()));
            if(EnumStatusType.VALIDEE.getCode().equalsIgnoreCase(dto.getStatus2()))
                entity.setStatus3(EnumStatusType.ATTENTE_VALIDATION);
            repository.save(entity);

            //Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            Optional<Utilisateur> validatorPrec =  utilisateurRepository.findUtilisateurByEmail(entity.getDrhl());
            Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entity.getDga());
            DemandePretDto dto2 = new DemandePretDto();
            BeanUtils.copyProperties(entity, dto2);

            // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus2().equals(EnumStatusType.VALIDEE.getCode())){
                    if(validator.isPresent())
                        notificationService.sendPretNotificationSender(dto2, validatorPrec.get().getPrenom()+ " "+validatorPrec.get().getNom());
                    notificationService.sendPretNotification(dto2, entity.getDga());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE.getCode()))// Sinon notification du sender du rejet
                    notificationService.sendPretNotificationRejet(dto2);

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/pret/dga", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandePretResponse> saveValidDga(@RequestBody DemandePretResponse dto){
        //validator.validate(dto);
        try {
            Optional<DemandePret> dbDemande = repository.findById(dto.getId());
            DemandePret entity = dbDemande.isPresent()?dbDemande.get():new DemandePret();
            entity.setCommentDga(dto.getCommentDga());
            entity.setStatus3(EnumStatusType.valueOf(dto.getStatus3()));
            if(EnumStatusType.VALIDEE.getCode().equalsIgnoreCase(dto.getStatus3()))
                entity.setStatus4(EnumStatusType.ATTENTE_VALIDATION);
            repository.save(entity);

            //Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            Optional<Utilisateur> validatorPrec =  utilisateurRepository.findUtilisateurByEmail(entity.getDga());
            Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entity.getDg());
            DemandePretDto dto2 = new DemandePretDto();
            BeanUtils.copyProperties(entity, dto2);

            // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus3().equals(EnumStatusType.VALIDEE.getCode())){
                    if(validator.isPresent())
                        notificationService.sendPretNotificationSender(dto2, validatorPrec.get().getPrenom()+ " "+validatorPrec.get().getNom());
                    notificationService.sendPretNotification(dto2, entity.getDg());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE.getCode()))// Sinon notification du sender du rejet
                    notificationService.sendPretNotificationRejet(dto2);

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/pret/valid4", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandePretResponse> saveValid4(@RequestBody DemandePretResponse dto){
        //validator.validate(dto);
        try {
            Optional<DemandePret> dbDemande = repository.findById(dto.getId());
            DemandePret entity = dbDemande.isPresent()?dbDemande.get():new DemandePret();
            entity.setCommentDg(dto.getCommentDg());
            entity.setStatus4(EnumStatusType.valueOf(dto.getStatus4()));
            repository.save(entity);

            //Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entity.getDg());
            DemandePretDto dto2 = new DemandePretDto();
            BeanUtils.copyProperties(entity, dto2);

               // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus4().equals(EnumStatusType.VALIDEE.getCode())){
                    if(validator.isPresent())
                        notificationService.sendPretNotificationSender(dto2, validator.get().getPrenom()+ " "+validator.get().getNom());
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE.getCode()))// Sinon notification du sender du rejet
                    notificationService.sendPretNotificationRejet(dto2);

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/{email}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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
        List<DemandePretDto> demandeAbsenceConges = repository.searchByEmailAndStatus(email, EnumStatusType.ATTENTE_VALIDATION.getCode()).stream()
                                                                        .map(DemandePretDto::fromEntity)
                                                                        .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/listbydate/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretResponse>> findByUserAndDate(
          @PathVariable("email") String email,
          @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
          @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.scepersonnel, n.status1, u1.nom, u1.prenom, u1.email, n.typepret "+
                ", n.montantpret, n.dureepret, n.drhl, n.dga, n.dg, n.status2, n.status3, n.status4, n.dtedebutpret, n.comment_user, n.comment_scrpers, n.comment_drhl, n.comment_dga, n.comment_dg, m1.vall  "+
                "FROM demandepret n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.typepret AND m1.ctab=802 and m1.nume=1 "+
                "WHERE u1.email=:email AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandePretResponse> liste = new ArrayList<DemandePretResponse>();
        for (Object[] o : lst)
        {
            DemandePretResponse cptble = new DemandePretResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setScePersonnel(o[4].toString());
            if(o[5]!=null) cptble.setStatus1(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setTypePret(o[9].toString());
            if(o[10]!=null) cptble.setMontantPret(new BigDecimal(o[10].toString()));
            if(o[11]!=null) cptble.setDureePret(Integer.parseInt(o[11].toString()));
            if(o[12]!=null) cptble.setDrhl(o[12].toString());
            if(o[13]!=null) cptble.setDga(o[13].toString());
            if(o[14]!=null) cptble.setDg(o[14].toString());
            if(o[15]!=null) cptble.setStatus2(o[15].toString());
            if(o[16]!=null) cptble.setStatus3(o[16].toString());
            if(o[17]!=null) cptble.setStatus4(o[17].toString());
            if(o[18]!=null){
                cptble.setDteDebutPret((Date) o[18]);
                cptble.setDateFin(new ClsDate((Date) o[18]).getDateS("dd/MM/yyyy"));
            }
            if(o[19]!=null) cptble.setCommentUser(o[19].toString());
            if(o[20]!=null) cptble.setCommentScepers(o[20].toString());
            if(o[21]!=null) cptble.setCommentDrhl(o[21].toString());
            if(o[22]!=null) cptble.setCommentDga(o[22].toString());
            if(o[23]!=null) cptble.setCommentDg(o[23].toString());
            if(o[24]!=null) cptble.setLibTypePret(o[24].toString());

           cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/status1/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretResponse>> findByUserAndDateStatus1(
            @PathVariable("email") String email,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status1")  String status1
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.scepersonnel, n.status1, u1.nom, u1.prenom, u1.email, n.typepret "+
                ", n.montantpret, n.dureepret, n.drhl, n.dga, n.dg, n.status2, n.status3, n.status4, n.dtedebutpret, n.comment_user, n.comment_scrpers, n.comment_drhl, n.comment_dga, n.comment_dg, m1.vall  "+
                "FROM demandepret n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.typepret AND m1.ctab=802 and m1.nume=1 "+
                "WHERE (u1.email=:email OR n.scepersonnel=:email) AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandePretResponse> liste = new ArrayList<DemandePretResponse>();
        for (Object[] o : lst)
        {
            DemandePretResponse cptble = new DemandePretResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setScePersonnel(o[4].toString());
            if(o[5]!=null) cptble.setStatus1(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setTypePret(o[9].toString());
            if(o[10]!=null) cptble.setMontantPret(new BigDecimal(o[10].toString()));
            if(o[11]!=null) cptble.setDureePret(Integer.parseInt(o[11].toString()));
            if(o[12]!=null) cptble.setDrhl(o[12].toString());
            if(o[13]!=null) cptble.setDga(o[13].toString());
            if(o[14]!=null) cptble.setDg(o[14].toString());
            if(o[15]!=null) cptble.setStatus2(o[15].toString());
            if(o[16]!=null) cptble.setStatus3(o[16].toString());
            if(o[17]!=null) cptble.setStatus4(o[17].toString());
            if(o[18]!=null){
                cptble.setDteDebutPret((Date) o[18]);
                cptble.setDateFin(new ClsDate((Date) o[18]).getDateS("dd/MM/yyyy"));
            }
            if(o[19]!=null) cptble.setCommentUser(o[19].toString());
            if(o[20]!=null) cptble.setCommentScepers(o[20].toString());
            if(o[21]!=null) cptble.setCommentDrhl(o[21].toString());
            if(o[22]!=null) cptble.setCommentDga(o[22].toString());
            if(o[23]!=null) cptble.setCommentDg(o[23].toString());
            if(o[24]!=null) cptble.setLibTypePret(o[24].toString());

            cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/status2/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretResponse>> findByUserAndDateStatus2(
            @PathVariable("email") String email,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status2")  String status2
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.scepersonnel, n.status1, u1.nom, u1.prenom, u1.email, n.typepret "+
                ", n.montantpret, n.dureepret, n.drhl, n.dga, n.dg, n.status2, n.status3, n.status4, n.dtedebutpret, n.comment_user, n.comment_scrpers, n.comment_drhl, n.comment_dga, n.comment_dg, m1.vall  "+
                "FROM demandepret n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.typepret AND m1.ctab=802 and m1.nume=1 "+
                "WHERE (u1.email=:email OR n.drhl=:email) AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Session session = generiqueConnexionService.getSession();
        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandePretResponse> liste = new ArrayList<DemandePretResponse>();
        for (Object[] o : lst)
        {
            DemandePretResponse cptble = new DemandePretResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setScePersonnel(o[4].toString());
            if(o[5]!=null) cptble.setStatus1(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setTypePret(o[9].toString());
            if(o[10]!=null) cptble.setMontantPret(new BigDecimal(o[10].toString()));
            if(o[11]!=null) cptble.setDureePret(Integer.parseInt(o[11].toString()));
            if(o[12]!=null) cptble.setDrhl(o[12].toString());
            if(o[13]!=null) cptble.setDga(o[13].toString());
            if(o[14]!=null) cptble.setDg(o[14].toString());
            if(o[15]!=null) cptble.setStatus2(o[15].toString());
            if(o[16]!=null) cptble.setStatus3(o[16].toString());
            if(o[17]!=null) cptble.setStatus4(o[17].toString());
            if(o[18]!=null){
                cptble.setDteDebutPret((Date) o[18]);
                cptble.setDateFin(new ClsDate((Date) o[18]).getDateS("dd/MM/yyyy"));
            }
            if(o[19]!=null) cptble.setCommentUser(o[19].toString());
            if(o[20]!=null) cptble.setCommentScepers(o[20].toString());
            if(o[21]!=null) cptble.setCommentDrhl(o[21].toString());
            if(o[22]!=null) cptble.setCommentDga(o[22].toString());
            if(o[23]!=null) cptble.setCommentDg(o[23].toString());
            if(o[24]!=null) cptble.setLibTypePret(o[24].toString());

            cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/status3/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretResponse>> findByUserAndDateStatus3(
            @PathVariable("email") String email,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status3")  String status3
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.scepersonnel, n.status1, u1.nom, u1.prenom, u1.email, n.typepret "+
                ", n.montantpret, n.dureepret, n.drhl, n.dga, n.dg, n.status2, n.status3, n.status4, n.dtedebutpret, n.comment_user, n.comment_scrpers, n.comment_drhl, n.comment_dga, n.comment_dg, m1.vall  "+
                "FROM demandepret n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.typepret AND m1.ctab=802 and m1.nume=1 "+
                "WHERE (u1.email=:email OR n.dga=:email) AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Session session = generiqueConnexionService.getSession();
        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandePretResponse> liste = new ArrayList<DemandePretResponse>();
        for (Object[] o : lst)
        {
            DemandePretResponse cptble = new DemandePretResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setScePersonnel(o[4].toString());
            if(o[5]!=null) cptble.setStatus1(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setTypePret(o[9].toString());
            if(o[10]!=null) cptble.setMontantPret(new BigDecimal(o[10].toString()));
            if(o[11]!=null) cptble.setDureePret(Integer.parseInt(o[11].toString()));
            if(o[12]!=null) cptble.setDrhl(o[12].toString());
            if(o[13]!=null) cptble.setDga(o[13].toString());
            if(o[14]!=null) cptble.setDg(o[14].toString());
            if(o[15]!=null) cptble.setStatus2(o[15].toString());
            if(o[16]!=null) cptble.setStatus3(o[16].toString());
            if(o[17]!=null) cptble.setStatus4(o[17].toString());
            if(o[18]!=null){
                cptble.setDteDebutPret((Date) o[18]);
                cptble.setDateFin(new ClsDate((Date) o[18]).getDateS("dd/MM/yyyy"));
            }
            if(o[19]!=null) cptble.setCommentUser(o[19].toString());
            if(o[20]!=null) cptble.setCommentScepers(o[20].toString());
            if(o[21]!=null) cptble.setCommentDrhl(o[21].toString());
            if(o[22]!=null) cptble.setCommentDga(o[22].toString());
            if(o[23]!=null) cptble.setCommentDg(o[23].toString());
            if(o[24]!=null) cptble.setLibTypePret(o[24].toString());

            cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/pret/status4/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandePretResponse>> findByUserAndDateStatus4(
            @PathVariable("email") String email,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status3")  String status4
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.scepersonnel, n.status1, u1.nom, u1.prenom, u1.email, n.typepret "+
                ", n.montantpret, n.dureepret, n.drhl, n.dga, n.dg, n.status2, n.status3, n.status4, n.dtedebutpret, n.comment_user, n.comment_scrpers, n.comment_drhl, n.comment_dga, n.comment_dg, m1.vall  "+
                "FROM demandepret n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.typepret AND m1.ctab=802 and m1.nume=1 "+
                "WHERE (u1.email=:email OR n.dg=:email) AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Session session = generiqueConnexionService.getSession();
        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandePretResponse> liste = new ArrayList<DemandePretResponse>();
        for (Object[] o : lst)
        {
            DemandePretResponse cptble = new DemandePretResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setScePersonnel(o[4].toString());
            if(o[5]!=null) cptble.setStatus1(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setTypePret(o[9].toString());
            if(o[10]!=null) cptble.setMontantPret(new BigDecimal(o[10].toString()));
            if(o[11]!=null) cptble.setDureePret(Integer.parseInt(o[11].toString()));
            if(o[12]!=null) cptble.setDrhl(o[12].toString());
            if(o[13]!=null) cptble.setDga(o[13].toString());
            if(o[14]!=null) cptble.setDg(o[14].toString());
            if(o[15]!=null) cptble.setStatus2(o[15].toString());
            if(o[16]!=null) cptble.setStatus3(o[16].toString());
            if(o[17]!=null) cptble.setStatus4(o[17].toString());
            if(o[18]!=null){
                cptble.setDteDebutPret((Date) o[18]);
                cptble.setDateFin(new ClsDate((Date) o[18]).getDateS("dd/MM/yyyy"));
            }
            if(o[19]!=null) cptble.setCommentUser(o[19].toString());
            if(o[20]!=null) cptble.setCommentScepers(o[20].toString());
            if(o[21]!=null) cptble.setCommentDrhl(o[21].toString());
            if(o[22]!=null) cptble.setCommentDga(o[22].toString());
            if(o[23]!=null) cptble.setCommentDg(o[23].toString());
            if(o[24]!=null) cptble.setLibTypePret(o[24].toString());

            cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @DeleteMapping(value = APP_ROOT_PORTAIL + "/demande/pret/{demandid}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Supprimer une demande pas encore validée", notes = "Cette methode permet de supprimer une demande pas encore validée")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    ResponseEntity<Void> delete(@PathVariable("demandid") Integer demandid) throws Exception {
        Optional<DemandePret> entite = repository.findById(demandid);
        if(entite.isPresent()) repository.deleteById(demandid);

        // Notification validateur de l'annulation
        DemandePretDto dto = DemandePretDto.fromEntity(entite.get());
        notificationService.sendAnnulationPretNotification(dto, dto.getScePersonnel());

        return ResponseEntity.accepted().build();
    }

}
