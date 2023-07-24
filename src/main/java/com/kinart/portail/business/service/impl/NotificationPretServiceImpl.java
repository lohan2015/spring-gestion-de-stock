package com.kinart.portail.business.service.impl;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.mail.EmailDetails;
import com.kinart.api.mail.service.EmailService;
import com.kinart.api.portail.dto.DemandePretDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.portail.business.model.NotifPret;
import com.kinart.portail.business.repository.NotifPretRepository;
import com.kinart.portail.business.service.NotificationPretService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationPretServiceImpl implements NotificationPretService {

    private final NotifPretRepository pretRepository;
    private final EmailService emailService;
    private final ParamDataRepository paramDataRepository;

    @Override
    @Transactional
    public void sendPretNotification(DemandePretDto dto, String recipient) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Merci de traiter la demande de prêt de $SENDER."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "MOD_PRET", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"MOD_PRET"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"MOD_PRET"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$SENDER", dto.getUserDemPret().getPrenom()+" "+dto.getUserDemPret().getNom());
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifPret notifPret = new NotifPret();
        notifPret.setSender(dto.getUserDemPret().getEmail());
        notifPret.setRecipient(recipient);
        notifPret.setMessage(modele);
        notifPret.setCreationDate(Instant.now());
        notifPret.setLastModifiedDate(Instant.now());
        pretRepository.save(notifPret);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifPret.getMessage());
        paramMail.setRecipient(recipient);
        paramMail.setSubject("Notification demande de prêt");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendPretNotificationSender(DemandePretDto dto, String validator) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Votre demande de prêt est soumise a la validation de $VALIDATOR."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "ACK_PRET", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"ACK_PRET"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"ACK_PRET"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$VALIDATOR", validator);
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifPret notifPret = new NotifPret();
        notifPret.setSender(dto.getUserDemPret().getEmail());
        notifPret.setRecipient(dto.getUserDemPret().getEmail());
        notifPret.setMessage(modele);
        notifPret.setCreationDate(Instant.now());
        notifPret.setLastModifiedDate(Instant.now());
        pretRepository.save(notifPret);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifPret.getMessage());
        paramMail.setRecipient(dto.getUserDemPret().getEmail());
        paramMail.setSubject("Notification demande de prêt");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendPretNotificationRejet(DemandePretDto dto) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Votre demande de prêt du $DATE a été rejetée."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "REJ_PRET", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"REJ_PRET"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"REJ_PRET"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$DATE", new SimpleDateFormat("dd-MM-yyyy").format(Date.from(dto.getCreationDate())));
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifPret notifPret = new NotifPret();
        notifPret.setSender(dto.getUserDemPret().getEmail());
        notifPret.setRecipient(dto.getUserDemPret().getEmail());
        notifPret.setMessage(modele);
        notifPret.setCreationDate(Instant.now());
        notifPret.setLastModifiedDate(Instant.now());
        pretRepository.save(notifPret);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifPret.getMessage());
        paramMail.setRecipient(dto.getUserDemPret().getEmail());
        paramMail.setSubject("Notification demande de prêt");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendAnnulationPretNotification(DemandePretDto dto, String recipient) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "La demande de prêt de $SENDER a été annulée par ce dernier."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "ANN_PRET", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"ANN_PRET"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"ANN_PRET"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$SENDER", dto.getUserDemPret().getPrenom()+" "+dto.getUserDemPret().getNom());
        modele = modele.replaceAll("\\$NUMERO", String.valueOf(dto.getId()));

        NotifPret notifPret = new NotifPret();
        notifPret.setSender(dto.getUserDemPret().getEmail());
        notifPret.setRecipient(recipient);
        notifPret.setMessage(modele);
        notifPret.setCreationDate(Instant.now());
        notifPret.setLastModifiedDate(Instant.now());
        pretRepository.save(notifPret);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifPret.getMessage());
        paramMail.setRecipient(recipient);
        paramMail.setSubject("Notification annulation demande de prêt");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
