package com.kinart.api.portail.controller;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.portail.dto.DemandeAttestationDto;
import com.kinart.api.portail.dto.DemandeAttestationResponse;
import com.kinart.api.portail.dto.DemandeModifInfoDto;
import com.kinart.api.portail.dto.DemandeModifInfoResponse;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
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
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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
    private final GeneriqueConnexionService generiqueConnexionService;

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/attestation/user", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte demande attestation", notes = "Cette methode permet d'enregistrer des demandes attestation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAttestationResponse> saveUser(@RequestBody DemandeAttestationResponse dto){
        //validator.validate(dto);
        System.out.println("Ok allons y..............................");

        try {
            DemandeAttestationDto dto2 = new DemandeAttestationDto();
            dto2.setTypeDoc(dto.getTypeDoc());
            // Get info user
            Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            if(user.isPresent()){
                dto2.setUserDemAttest(user.get());
                /*dto.getUserDemAttest().setEmail(dto.getEmail());
                dto.getUserDemAttest().setNom(user.get().getNom());
                dto.getUserDemAttest().setPrenom(user.get().getPrenom());*/
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
            dto2.setScePersonnel(emailValidator);
            dto2.setStatus(EnumStatusType.ATTENTE_VALIDATION);
            DemandeAttestation entity = repository.save(DemandeAttestationDto.toEntity(dto2));
            if(entity != null) dto.setId(entity.getId());

            // Gestion des notifications
            notificationService.sendAttestationNotification(dto2, dto2.getScePersonnel());

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/attestation/valid", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande attestation", notes = "Cette methode permet d'enregistrer des demandes attestation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAttestationResponse> saveValid(@RequestBody DemandeAttestationResponse dto){
        //validator.validate(dto);
        try {

            repository.updateDemande(dto.getId(), dto.getStatus());

            Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
            Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(dto.getScePersonnel());
            DemandeAttestationDto dto2 = new DemandeAttestationDto();
            BeanUtils.copyProperties(dto, dto2);
            dto2.setCreationDate(new ClsDate(dto.getValueDate(), "dd/MM/yyyy").getDate().toInstant());

            if(user.isPresent()){
                dto2.setUserDemAttest(user.get());
            } else throw new EntityNotFoundException("Utilisateur inexistant");

            // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus().equalsIgnoreCase(EnumStatusType.VALIDEE.getCode())){
                    if(validator.isPresent())
                        notificationService.sendAttestationNotificationSender(dto2, validator.get().getPrenom()+ " "+validator.get().getNom());
                } else if(dto.getStatus().equalsIgnoreCase(EnumStatusType.REJETEE.getCode()))// Sinon notification du sender du rejet
                    notificationService.sendAttestationNotificationRejet(dto2);

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/attestation/status/{email}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeAttestationResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeAttestationResponse>> findByUserAndDateStatus(
            @PathVariable("email") String email,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status1")  String status1
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.scepersonnel, n.status, u1.nom, u1.prenom, u1.email, n.typedoc "+
                "FROM demandeattestation n "+
                "LEFT JOIN utilisateur u1 ON u1.identreprise=n.identreprise AND u1.id=n.user_id "+
                "WHERE (u1.email=:email OR n.scepersonnel=:email) AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandeAttestationResponse> liste = new ArrayList<DemandeAttestationResponse>();
        for (Object[] o : lst)
        {
            DemandeAttestationResponse cptble = new DemandeAttestationResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setScePersonnel(o[4].toString());
            if(o[5]!=null) cptble.setStatus(o[5].toString());
            if(o[6]!=null) cptble.setAuthor(o[6].toString());
            if(o[7]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[7].toString());
            if(o[8]!=null) cptble.setEmail(o[8].toString());
            if(o[9]!=null) cptble.setTypeDoc(o[9].toString());

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

    @DeleteMapping(value = APP_ROOT_PORTAIL + "/demande/attestation/{demandid}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Supprimer une demande pas encore validée", notes = "Cette methode permet de supprimer une demande pas encore validée")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    ResponseEntity<Void> delete(@PathVariable("demandid") Integer demandid) throws Exception {
        Optional<DemandeAttestation> entite = repository.findById(demandid);
        if(entite.isPresent()) repository.deleteById(demandid);

        // Notification validateur de l'annulation
        DemandeAttestationDto dto = DemandeAttestationDto.fromEntity(entite.get());
        notificationService.sendAnnulationAttestationNotification(dto, dto.getScePersonnel());

        return ResponseEntity.accepted().build();
    }
}
