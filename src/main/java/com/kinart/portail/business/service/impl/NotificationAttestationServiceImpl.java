package com.kinart.portail.business.service.impl;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.mail.EmailDetails;
import com.kinart.api.mail.service.EmailService;
import com.kinart.api.portail.dto.DemandeHabilitationDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.portail.business.model.NotifHabilitation;
import com.kinart.portail.business.repository.NotifAbsCongeRepository;
import com.kinart.portail.business.repository.NotifHabilitationRepository;
import com.kinart.portail.business.service.NotificationHabilitationService;
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
public class NotificationHabilitationServiceImpl implements NotificationHabilitationService {

    private final NotifHabilitationRepository habilitationRepository;
    private final EmailService emailService;
    private final ParamDataRepository paramDataRepository;

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
    public void sendHabilitationNotificationSender(DemandeHabilitationDto dto, String validator) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Votre demande d'habilitation est soumise a la validation de $VALIDATOR."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "ACK_HABIL", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"ACK_HABIL"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"ACK_HABIL"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$$VALIDATOR", validator);

        NotifHabilitation notifHabilitation = new NotifHabilitation();
        notifHabilitation.setSender(dto.getUserDemHabil().getEmail());
        notifHabilitation.setRecipient(dto.getUserDemHabil().getEmail());
        notifHabilitation.setMessage(modele);
        notifHabilitation.setCreationDate(Instant.now());
        notifHabilitation.setLastModifiedDate(Instant.now());
        habilitationRepository.save(notifHabilitation);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifHabilitation.getMessage());
        paramMail.setRecipient(dto.getUserDemHabil().getEmail());
        paramMail.setSubject("Notification demande d'habilitation");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendHabilitationNotificationRejet(DemandeHabilitationDto dto) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "Votre demande d'habilitation du $DATE a été rejetée."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "REJ_HABIL", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"REJ_HABIL"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"REJ_HABIL"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
        else modele = fnom.getVall();

        modele = modele.replaceAll("\\$DATE", new SimpleDateFormat("MM-dd-yyyy").format(dto.getCreationDate()));

        NotifHabilitation notifHabilitation = new NotifHabilitation();
        notifHabilitation.setSender(dto.getUserDemHabil().getEmail());
        notifHabilitation.setRecipient(dto.getUserDemHabil().getEmail());
        notifHabilitation.setMessage(modele);
        notifHabilitation.setCreationDate(Instant.now());
        notifHabilitation.setLastModifiedDate(Instant.now());
        habilitationRepository.save(notifHabilitation);

        // Envoi notification
        EmailDetails paramMail = new EmailDetails();
        paramMail.setMsgBody(notifHabilitation.getMessage());
        paramMail.setRecipient(dto.getUserDemHabil().getEmail());
        paramMail.setSubject("Notification demande d'habilitation");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void sendAnnulationHabilitationNotification(DemandeHabilitationDto dto, String recipient) throws Exception {
        // Sauvegarde modification
        String modele = "Bonjour,"+
                        "La demande d'habilitation de $SENDER a été annulée par ce dernier."+
                        "Cdlt,"+
                        "RH SONIBANK";

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "ANN_HABIL", Integer.valueOf(1))
                .map(ParamDataDto::fromEntity)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Aucune donnée avec l'ID = "+"ANN_HABIL"+" n' ete trouve dans la table 99",
                                ErrorCodes.ARTICLE_NOT_FOUND)
                );

        if(fnom == null)
            throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"ANN_HABIL"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
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
        paramMail.setSubject("Notification annulation d'habilitation'");
        try {
            emailService.sendSimpleMail(paramMail);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
