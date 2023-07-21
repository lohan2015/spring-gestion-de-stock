package com.kinart.api.portail.controller;

import com.kinart.api.portail.dto.*;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PORTAIL;

@Api("notification-data")
@RestController
//@RequiredArgsConstructor
public class NotificationDataController {

    private GeneriqueConnexionService generiqueConnexionService;

    @Autowired
    public NotificationDataController(GeneriqueConnexionService generiqueConnexionService){
        this.generiqueConnexionService = generiqueConnexionService;
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/notification/absence-conge/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des notifcation", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<NotifabscongeDto>> findAbsCongeNotificationByUser(@PathVariable("email") String email){
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.sender, n.recipient, n.message "+
                        ", u1.nom as nomsender, u1.prenom as prenomsender, u2.nom as nomrecip, u2.prenom as prenomrecip "+
                        "FROM notifabsconge n "+
                        "LEFT JOIN utilisateur u1 ON u1.email=n.sender "+
                        "LEFT JOIN utilisateur u2 ON u2.email=n.recipient "+
                        "WHERE n.recipient=:email ORDER BY n.creation_date DESC";
        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<NotifabscongeDto> liste = new ArrayList<NotifabscongeDto>();
        for (Object[] o : lst)
        {
            NotifabscongeDto cptble = new NotifabscongeDto();
            if(o[0]!=null) cptble.setId(Integer.getInteger(o[0].toString()));
            if(o[1]!=null) cptble.setDate_created((Date) o[1]);
            if(o[2]!=null) cptble.setSender(o[2].toString());
            if(o[3]!=null) cptble.setRecipient(o[3].toString());
            if(o[4]!=null) cptble.setMessage(o[4].toString());
            if(o[5]!=null) cptble.setSenderName(o[5].toString());
            if(o[6]!=null) cptble.setSenderName(cptble.getSenderName()+" "+o[6].toString());
            if(o[7]!=null) cptble.setRecipientName(o[7].toString());
            if(o[8]!=null) cptble.setRecipientName(cptble.getRecipientName()+" "+o[8].toString());

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/notification/absence-conge-by-date/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des notifcation", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandePretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<NotifabscongeDto>> findAbsCongeNotificationByUserAndDate(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));

        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.sender, n.recipient, n.message "+
                ", u1.nom as nomsender, u1.prenom as prenomsender, u2.nom as nomrecip, u2.prenom as prenomrecip "+
                "FROM notifabsconge n "+
                "LEFT JOIN utilisateur u1 ON u1.email=n.sender "+
                "LEFT JOIN utilisateur u2 ON u2.email=n.recipient "+
                "WHERE n.recipient=:email AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<NotifabscongeDto> liste = new ArrayList<NotifabscongeDto>();
        for (Object[] o : lst)
        {
            NotifabscongeDto cptble = new NotifabscongeDto();
            if(o[0]!=null) cptble.setId(Integer.getInteger(o[0].toString()));
            if(o[1]!=null) cptble.setDate_created((Date) o[1]);
            if(o[2]!=null) cptble.setSender(o[2].toString());
            if(o[3]!=null) cptble.setRecipient(o[3].toString());
            if(o[4]!=null) cptble.setMessage(o[4].toString());
            if(o[5]!=null) cptble.setSenderName(o[5].toString());
            if(o[6]!=null) cptble.setSenderName(cptble.getSenderName()+" "+o[6].toString());
            if(o[7]!=null) cptble.setRecipientName(o[7].toString());
            if(o[8]!=null) cptble.setRecipientName(cptble.getRecipientName()+" "+o[8].toString());

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/notification/attestation/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des notifcation", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<NotifattestationDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<NotifattestationDto>> findAttestationNotificationByUser(@PathVariable("email") String email){
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.sender, n.recipient, n.message "+
                ", u1.nom as nomsender, u1.prenom as prenomsender, u2.nom as nomrecip, u2.prenom as prenomrecip "+
                "FROM notifattestation n "+
                "LEFT JOIN utilisateur u1 ON u1.email=n.sender "+
                "LEFT JOIN utilisateur u2 ON u2.email=n.recipient "+
                "WHERE n.recipient=:email ORDER BY n.creation_date DESC";
        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<NotifattestationDto> liste = new ArrayList<NotifattestationDto>();
        for (Object[] o : lst)
        {
            NotifattestationDto cptble = new NotifattestationDto();
            if(o[0]!=null) cptble.setId(Integer.getInteger(o[0].toString()));
            if(o[1]!=null) cptble.setDate_created((Date) o[1]);
            if(o[2]!=null) cptble.setSender(o[2].toString());
            if(o[3]!=null) cptble.setRecipient(o[3].toString());
            if(o[4]!=null) cptble.setMessage(o[4].toString());
            if(o[5]!=null) cptble.setSenderName(o[5].toString());
            if(o[6]!=null) cptble.setSenderName(cptble.getSenderName()+" "+o[6].toString());
            if(o[7]!=null) cptble.setRecipientName(o[7].toString());
            if(o[8]!=null) cptble.setRecipientName(cptble.getRecipientName()+" "+o[8].toString());

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/notification/attestation-by-date/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des notifcation", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<NotifattestationDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<NotifattestationDto>> findAttestationNotificationByUserAndDate(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));

        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.sender, n.recipient, n.message "+
                        ", u1.nom as nomsender, u1.prenom as prenomsender, u2.nom as nomrecip, u2.prenom as prenomrecip "+
                        "FROM notifattestation n "+
                        "LEFT JOIN utilisateur u1 ON u1.email=n.sender "+
                        "LEFT JOIN utilisateur u2 ON u2.email=n.recipient "+
                        "WHERE n.recipient=:email AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<NotifattestationDto> liste = new ArrayList<NotifattestationDto>();
        for (Object[] o : lst)
        {
            NotifattestationDto cptble = new NotifattestationDto();
            if(o[0]!=null) cptble.setId(Integer.getInteger(o[0].toString()));
            if(o[1]!=null) cptble.setDate_created((Date) o[1]);
            if(o[2]!=null) cptble.setSender(o[2].toString());
            if(o[3]!=null) cptble.setRecipient(o[3].toString());
            if(o[4]!=null) cptble.setMessage(o[4].toString());
            if(o[5]!=null) cptble.setSenderName(o[5].toString());
            if(o[6]!=null) cptble.setSenderName(cptble.getSenderName()+" "+o[6].toString());
            if(o[7]!=null) cptble.setRecipientName(o[7].toString());
            if(o[8]!=null) cptble.setRecipientName(cptble.getRecipientName()+" "+o[8].toString());

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/notification/habilitation/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des notifcation", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<NotifhabilitationDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<NotifhabilitationDto>> findHabilitationNotificationByUser(@PathVariable("email") String email){
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.sender, n.recipient, n.message "+
                        ", u1.nom as nomsender, u1.prenom as prenomsender, u2.nom as nomrecip, u2.prenom as prenomrecip "+
                        "FROM notifhabilitation n "+
                        "LEFT JOIN utilisateur u1 ON u1.email=n.sender "+
                        "LEFT JOIN utilisateur u2 ON u2.email=n.recipient "+
                        "WHERE n.recipient=:email ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);

        List<NotifhabilitationDto> liste = new ArrayList<NotifhabilitationDto>();
        for (Object[] o : lst)
        {
            NotifhabilitationDto cptble = new NotifhabilitationDto();
            if(o[0]!=null) cptble.setId(Integer.getInteger(o[0].toString()));
            if(o[1]!=null) cptble.setDate_created((Date) o[1]);
            if(o[2]!=null) cptble.setSender(o[2].toString());
            if(o[3]!=null) cptble.setRecipient(o[3].toString());
            if(o[4]!=null) cptble.setMessage(o[4].toString());
            if(o[5]!=null) cptble.setSenderName(o[5].toString());
            if(o[6]!=null) cptble.setSenderName(cptble.getSenderName()+" "+o[6].toString());
            if(o[7]!=null) cptble.setRecipientName(o[7].toString());
            if(o[8]!=null) cptble.setRecipientName(cptble.getRecipientName()+" "+o[8].toString());

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/notification/habilitation-by-date/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des notifcation", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<NotifhabilitationDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<NotifhabilitationDto>> findHabilitationNotificationByUserAndDate(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));

        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.sender, n.recipient, n.message "+
                        ", u1.nom as nomsender, u1.prenom as prenomsender, u2.nom as nomrecip, u2.prenom as prenomrecip "+
                        "FROM notifhabilitation n "+
                        "LEFT JOIN utilisateur u1 ON u1.email=n.sender "+
                        "LEFT JOIN utilisateur u2 ON u2.email=n.recipient "+
                        "WHERE n.recipient=:email AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);

        List<NotifhabilitationDto> liste = new ArrayList<NotifhabilitationDto>();
        for (Object[] o : lst)
        {
            NotifhabilitationDto cptble = new NotifhabilitationDto();
            if(o[0]!=null) cptble.setId(Integer.getInteger(o[0].toString()));
            if(o[1]!=null) cptble.setDate_created((Date) o[1]);
            if(o[2]!=null) cptble.setSender(o[2].toString());
            if(o[3]!=null) cptble.setRecipient(o[3].toString());
            if(o[4]!=null) cptble.setMessage(o[4].toString());
            if(o[5]!=null) cptble.setSenderName(o[5].toString());
            if(o[6]!=null) cptble.setSenderName(cptble.getSenderName()+" "+o[6].toString());
            if(o[7]!=null) cptble.setRecipientName(o[7].toString());
            if(o[8]!=null) cptble.setRecipientName(cptble.getRecipientName()+" "+o[8].toString());

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/notification/modifinfo-by-date/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des notifcation", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<NotifmodifinfoDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<NotifmodifinfoDto>> findModifInfoNotificationByUserAndDate(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));

        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.sender, n.recipient, n.message "+
                ", u1.nom as nomsender, u1.prenom as prenomsender, u2.nom as nomrecip, u2.prenom as prenomrecip "+
                "FROM notifhabilitation n "+
                "LEFT JOIN utilisateur u1 ON u1.email=n.sender "+
                "LEFT JOIN utilisateur u2 ON u2.email=n.recipient "+
                "WHERE n.recipient=:email AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);

        List<NotifmodifinfoDto> liste = new ArrayList<NotifmodifinfoDto>();
        for (Object[] o : lst)
        {
            NotifmodifinfoDto cptble = new NotifmodifinfoDto();
            if(o[0]!=null) cptble.setId(Integer.getInteger(o[0].toString()));
            if(o[1]!=null) cptble.setDate_created((Date) o[1]);
            if(o[2]!=null) cptble.setSender(o[2].toString());
            if(o[3]!=null) cptble.setRecipient(o[3].toString());
            if(o[4]!=null) cptble.setMessage(o[4].toString());
            if(o[5]!=null) cptble.setSenderName(o[5].toString());
            if(o[6]!=null) cptble.setSenderName(cptble.getSenderName()+" "+o[6].toString());
            if(o[7]!=null) cptble.setRecipientName(o[7].toString());
            if(o[8]!=null) cptble.setRecipientName(cptble.getRecipientName()+" "+o[8].toString());

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/notification/modifinfo/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des notifcation", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<NotifmodifinfoDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<NotifmodifinfoDto>> findModifInfoNotificationByUser(
            @PathVariable("email") String email
    ){
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.sender, n.recipient, n.message "+
                ", u1.nom as nomsender, u1.prenom as prenomsender, u2.nom as nomrecip, u2.prenom as prenomrecip "+
                "FROM notifhabilitation n "+
                "LEFT JOIN utilisateur u1 ON u1.email=n.sender "+
                "LEFT JOIN utilisateur u2 ON u2.email=n.recipient "+
                "WHERE n.recipient=:email ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);

        List<NotifmodifinfoDto> liste = new ArrayList<NotifmodifinfoDto>();
        for (Object[] o : lst)
        {
            NotifmodifinfoDto cptble = new NotifmodifinfoDto();
            if(o[0]!=null) cptble.setId(Integer.getInteger(o[0].toString()));
            if(o[1]!=null) cptble.setDate_created((Date) o[1]);
            if(o[2]!=null) cptble.setSender(o[2].toString());
            if(o[3]!=null) cptble.setRecipient(o[3].toString());
            if(o[4]!=null) cptble.setMessage(o[4].toString());
            if(o[5]!=null) cptble.setSenderName(o[5].toString());
            if(o[6]!=null) cptble.setSenderName(cptble.getSenderName()+" "+o[6].toString());
            if(o[7]!=null) cptble.setRecipientName(o[7].toString());
            if(o[8]!=null) cptble.setRecipientName(cptble.getRecipientName()+" "+o[8].toString());

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/notification/pret-by-date/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des notifcation", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<NotifpretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<NotifpretDto>> findPretNotificationByUserAndDate(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));

        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.sender, n.recipient, n.message "+
                        ", u1.nom as nomsender, u1.prenom as prenomsender, u2.nom as nomrecip, u2.prenom as prenomrecip "+
                        "FROM notifhabilitation n "+
                        "LEFT JOIN utilisateur u1 ON u1.email=n.sender "+
                        "LEFT JOIN utilisateur u2 ON u2.email=n.recipient "+
                        "WHERE n.recipient=:email AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);

        List<NotifpretDto> liste = new ArrayList<NotifpretDto>();
        for (Object[] o : lst)
        {
            NotifpretDto cptble = new NotifpretDto();
            if(o[0]!=null) cptble.setId(Integer.getInteger(o[0].toString()));
            if(o[1]!=null) cptble.setDate_created((Date) o[1]);
            if(o[2]!=null) cptble.setSender(o[2].toString());
            if(o[3]!=null) cptble.setRecipient(o[3].toString());
            if(o[4]!=null) cptble.setMessage(o[4].toString());
            if(o[5]!=null) cptble.setSenderName(o[5].toString());
            if(o[6]!=null) cptble.setSenderName(cptble.getSenderName()+" "+o[6].toString());
            if(o[7]!=null) cptble.setRecipientName(o[7].toString());
            if(o[8]!=null) cptble.setRecipientName(cptble.getRecipientName()+" "+o[8].toString());

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/notification/pret/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des notifcation", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<NotifpretDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<NotifpretDto>> findPretNotificationByUser(
            @PathVariable("email") String email
    ){
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.sender, n.recipient, n.message "+
                ", u1.nom as nomsender, u1.prenom as prenomsender, u2.nom as nomrecip, u2.prenom as prenomrecip "+
                "FROM notifhabilitation n "+
                "LEFT JOIN utilisateur u1 ON u1.email=n.sender "+
                "LEFT JOIN utilisateur u2 ON u2.email=n.recipient "+
                "WHERE n.recipient=:email ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);

        List<NotifpretDto> liste = new ArrayList<NotifpretDto>();
        for (Object[] o : lst)
        {
            NotifpretDto cptble = new NotifpretDto();
            if(o[0]!=null) cptble.setId(Integer.getInteger(o[0].toString()));
            if(o[1]!=null) cptble.setDate_created((Date) o[1]);
            if(o[2]!=null) cptble.setSender(o[2].toString());
            if(o[3]!=null) cptble.setRecipient(o[3].toString());
            if(o[4]!=null) cptble.setMessage(o[4].toString());
            if(o[5]!=null) cptble.setSenderName(o[5].toString());
            if(o[6]!=null) cptble.setSenderName(cptble.getSenderName()+" "+o[6].toString());
            if(o[7]!=null) cptble.setRecipientName(o[7].toString());
            if(o[8]!=null) cptble.setRecipientName(cptble.getRecipientName()+" "+o[8].toString());

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

}
