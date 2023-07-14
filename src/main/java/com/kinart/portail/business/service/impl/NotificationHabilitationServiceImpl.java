package com.kinart.portail.business.service.impl;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.mail.EmailDetails;
import com.kinart.api.mail.service.EmailService;
import com.kinart.api.portail.dto.DemandeAbsenceCongeDto;
import com.kinart.api.portail.dto.DemandeHabilitationDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.portail.business.model.NotifAbsConge;
import com.kinart.portail.business.model.NotifHabilitation;
import com.kinart.portail.business.repository.NotifAbsCongeRepository;
import com.kinart.portail.business.repository.NotifHabilitationRepository;
import com.kinart.portail.business.service.NotificationAbsenceCongeService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationAbsenceCongeServiceImpl implements NotificationAbsenceCongeService {

    private final NotifAbsCongeRepository absCongeRepository;
    private final NotifHabilitationRepository habilitationRepository;
    private final EmailService emailService;
    private final ParamDataRepository paramDataRepository;

    @Override
    @Transactional
    public void sendAbsenceCongeNotification(DemandeAbsenceCongeDto dto, String recipient) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Merci de traiter la demande de congé de $SENDER pour la période du $DATEDEBUT au $DATEFIN."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "MOD_ABSCGE", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"MOD_ABSCGE"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"MOD_ABSCGE"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$SENDER", dto.getUserDemAbsCg().getPrenom()+" "+dto.getUserDemAbsCg().getNom());
        modele = modele.replaceAll("\\$DATEDEBUT", new SimpleDateFormat("MM-dd-yyyy").format(dto.getDteDebut()));
        modele = modele.replaceAll("\\$DATEFIN", new SimpleDateFormat("MM-dd-yyyy").format(dto.getDteFin()));

        NotifAbsConge notifAbsConge = new NotifAbsConge();
        notifAbsConge.setSender(dto.getUserDemAbsCg().getEmail());
        notifAbsConge.setRecipient(recipient);
        notifAbsConge.setMessage(modele);
        notifAbsConge.setCreationDate(Instant.now());
        notifAbsConge.setLastModifiedDate(Instant.now());
        absCongeRepository.save(notifAbsConge);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifAbsConge.getMessage());
        paramMail.setRecipient(recipient);
        paramMail.setSubject("Notification absence / congé");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendHabilitationNotification(DemandeHabilitationDto dto, String recipient) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                "Merci de traiter la demande d'habilitation de $SENDER."+
                "Cdlt,"+
                "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "MOD_HABIL", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"MOD_HABIL"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"MOD_HABIL"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$SENDER", dto.getUserDemHabil().getPrenom()+" "+dto.getUserDemHabil().getNom());

        NotifHabilitation notifHabilitation = new NotifHabilitation();
        notifHabilitation.setSender(dto.getUserDemHabil().getEmail());
        notifHabilitation.setRecipient(recipient);
        notifHabilitation.setMessage(modele);
        notifHabilitation.setCreationDate(Instant.now());
        notifHabilitation.setLastModifiedDate(Instant.now());
        habilitationRepository.save(notifHabilitation);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifHabilitation.getMessage());
        paramMail.setRecipient(recipient);
        paramMail.setSubject("Notification demande habilitation");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendAbsCongeNotificationSender(DemandeAbsenceCongeDto dto, String validator) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Votre demande d'absence / congé est soumise a la validation de $VALIDATOR."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "ACK_ABSCGE", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"ACK_ABSCGE"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"ACK_ABSCGE"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$$VALIDATOR", validator);

        NotifHabilitation notifHabilitation = new NotifHabilitation();
        notifHabilitation.setSender(dto.getUserDemAbsCg().getEmail());
        notifHabilitation.setRecipient(dto.getUserDemAbsCg().getEmail());
        notifHabilitation.setMessage(modele);
        notifHabilitation.setCreationDate(Instant.now());
        notifHabilitation.setLastModifiedDate(Instant.now());
        habilitationRepository.save(notifHabilitation);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifHabilitation.getMessage());
        paramMail.setRecipient(dto.getUserDemAbsCg().getEmail());
        paramMail.setSubject("Notification demande absence / congé");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendAbsCongeNotificationRejet(DemandeAbsenceCongeDto dto) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Votre demande d'absence / congé pour la période du $DATEDEBUT au $DATEFIN a été rejetée."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "REJ_ABSCGE", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"REJ_ABSCGE"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"REJ_ABSCGE"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$DATEDEBUT", new SimpleDateFormat("MM-dd-yyyy").format(dto.getDteDebut()));
        modele = modele.replaceAll("\\$DATEFIN", new SimpleDateFormat("MM-dd-yyyy").format(dto.getDteFin()));

        NotifHabilitation notifHabilitation = new NotifHabilitation();
        notifHabilitation.setSender(dto.getUserDemAbsCg().getEmail());
        notifHabilitation.setRecipient(dto.getUserDemAbsCg().getEmail());
        notifHabilitation.setMessage(modele);
        notifHabilitation.setCreationDate(Instant.now());
        notifHabilitation.setLastModifiedDate(Instant.now());
        habilitationRepository.save(notifHabilitation);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifHabilitation.getMessage());
        paramMail.setRecipient(dto.getUserDemAbsCg().getEmail());
        paramMail.setSubject("Notification demande absence / congé");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendAnnulationAbsCgeNotification(DemandeAbsenceCongeDto dto, String recipient) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "La demande d'absence / congé de $SENDER a été annulée par ce dernier."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "ANN_ABSCGE", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"ANN_ABSCGE"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"ANN_ABSCGE"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$SENDER", dto.getUserDemAbsCg().getPrenom()+" "+dto.getUserDemAbsCg().getNom());

        NotifHabilitation notifHabilitation = new NotifHabilitation();
        notifHabilitation.setSender(dto.getUserDemAbsCg().getEmail());
        notifHabilitation.setRecipient(recipient);
        notifHabilitation.setMessage(modele);
        notifHabilitation.setCreationDate(Instant.now());
        notifHabilitation.setLastModifiedDate(Instant.now());
        habilitationRepository.save(notifHabilitation);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifHabilitation.getMessage());
        paramMail.setRecipient(recipient);
        paramMail.setSubject("Notification annulation demande absence / congé");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
