package com.kinart.api.portail.controller;

import com.kinart.api.portail.dto.*;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.repository.SalarieRepository;
import com.kinart.paie.business.services.utils.*;
import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.model.DemandePret;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
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
    private final RestTemplate restTemplate;
    private final SalarieRepository salarieRepository;
    private final GeneriqueConnexionService service;
    private final ParamDataRepository paramDataRepository;
    private final GeneriqueConnexionService generiqueConnexionService;

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/user", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAbsenceCongeResponse> saveUser(@RequestBody DemandeAbsenceCongeResponse dto){
        //validator.validate(dto);
        try {
            DemandeAbsenceCongeDto dto2 = new DemandeAbsenceCongeDto();
            //System.out.println("RAISON=============="+dto.getRaison());
            dto2.setMotif(dto.getMotif());
            dto2.setRaison(dto.getRaison());
            dto2.setDteFin(dto.getDteFin());
            dto2.setDteDebut(dto.getDteDebut());
            dto2.setNbAbs(dto.getNbAbs());
            dto2.setNbCg(dto.getNbCg());
            dto.setStrDateDebut(new ClsDate(dto.getDteDebut()).getDateS("dd/MM/yyyy"));
            dto.setStrDateFin(new ClsDate(dto.getDteFin()).getDateS("dd/MM/yyyy"));
            // Fixer les validateurs
            Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            if(user.isPresent()){
                dto2.setUserDemAbsCg(user.get());
                dto2.setValid1(user.get().getValid1());
                dto2.setValid2(user.get().getValid2());
                dto2.setValid3(user.get().getValid3());
                dto2.setValid4(user.get().getValid4());
            } else throw new EntityNotFoundException("Utilisateur inexistant");

            dto2.setStatus1(EnumStatusType.ATTENTE_VALIDATION);
            dto2.setStatus2(EnumStatusType.NONE);
            dto2.setStatus3(EnumStatusType.NONE);
            dto2.setStatus4(EnumStatusType.NONE);
            DemandeAbsenceConge entity = repository.save(DemandeAbsenceCongeDto.toEntity(dto2));
            if(entity != null){
                dto.setId(entity.getId());
                dto2.setId(entity.getId());
            }

            // Gestion des notifications
            notificationService.sendAbsenceCongeNotification(dto2, dto2.getValid1());

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid1", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAbsenceCongeResponse> saveValid1(@RequestBody DemandeAbsenceCongeResponse dto){
        //validator.validate(dto);
        try {
            Optional<DemandeAbsenceConge> dbDemande = repository.findById(dto.getId());
            DemandeAbsenceConge entity = dbDemande.isPresent()?dbDemande.get():new DemandeAbsenceConge();
            entity.setNbAbs(dto.getNbAbs());
            entity.setNbCg(dto.getNbCg());
            entity.setDteFin(dto.getDteFin());
            entity.setDteDebut(dto.getDteDebut());
            entity.setCommentN1(dto.getCommentN1());
            entity.setStatus1(EnumStatusType.valueOf(dto.getStatus1()));
            if(EnumStatusType.VALIDEE.getCode().equalsIgnoreCase(dto.getStatus1()))
                entity.setStatus2(EnumStatusType.ATTENTE_VALIDATION);
            repository.save(entity);

            //Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            Optional<Utilisateur> validatorPrec =  utilisateurRepository.findUtilisateurByEmail(entity.getValid1());
            Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entity.getValid2());
            DemandeAbsenceCongeDto dto2 = new DemandeAbsenceCongeDto();
            BeanUtils.copyProperties(entity, dto2);

                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus1().equals(EnumStatusType.VALIDEE.getCode())){
                    if(validator.isPresent())
                        notificationService.sendAbsCongeNotificationSender(dto2, validatorPrec.get().getPrenom()+ " "+validatorPrec.get().getNom());
                    notificationService.sendAbsenceCongeNotification(dto2, entity.getValid2());
                    if(StringUtil.isNoneEmpty(entity.getValid3()))
                        notificationService.sendAbsenceCongeNotification(dto2, entity.getValid2());
                    else { // Pas de validateur de niveau 4
                        insertEV(dto2);
                    }
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE.getCode()))// Sinon notification du sender du rejet
                    notificationService.sendAbsCongeNotificationRejet(dto2);

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid2", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAbsenceCongeResponse> saveValid2(@RequestBody DemandeAbsenceCongeResponse dto){
        //validator.validate(dto);
        try {
            Optional<DemandeAbsenceConge> dbDemande = repository.findById(dto.getId());
            DemandeAbsenceConge entity = dbDemande.isPresent()?dbDemande.get():new DemandeAbsenceConge();
            entity.setNbAbs(dto.getNbAbs());
            entity.setNbCg(dto.getNbCg());
            entity.setDteFin(dto.getDteFin());
            entity.setDteDebut(dto.getDteDebut());
            entity.setCommentN2(dto.getCommentN2());
            entity.setStatus2(EnumStatusType.valueOf(dto.getStatus2()));
            if(EnumStatusType.VALIDEE.getCode().equalsIgnoreCase(dto.getStatus2()))
                entity.setStatus3(EnumStatusType.ATTENTE_VALIDATION);
            repository.save(entity);

            //Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            Optional<Utilisateur> validatorPrec =  utilisateurRepository.findUtilisateurByEmail(entity.getValid2());
            Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entity.getValid3());
            DemandeAbsenceCongeDto dto2 = new DemandeAbsenceCongeDto();
            BeanUtils.copyProperties(entity, dto2);
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus2().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendAbsCongeNotificationSender(dto2, validatorPrec.get().getPrenom()+ " "+validatorPrec.get().getNom());
                    if(StringUtil.isNoneEmpty(entity.getValid3()))
                        notificationService.sendAbsenceCongeNotification(dto2, entity.getValid3());
                    else { // Pas de validateur de niveau 4
                        insertEV(dto2);
                    }
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendAbsCongeNotificationRejet(dto2);

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid3", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAbsenceCongeResponse> saveValid3(@RequestBody DemandeAbsenceCongeResponse dto){
        //validator.validate(dto);
        try {
            Optional<DemandeAbsenceConge> dbDemande = repository.findById(dto.getId());
            DemandeAbsenceConge entity = dbDemande.isPresent()?dbDemande.get():new DemandeAbsenceConge();
            entity.setNbAbs(dto.getNbAbs());
            entity.setNbCg(dto.getNbCg());
            entity.setDteFin(dto.getDteFin());
            entity.setDteDebut(dto.getDteDebut());
            entity.setCommentN3(dto.getCommentN3());
            entity.setStatus3(EnumStatusType.valueOf(dto.getStatus3()));
            if(EnumStatusType.VALIDEE.getCode().equalsIgnoreCase(dto.getStatus3()))
                entity.setStatus4(EnumStatusType.ATTENTE_VALIDATION);
            repository.save(entity);

            //Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            Optional<Utilisateur> validatorPrec =  utilisateurRepository.findUtilisateurByEmail(entity.getValid3());
            Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entity.getValid4());
            DemandeAbsenceCongeDto dto2 = new DemandeAbsenceCongeDto();
            BeanUtils.copyProperties(entity, dto2);
                            // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus3().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendAbsCongeNotificationSender(dto2, validatorPrec.get().getPrenom()+ " "+validatorPrec.get().getNom());
                    if(StringUtil.isNotEmpty(entity.getValid4()))
                        notificationService.sendAbsenceCongeNotification(dto2, entity.getValid4());
                    else { // Pas de validateur de niveau 4
                        insertEV(dto2);
                    }
                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendAbsCongeNotificationRejet(dto2);

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    /**
     *
     * @param dto
     */
    private void insertEV(DemandeAbsenceCongeDto dto) {
        // MAJ dans Amplitude RH
        Optional<Salarie> salDB =   salarieRepository.findByAdr4(dto.getUserDemAbsCg().getEmail());

        if(salDB.isPresent()){
            System.out.println("SALARIE CONCERNE="+salDB.get().getNmat());
            String host = "http://localhost:8082";
            String urlPost = host+"/amplituderh/v1/evabsenceconge";
            ElementVarCongeDto elementVarCongeDto = new ElementVarCongeDto();
            elementVarCongeDto.setCdos("1");
            elementVarCongeDto.setCuti("DELT");
            elementVarCongeDto.setNbul(9);
            elementVarCongeDto.setMotf(dto.getMotif());
            elementVarCongeDto.setNmat(salDB.get().getNmat());
            elementVarCongeDto.setAamm(new ClsDate(dto.getDteDebut()).getYearAndMonth());
            elementVarCongeDto.setDdeb(dto.getDteDebut());
            elementVarCongeDto.setDfin(dto.getDteFin());
            // Calcul des nombres de jour de congé et absence
//            ClsDateRhpcalendrier rhp = new ClsDateRhpcalendrier(service, paramDataRepository, "1", "dd/MM/yyyy");
//            int nbJrAbs = rhp.getNombreDeJoursAbsences(dto.getDteDebut(), dto.getDteFin(), TypeBDUtil.OR);
//            int nbJrCgs = rhp.getNombreDeJoursConges(dto.getDteDebut(), dto.getDteFin(), TypeBDUtil.OR);
            elementVarCongeDto.setNbjc(dto.getNbCg());
            elementVarCongeDto.setNbja(dto.getNbAbs());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            List<ElementVarCongeDto> listeElementsAbsConge = ClsDateRhpcalendrier.getAllElementAbsenceConge(service, paramDataRepository, elementVarCongeDto);
            System.out.println("NOMBRE EV="+listeElementsAbsConge.size());
            for(ElementVarCongeDto evDto : listeElementsAbsConge){
                evDto.setCdos("02");
                evDto.setFirstDay((new ClsDate(elementVarCongeDto.getAamm(), "yyyyMM")).getFirstDayOfMonth());
                evDto.setLastDay((new ClsDate(elementVarCongeDto.getAamm(), "yyyyMM")).getLastDayOfMonth());
                HttpEntity<ElementVarCongeDto> entity = new  HttpEntity<ElementVarCongeDto>(evDto,headers);
                restTemplate.exchange(urlPost, HttpMethod.POST, entity, ElementVarCongeDto.class);
            }

        } else throw new EntityNotFoundException("Aucun salarié correspondant a l'adresse mail de l'utilisateur");
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/valid4", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande absence congé", notes = "Cette methode permet d'enregistrer des demandes absence congé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAbsenceCongeResponse> saveValid4(@RequestBody DemandeAbsenceCongeResponse dto){
        //validator.validate(dto);
        try {
            Optional<DemandeAbsenceConge> dbDemande = repository.findById(dto.getId());
            DemandeAbsenceConge entity = dbDemande.isPresent()?dbDemande.get():new DemandeAbsenceConge();
            entity.setNbAbs(dto.getNbAbs());
            entity.setNbCg(dto.getNbCg());
            entity.setDteFin(dto.getDteFin());
            entity.setDteDebut(dto.getDteDebut());
            entity.setCommentN4(dto.getCommentN4());
            entity.setStatus4(EnumStatusType.valueOf(dto.getStatus4()));
            repository.save(entity);

            //Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            if(StringUtils.isEmpty(dto.getIsDrhl()) || "N".equalsIgnoreCase(dto.getIsDrhl())){
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entity.getValid4());
                DemandeAbsenceCongeDto dto2 = new DemandeAbsenceCongeDto();
                BeanUtils.copyProperties(entity, dto2);
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus4().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendAbsCongeNotificationSender(dto2, validator.get().getPrenom()+ " "+validator.get().getNom());
                    // MAJ dans Amplitude RH
//                    insertEV(dto2);

                } else if(dto.getStatus1().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendAbsCongeNotificationRejet(dto2);
            } else if("O".equalsIgnoreCase(dto.getIsDrhl())){
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entity.getValid4());
                DemandeAbsenceCongeDto dto2 = new DemandeAbsenceCongeDto();
                BeanUtils.copyProperties(entity, dto2);
                if(dto.getStatus4().equals(EnumStatusType.VALIDEE)){
                    // MAJ dans Amplitude RH
                    insertEV(dto2);

                }
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
        List<DemandeAbsenceCongeDto> demandeAbsenceConges = repository.searchByEmailAndStatus(email, EnumStatusType.ATTENTE_VALIDATION.getCode()).stream()
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
    ResponseEntity<List<DemandeAbsenceCongeResponse>> findByUserAndDate(
          @PathVariable("email") String email,
          @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
          @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.valid1, n.status1, u1.nom, u1.prenom, u1.email, n.motif "+
                ", n.raison, n.dteDebut, n.dteFin, n.valid2, n.valid3, n.valid4, n.status2, n.status3, n.status4, m1.vall, n.comment_n1, n.comment_n2, n.comment_n3, n.comment_n4, n.nbAbs, n.nbCg  "+
                "FROM demandeabsenceconge n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.motif AND m1.ctab=22 and m1.nume=1 "+
                "WHERE u1.email=:email AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandeAbsenceCongeResponse> liste = new ArrayList<DemandeAbsenceCongeResponse>();
        for (Object[] o : lst)
        {
            DemandeAbsenceCongeResponse cptble = new DemandeAbsenceCongeResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setValid1(o[4].toString());
            if(o[5]!=null) cptble.setStatus1(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setMotif(o[9].toString());
            if(o[10]!=null) cptble.setRaison(o[10].toString());
            if(o[11]!=null) cptble.setDteDebut((Date) o[11]);
            if(o[12]!=null) cptble.setDteFin((Date) o[12]);
            if(o[13]!=null) cptble.setValid2(o[13].toString());
            if(o[14]!=null) cptble.setValid3(o[14].toString());
            if(o[15]!=null) cptble.setValid4(o[15].toString());
            if(o[16]!=null) cptble.setStatus2(o[16].toString());
            if(o[17]!=null) cptble.setStatus3(o[17].toString());
            if(o[18]!=null) cptble.setStatus4(o[18].toString());
            if(o[19]!=null) cptble.setLibmotif(o[19].toString());
            if(o[20]!=null) cptble.setCommentN1(o[20].toString());
            if(o[21]!=null) cptble.setCommentN2(o[21].toString());
            if(o[22]!=null) cptble.setCommentN3(o[22].toString());
            if(o[23]!=null) cptble.setCommentN4(o[23].toString());

            if(o[24]!=null) cptble.setNbAbs(new BigDecimal(o[24].toString()));
            if(o[25]!=null) cptble.setNbCg(new BigDecimal(o[25].toString()));

            cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));
            cptble.setStrDateDebut(new ClsDate(cptble.getDteDebut()).getDateS("dd/MM/yyyy"));
            cptble.setStrDateFin(new ClsDate(cptble.getDteFin()).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/status1/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeResponse>> findByUserAndDateStatus1(
            @PathVariable("email") String email,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status1")  String status1
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.valid1, n.status1, u1.nom, u1.prenom, u1.email, n.motif "+
                ", n.raison, n.dteDebut, n.dteFin, n.valid2, n.valid3, n.valid4, n.status2, n.status3, n.status4, m1.vall, n.comment_n1, n.comment_n2, n.comment_n3, n.comment_n4, n.nbAbs, n.nbCg  "+
                "FROM demandeabsenceconge n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.motif AND m1.ctab=22 and m1.nume=1 "+
                "WHERE (u1.email=:email OR n.valid1=:email) AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandeAbsenceCongeResponse> liste = new ArrayList<DemandeAbsenceCongeResponse>();
        for (Object[] o : lst)
        {
            DemandeAbsenceCongeResponse cptble = new DemandeAbsenceCongeResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setValid1(o[4].toString());
            if(o[5]!=null) cptble.setStatus1(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setMotif(o[9].toString());
            if(o[10]!=null) cptble.setRaison(o[10].toString());
            if(o[11]!=null) cptble.setDteDebut((Date) o[11]);
            if(o[12]!=null) cptble.setDteFin((Date) o[12]);
            if(o[13]!=null) cptble.setValid2(o[13].toString());
            if(o[14]!=null) cptble.setValid3(o[14].toString());
            if(o[15]!=null) cptble.setValid4(o[15].toString());
            if(o[16]!=null) cptble.setStatus2(o[16].toString());
            if(o[17]!=null) cptble.setStatus3(o[17].toString());
            if(o[18]!=null) cptble.setStatus4(o[18].toString());
            if(o[19]!=null) cptble.setLibmotif(o[19].toString());
            if(o[20]!=null) cptble.setCommentN1(o[20].toString());
            if(o[21]!=null) cptble.setCommentN2(o[21].toString());
            if(o[22]!=null) cptble.setCommentN3(o[22].toString());
            if(o[23]!=null) cptble.setCommentN4(o[23].toString());
            if(o[24]!=null) cptble.setNbAbs(new BigDecimal(o[24].toString()));
            if(o[25]!=null) cptble.setNbCg(new BigDecimal(o[25].toString()));

            cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));
            cptble.setStrDateDebut(new ClsDate(cptble.getDteDebut()).getDateS("dd/MM/yyyy"));
            cptble.setStrDateFin(new ClsDate(cptble.getDteFin()).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/status2/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeResponse>> findByUserAndDateStatus2(
            @PathVariable("email") String email,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status2")  String status2
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.valid1, n.status1, u1.nom, u1.prenom, u1.email, n.motif "+
                ", n.raison, n.dteDebut, n.dteFin, n.valid2, n.valid3, n.valid4, n.status2, n.status3, n.status4, m1.vall, n.comment_n1, n.comment_n2, n.comment_n3, n.comment_n4, n.nbAbs, n.nbCg  "+
                "FROM demandeabsenceconge n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.motif AND m1.ctab=22 and m1.nume=1 "+
                "WHERE (u1.email=:email OR n.valid2=:email) AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandeAbsenceCongeResponse> liste = new ArrayList<DemandeAbsenceCongeResponse>();
        for (Object[] o : lst)
        {
            DemandeAbsenceCongeResponse cptble = new DemandeAbsenceCongeResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setValid1(o[4].toString());
            if(o[5]!=null) cptble.setStatus1(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setMotif(o[9].toString());
            if(o[10]!=null) cptble.setRaison(o[10].toString());
            if(o[11]!=null) cptble.setDteDebut((Date) o[11]);
            if(o[12]!=null) cptble.setDteFin((Date) o[12]);
            if(o[13]!=null) cptble.setValid2(o[13].toString());
            if(o[14]!=null) cptble.setValid3(o[14].toString());
            if(o[15]!=null) cptble.setValid4(o[15].toString());
            if(o[16]!=null) cptble.setStatus2(o[16].toString());
            if(o[17]!=null) cptble.setStatus3(o[17].toString());
            if(o[18]!=null) cptble.setStatus4(o[18].toString());
            if(o[19]!=null) cptble.setLibmotif(o[19].toString());
            if(o[20]!=null) cptble.setCommentN1(o[20].toString());
            if(o[21]!=null) cptble.setCommentN2(o[21].toString());
            if(o[22]!=null) cptble.setCommentN3(o[22].toString());
            if(o[23]!=null) cptble.setCommentN4(o[23].toString());
            if(o[24]!=null) cptble.setNbAbs(new BigDecimal(o[24].toString()));
            if(o[25]!=null) cptble.setNbCg(new BigDecimal(o[25].toString()));

            cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));
            cptble.setStrDateDebut(new ClsDate(cptble.getDteDebut()).getDateS("dd/MM/yyyy"));
            cptble.setStrDateFin(new ClsDate(cptble.getDteFin()).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/status3/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeResponse>> findByUserAndDateStatus3(
            @PathVariable("email") String email,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status3")  String status3
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.valid1, n.status1, u1.nom, u1.prenom, u1.email, n.motif "+
                ", n.raison, n.dteDebut, n.dteFin, n.valid2, n.valid3, n.valid4, n.status2, n.status3, n.status4, m1.vall, n.comment_n1, n.comment_n2, n.comment_n3, n.comment_n4, n.nbAbs, n.nbCg  "+
                "FROM demandeabsenceconge n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.motif AND m1.ctab=22 and m1.nume=1 "+
                "WHERE (u1.email=:email OR n.valid3=:email) AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandeAbsenceCongeResponse> liste = new ArrayList<DemandeAbsenceCongeResponse>();
        for (Object[] o : lst)
        {
            DemandeAbsenceCongeResponse cptble = new DemandeAbsenceCongeResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setValid1(o[4].toString());
            if(o[5]!=null) cptble.setStatus1(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setMotif(o[9].toString());
            if(o[10]!=null) cptble.setRaison(o[10].toString());
            if(o[11]!=null) cptble.setDteDebut((Date) o[11]);
            if(o[12]!=null) cptble.setDteFin((Date) o[12]);
            if(o[13]!=null) cptble.setValid2(o[13].toString());
            if(o[14]!=null) cptble.setValid3(o[14].toString());
            if(o[15]!=null) cptble.setValid4(o[15].toString());
            if(o[16]!=null) cptble.setStatus2(o[16].toString());
            if(o[17]!=null) cptble.setStatus3(o[17].toString());
            if(o[18]!=null) cptble.setStatus4(o[18].toString());
            if(o[19]!=null) cptble.setLibmotif(o[19].toString());
            if(o[20]!=null) cptble.setCommentN1(o[20].toString());
            if(o[21]!=null) cptble.setCommentN2(o[21].toString());
            if(o[22]!=null) cptble.setCommentN3(o[22].toString());
            if(o[23]!=null) cptble.setCommentN4(o[23].toString());
            if(o[24]!=null) cptble.setNbAbs(new BigDecimal(o[24].toString()));
            if(o[25]!=null) cptble.setNbCg(new BigDecimal(o[25].toString()));

            cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));
            cptble.setStrDateDebut(new ClsDate(cptble.getDteDebut()).getDateS("dd/MM/yyyy"));
            cptble.setStrDateFin(new ClsDate(cptble.getDteFin()).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/status4/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAbsenceCongeDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAbsenceCongeResponse>> findByUserAndDateStatus4(
            @PathVariable("email") String email,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status3")  String status4
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.valid1, n.status1, u1.nom, u1.prenom, u1.email, n.motif "+
                ", n.raison, n.dteDebut, n.dteFin, n.valid2, n.valid3, n.valid4, n.status2, n.status3, n.status4, m1.vall, n.comment_n1, n.comment_n2, n.comment_n3, n.comment_n4, n.nbAbs, n.nbCg  "+
                "FROM demandeabsenceconge n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.motif AND m1.ctab=22 and m1.nume=1 "+
                "WHERE (u1.email=:email OR n.valid4=:email) AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        // Check if user is DRHL
        if(StringUtils.isNotEmpty(status4) && status4.equalsIgnoreCase("DRHL"))
            query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.valid1, n.status1, u1.nom, u1.prenom, u1.email, n.motif "+
                    ", n.raison, n.dteDebut, n.dteFin, n.valid2, n.valid3, n.valid4, n.status2, n.status3, n.status4, m1.vall, n.comment_n1, n.comment_n2, n.comment_n3, n.comment_n4  "+
                    "FROM demandeabsenceconge n "+
                    "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                    "LEFT JOIN paramdata m1 ON m1.identreprise=n.identreprise AND m1.cacc=n.motif AND m1.ctab=22 and m1.nume=1 "+
                    "WHERE n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        if(StringUtils.isEmpty(status4) || !status4.equalsIgnoreCase("DRHL"))
            q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandeAbsenceCongeResponse> liste = new ArrayList<DemandeAbsenceCongeResponse>();
        for (Object[] o : lst)
        {
            DemandeAbsenceCongeResponse cptble = new DemandeAbsenceCongeResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setValid1(o[4].toString());
            if(o[5]!=null) cptble.setStatus1(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setMotif(o[9].toString());
            if(o[10]!=null) cptble.setRaison(o[10].toString());
            if(o[11]!=null) cptble.setDteDebut((Date) o[11]);
            if(o[12]!=null) cptble.setDteFin((Date) o[12]);
            if(o[13]!=null) cptble.setValid2(o[13].toString());
            if(o[14]!=null) cptble.setValid3(o[14].toString());
            if(o[15]!=null) cptble.setValid4(o[15].toString());
            if(o[16]!=null) cptble.setStatus2(o[16].toString());
            if(o[17]!=null) cptble.setStatus3(o[17].toString());
            if(o[18]!=null) cptble.setStatus4(o[18].toString());
            if(o[19]!=null) cptble.setLibmotif(o[19].toString());
            if(o[20]!=null) cptble.setCommentN1(o[20].toString());
            if(o[21]!=null) cptble.setCommentN2(o[21].toString());
            if(o[22]!=null) cptble.setCommentN3(o[22].toString());
            if(o[23]!=null) cptble.setCommentN4(o[23].toString());
            if(o[24]!=null) cptble.setNbAbs(new BigDecimal(o[24].toString()));
            if(o[25]!=null) cptble.setNbCg(new BigDecimal(o[25].toString()));

            cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));
            cptble.setStrDateDebut(new ClsDate(cptble.getDteDebut()).getDateS("dd/MM/yyyy"));
            cptble.setStrDateFin(new ClsDate(cptble.getDteFin()).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @DeleteMapping(value = APP_ROOT_PORTAIL + "/demande/absconge/{demandid}")
    @ApiOperation(value = "Supprimer une demande pas encore validée", notes = "Cette methode permet de supprimer une demande pas encore validée")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    ResponseEntity<Void> delete(@PathVariable("demandid") Integer demandid) throws Exception {
        Optional<DemandeAbsenceConge> entite = repository.findById(demandid);
        if(entite.isPresent()) repository.deleteById(demandid);

        // Notification validateur de l'annulation
        DemandeAbsenceCongeDto dto = DemandeAbsenceCongeDto.fromEntity(entite.get());
        notificationService.sendAnnulationAbsCgeNotification(dto, dto.getValid1());

        return ResponseEntity.accepted().build();
    }

}
